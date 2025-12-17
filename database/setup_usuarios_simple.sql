-- =====================================================
-- SETUP RÁPIDO: TABLA USUARIOS PARA SABOR FORÁNEO
-- =====================================================
-- Ejecuta este script completo en Supabase SQL Editor

-- =========================================
-- 1. ELIMINAR TABLA SI YA EXISTE
-- =========================================
DROP TABLE IF EXISTS usuarios CASCADE;

-- =========================================
-- 2. CREAR TABLA USUARIOS
-- =========================================
CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    auth_id UUID NOT NULL UNIQUE REFERENCES auth.users(id) ON DELETE CASCADE,
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

-- =========================================
-- 3. CREAR ÍNDICES
-- =========================================
CREATE INDEX idx_usuarios_auth_id ON usuarios(auth_id);
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_role ON usuarios(role);

-- =========================================
-- 4. FUNCIÓN PARA ACTUALIZAR updated_at
-- =========================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- =========================================
-- 5. TRIGGER PARA MANTENER updated_at
-- =========================================
CREATE TRIGGER trigger_update_usuarios_updated_at
    BEFORE UPDATE ON usuarios
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- =========================================
-- 6. HABILITAR ROW LEVEL SECURITY (RLS)
-- =========================================
ALTER TABLE usuarios ENABLE ROW LEVEL SECURITY;

-- =========================================
-- 7. POLÍTICAS RLS - ⚠️ MUY IMPORTANTE
-- =========================================

-- Todos pueden ver todos los perfiles públicos
CREATE POLICY "Lectura pública de perfiles"
    ON usuarios FOR SELECT
    USING (true);

-- Los usuarios pueden crear su propio perfil al registrarse
CREATE POLICY "Insertar propio perfil"
    ON usuarios FOR INSERT
    WITH CHECK (auth.uid() = auth_id);

-- Los usuarios solo pueden actualizar su propio perfil
CREATE POLICY "Actualizar propio perfil"
    ON usuarios FOR UPDATE
    USING (auth.uid() = auth_id)
    WITH CHECK (auth.uid() = auth_id);

-- =========================================
-- 8. VERIFICAR INSTALACIÓN
-- =========================================

-- Ver la tabla creada
SELECT 
    table_name,
    (SELECT COUNT(*) FROM usuarios) as total_usuarios
FROM information_schema.tables 
WHERE table_schema = 'public' 
  AND table_name = 'usuarios';

-- Ver políticas RLS activas
SELECT 
    schemaname, 
    tablename, 
    policyname,
    cmd as operacion
FROM pg_policies 
WHERE tablename = 'usuarios';

-- =========================================
-- ✅ LISTO PARA USAR
-- =========================================
-- Ahora puedes registrar usuarios desde la app
-- Los roles se asignan automáticamente:
-- • admin@saborforaneo.com → COCINERO
-- • Otros emails → USUARIO
