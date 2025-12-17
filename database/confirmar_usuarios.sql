-- =====================================================
-- SOLUCIÓN: Confirmar usuarios existentes
-- =====================================================
-- Si creaste usuarios ANTES de desactivar la confirmación,
-- ejecuta este script en Supabase SQL Editor

-- Ver usuarios sin confirmar
SELECT 
    id,
    email,
    email_confirmed_at,
    created_at
FROM auth.users
WHERE email_confirmed_at IS NULL;

-- Confirmar TODOS los usuarios no confirmados
UPDATE auth.users
SET email_confirmed_at = NOW()
WHERE email_confirmed_at IS NULL;

-- Verificar que se confirmaron
SELECT 
    email,
    email_confirmed_at,
    CASE 
        WHEN email_confirmed_at IS NOT NULL THEN '✅ Confirmado'
        ELSE '❌ No confirmado'
    END as estado
FROM auth.users;
