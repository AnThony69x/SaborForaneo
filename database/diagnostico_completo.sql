-- =====================================================
-- DIAGNÓSTICO COMPLETO - EJECUTAR EN SUPABASE
-- =====================================================

-- 1. Ver si existe la tabla usuarios
SELECT EXISTS (
    SELECT FROM information_schema.tables 
    WHERE table_schema = 'public' 
    AND table_name = 'usuarios'
) as tabla_existe;

-- 2. Ver estructura de la tabla (si existe)
SELECT 
    column_name,
    data_type,
    is_nullable,
    column_default
FROM information_schema.columns
WHERE table_name = 'usuarios'
ORDER BY ordinal_position;

-- 3. Ver usuarios en auth
SELECT 
    id,
    email,
    email_confirmed_at,
    created_at
FROM auth.users
ORDER BY created_at DESC
LIMIT 5;

-- 4. Ver usuarios en tabla usuarios
SELECT 
    id,
    email,
    nombre,
    role,
    created_at
FROM usuarios
ORDER BY created_at DESC
LIMIT 5;

-- 5. Ver políticas RLS
SELECT 
    schemaname,
    tablename,
    policyname,
    cmd as operacion,
    qual as condicion
FROM pg_policies
WHERE tablename = 'usuarios';

-- 6. Verificar que RLS está habilitado
SELECT 
    schemaname,
    tablename,
    rowsecurity as rls_habilitado
FROM pg_tables
WHERE tablename = 'usuarios';

-- =====================================================
-- RESULTADO ESPERADO:
-- =====================================================
-- ✅ tabla_existe = true
-- ✅ Debe mostrar columnas: id, auth_id, email, nombre, role, etc.
-- ✅ Debe haber usuarios en auth.users
-- ✅ Debe haber políticas RLS activas
-- ✅ rls_habilitado = true
