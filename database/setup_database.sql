-- =====================================================
-- SABOR FORÁNEO - Scripts SQL para Supabase
-- =====================================================
-- Ejecuta estos scripts en orden en el SQL Editor de Supabase
-- Dashboard → SQL Editor → New Query

-- =====================================================
-- 1. CREAR TABLAS
-- =====================================================

-- Tabla de usuarios
CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    auth_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    email TEXT NOT NULL UNIQUE,
    nombre TEXT NOT NULL,
    role TEXT DEFAULT 'USUARIO' CHECK (role IN ('USUARIO', 'COCINERO')),
    telefono TEXT,
    direccion TEXT,
    foto_perfil TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

COMMENT ON TABLE usuarios IS 'Perfiles de usuarios de la aplicación';
COMMENT ON COLUMN usuarios.role IS 'USUARIO: puede ver recetas, COCINERO: puede crear recetas';

-- Tabla de recetas
CREATE TABLE recetas (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cocinero_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    nombre TEXT NOT NULL,
    descripcion TEXT NOT NULL,
    ingredientes TEXT[] NOT NULL,
    pasos TEXT[] NOT NULL,
    tiempo_preparacion INTEGER NOT NULL CHECK (tiempo_preparacion > 0),
    porciones INTEGER NOT NULL CHECK (porciones > 0),
    dificultad TEXT NOT NULL CHECK (dificultad IN ('FACIL', 'MEDIA', 'DIFICIL')),
    precio TEXT NOT NULL CHECK (precio IN ('ECONOMICO', 'MODERADO', 'COSTOSO')),
    categoria TEXT NOT NULL,
    tags TEXT[] DEFAULT '{}',
    imagenes TEXT[] DEFAULT '{}',
    video_url TEXT,
    cantidad_vistas INTEGER DEFAULT 0 CHECK (cantidad_vistas >= 0),
    calificacion_promedio DECIMAL(3,2) CHECK (calificacion_promedio >= 0 AND calificacion_promedio <= 5),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

COMMENT ON TABLE recetas IS 'Recetas creadas por cocineros';
COMMENT ON COLUMN recetas.ingredientes IS 'Array de ingredientes necesarios';
COMMENT ON COLUMN recetas.pasos IS 'Array de pasos de preparación';

-- Tabla de favoritos
CREATE TABLE favoritos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    receta_id UUID NOT NULL REFERENCES recetas(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(usuario_id, receta_id)
);

COMMENT ON TABLE favoritos IS 'Relación muchos a muchos entre usuarios y recetas favoritas';

-- =====================================================
-- 2. CREAR ÍNDICES
-- =====================================================

-- Índices para mejorar el rendimiento de las consultas
CREATE INDEX idx_usuarios_auth_id ON usuarios(auth_id);
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_role ON usuarios(role);

CREATE INDEX idx_recetas_cocinero ON recetas(cocinero_id);
CREATE INDEX idx_recetas_categoria ON recetas(categoria);
CREATE INDEX idx_recetas_created_at ON recetas(created_at DESC);
CREATE INDEX idx_recetas_cantidad_vistas ON recetas(cantidad_vistas DESC);
CREATE INDEX idx_recetas_calificacion ON recetas(calificacion_promedio DESC);

CREATE INDEX idx_favoritos_usuario ON favoritos(usuario_id);
CREATE INDEX idx_favoritos_receta ON favoritos(receta_id);

-- Índice GIN para búsqueda de texto completo
CREATE INDEX idx_recetas_nombre_gin ON recetas USING gin(to_tsvector('spanish', nombre));
CREATE INDEX idx_recetas_descripcion_gin ON recetas USING gin(to_tsvector('spanish', descripcion));
CREATE INDEX idx_recetas_tags_gin ON recetas USING gin(tags);

-- =====================================================
-- 3. CREAR FUNCIONES Y TRIGGERS
-- =====================================================

-- Función para actualizar el campo updated_at automáticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Triggers para updated_at
CREATE TRIGGER update_usuarios_updated_at 
    BEFORE UPDATE ON usuarios
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_recetas_updated_at 
    BEFORE UPDATE ON recetas
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Función para incrementar vistas de receta
CREATE OR REPLACE FUNCTION increment_receta_views(receta_uuid UUID)
RETURNS void AS $$
BEGIN
    UPDATE recetas
    SET cantidad_vistas = cantidad_vistas + 1
    WHERE id = receta_uuid;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- =====================================================
-- 4. HABILITAR ROW LEVEL SECURITY (RLS)
-- =====================================================

ALTER TABLE usuarios ENABLE ROW LEVEL SECURITY;
ALTER TABLE recetas ENABLE ROW LEVEL SECURITY;
ALTER TABLE favoritos ENABLE ROW LEVEL SECURITY;

-- =====================================================
-- 5. POLÍTICAS RLS - TABLA USUARIOS
-- =====================================================

-- Todos pueden ver todos los perfiles públicos
CREATE POLICY "Lectura pública de perfiles"
    ON usuarios FOR SELECT
    USING (true);

-- Los usuarios pueden crear su propio perfil
CREATE POLICY "Insertar propio perfil"
    ON usuarios FOR INSERT
    WITH CHECK (auth.uid() = auth_id);

-- Los usuarios solo pueden actualizar su propio perfil
CREATE POLICY "Actualizar propio perfil"
    ON usuarios FOR UPDATE
    USING (auth.uid() = auth_id)
    WITH CHECK (auth.uid() = auth_id);

-- Los usuarios no pueden eliminar perfiles (solo mediante CASCADE de auth)
-- No se crea política de DELETE

-- =====================================================
-- 6. POLÍTICAS RLS - TABLA RECETAS
-- =====================================================

-- Todos pueden leer todas las recetas (público)
CREATE POLICY "Lectura pública de recetas"
    ON recetas FOR SELECT
    USING (true);

-- Solo los cocineros pueden crear recetas
CREATE POLICY "Cocineros pueden crear recetas"
    ON recetas FOR INSERT
    WITH CHECK (
        EXISTS (
            SELECT 1 FROM usuarios
            WHERE id = cocinero_id
            AND auth_id = auth.uid()
            AND role = 'COCINERO'
        )
    );

-- Los cocineros solo pueden actualizar sus propias recetas
CREATE POLICY "Actualizar propias recetas"
    ON recetas FOR UPDATE
    USING (
        EXISTS (
            SELECT 1 FROM usuarios
            WHERE id = cocinero_id
            AND auth_id = auth.uid()
        )
    )
    WITH CHECK (
        EXISTS (
            SELECT 1 FROM usuarios
            WHERE id = cocinero_id
            AND auth_id = auth.uid()
        )
    );

-- Los cocineros solo pueden eliminar sus propias recetas
CREATE POLICY "Eliminar propias recetas"
    ON recetas FOR DELETE
    USING (
        EXISTS (
            SELECT 1 FROM usuarios
            WHERE id = cocinero_id
            AND auth_id = auth.uid()
        )
    );

-- =====================================================
-- 7. POLÍTICAS RLS - TABLA FAVORITOS
-- =====================================================

-- Los usuarios solo pueden ver sus propios favoritos
CREATE POLICY "Ver propios favoritos"
    ON favoritos FOR SELECT
    USING (
        EXISTS (
            SELECT 1 FROM usuarios
            WHERE id = usuario_id
            AND auth_id = auth.uid()
        )
    );

-- Los usuarios pueden agregar favoritos a su propia lista
CREATE POLICY "Agregar a favoritos"
    ON favoritos FOR INSERT
    WITH CHECK (
        EXISTS (
            SELECT 1 FROM usuarios
            WHERE id = usuario_id
            AND auth_id = auth.uid()
        )
    );

-- Los usuarios pueden eliminar de sus propios favoritos
CREATE POLICY "Eliminar de favoritos"
    ON favoritos FOR DELETE
    USING (
        EXISTS (
            SELECT 1 FROM usuarios
            WHERE id = usuario_id
            AND auth_id = auth.uid()
        )
    );

-- =====================================================
-- 8. DATOS DE PRUEBA (OPCIONAL)
-- =====================================================

-- NOTA: Primero debes crear usuarios desde la app
-- Estos son solo ejemplos de cómo insertar datos manualmente

/*
-- Ejemplo: Insertar un usuario de prueba (requiere auth_id válido)
INSERT INTO usuarios (auth_id, email, nombre, role) VALUES
    ('UUID_DEL_AUTH_USER', 'cocinero@test.com', 'Chef Prueba', 'COCINERO');

-- Ejemplo: Insertar recetas de prueba
INSERT INTO recetas (
    cocinero_id, 
    nombre, 
    descripcion, 
    ingredientes, 
    pasos,
    tiempo_preparacion,
    porciones,
    dificultad,
    precio,
    categoria,
    tags
) VALUES (
    'UUID_DEL_COCINERO',
    'Tacos al Pastor',
    'Deliciosos tacos mexicanos con carne marinada',
    ARRAY['500g carne de cerdo', '2 piñas', '1 cebolla', 'Cilantro', 'Tortillas'],
    ARRAY['Marinar la carne', 'Asar la carne', 'Cortar en trozos', 'Servir en tortillas'],
    45,
    4,
    'MEDIA',
    'ECONOMICO',
    'Mexicana',
    ARRAY['mexicana', 'tacos', 'carne']
);
*/

-- =====================================================
-- 9. VERIFICAR INSTALACIÓN
-- =====================================================

-- Ejecuta estas consultas para verificar que todo esté correcto

-- Ver todas las tablas creadas
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public' 
  AND table_name IN ('usuarios', 'recetas', 'favoritos');

-- Ver políticas RLS
SELECT schemaname, tablename, policyname 
FROM pg_policies 
WHERE tablename IN ('usuarios', 'recetas', 'favoritos');

-- Ver índices creados
SELECT tablename, indexname 
FROM pg_indexes 
WHERE schemaname = 'public' 
  AND tablename IN ('usuarios', 'recetas', 'favoritos');

-- =====================================================
-- FIN DEL SCRIPT
-- =====================================================

-- Si todo se ejecutó correctamente, deberías ver:
-- ✓ 3 tablas creadas (usuarios, recetas, favoritos)
-- ✓ Múltiples índices para optimización
-- ✓ Políticas RLS activas para seguridad
-- ✓ Triggers para updated_at

-- Siguiente paso: Configurar Storage para imágenes (ver STORAGE_SETUP.sql)
