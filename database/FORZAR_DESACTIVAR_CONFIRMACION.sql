-- =====================================================
-- CONFIRMAR USUARIOS EXISTENTES MANUALMENTE
-- =====================================================
-- Ejecuta este SQL en Supabase SQL Editor

-- Confirma TODOS los usuarios que están esperando verificación
UPDATE auth.users
SET email_confirmed_at = NOW()
WHERE email_confirmed_at IS NULL;

-- Verifica que se confirmaron
SELECT 
    id,
    email,
    email_confirmed_at,
    confirmed_at,
    CASE 
        WHEN email_confirmed_at IS NOT NULL THEN 'Confirmado'
        ELSE 'No confirmado'
    END as estado
FROM auth.users
ORDER BY created_at DESC;
