# 🔧 Solución: Email inválido en Supabase

## ❌ Problema
El error "Email address 'xxx@gmail.com' is invalid" aparece al intentar registrar usuarios.

## ✅ Soluciones (en orden de prioridad)

### 1. **Ejecuta el script SQL** ⭐ MÁS IMPORTANTE
Si no lo has hecho, **debes crear las tablas primero**:

1. Ve a: https://sjlrxypzibhgyfetnkbt.supabase.co
2. Click en **SQL Editor** (menú izquierdo)
3. Click en **New Query**
4. Copia TODO el contenido de `database/setup_database.sql`
5. Click en **Run** (o Ctrl+Enter)
6. Verifica que aparezca: "Success. No rows returned"

### 2. **Configura Email Provider en Supabase**
1. Ve a: **Authentication** → **Providers** → **Email**
2. Asegúrate de que:
   - ✅ **Enable Email provider** esté ACTIVADO
   - ❌ **Confirm email** esté DESACTIVADO (para testing)
3. Click en **Save**

### 3. **Verifica la configuración de Email**
En el Dashboard de Supabase:
1. Ve a **Authentication** → **Settings**
2. En **Email Auth** verifica:
   - **Enable email signups**: ✅ Activado
   - **Enable email confirmations**: ❌ Desactivado (para testing)

### 4. **Prueba con estos emails**
- `test@example.com` (funciona siempre)
- `admin@saborforaneo.com` (te dará rol COCINERO)
- Tu email real de gmail/outlook

### 5. **Revisa los logs de Supabase**
1. Ve a: **Logs** → **Auth Logs**
2. Busca el intento de registro
3. Ve el error exacto que Supabase está generando

### 6. **Verifica que local.properties esté correcto**
```properties
SUPABASE_URL=https://sjlrxypzibhgyfetnkbt.supabase.co
SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 🔍 Debugging

### Ver logs en Android Studio:
1. Abre **Logcat**
2. Filtra por: `AuthRepository`
3. Busca mensajes como:
   - "Intentando registrar: email='...'"
   - "Error en signUpWith: ..."

### Los logs te dirán exactamente:
- ✅ El email que se está enviando
- ✅ El error exacto de Supabase
- ✅ En qué paso falla (Auth o inserción en tabla)

## 🎯 Cambios recientes en el código:

1. ✅ Email se limpia automáticamente (trim + lowercase)
2. ✅ Logs detallados para debugging
3. ✅ Mensajes de error más claros
4. ✅ Sugerencias visuales en la UI
5. ✅ Validación mejorada

## 📝 Pasos siguientes:

1. **Rebuild Project** en Android Studio
2. **Ejecuta el SQL** si no lo has hecho
3. **Configura Email Provider** en Supabase
4. **Intenta registrarte** de nuevo
5. **Revisa los logs** en Logcat para ver el error exacto

## 🆘 Si sigue sin funcionar:

Mándame:
1. Los logs de Logcat (busca "AuthRepository")
2. Screenshot de Authentication → Providers → Email en Supabase
3. Screenshot de los Auth Logs en Supabase
