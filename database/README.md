# 📊 Scripts de Base de Datos - Sabor Foráneo

Esta carpeta contiene todos los scripts SQL necesarios para configurar la base de datos en Supabase.

## 📁 Archivos

### `setup_database.sql`
Script principal para crear toda la estructura de la base de datos:
- ✅ Tablas (usuarios, recetas, favoritos)
- ✅ Índices para optimización
- ✅ Funciones y triggers
- ✅ Row Level Security (RLS)
- ✅ Políticas de seguridad

## 🚀 Cómo usar

### 1. Acceder al SQL Editor de Supabase
1. Ve a https://supabase.com/dashboard
2. Abre tu proyecto
3. Click en **SQL Editor** en el menú lateral
4. Click en **New Query**

### 2. Ejecutar el script
1. Copia todo el contenido de `setup_database.sql`
2. Pégalo en el editor SQL
3. Click en **Run** o presiona `Ctrl/Cmd + Enter`
4. Espera a que se ejecute completamente

### 3. Verificar la instalación
Ejecuta las consultas de verificación al final del script para confirmar que todo está correcto:
- 3 tablas creadas
- Múltiples índices
- Políticas RLS activas

## 📋 Estructura de Tablas

### `usuarios`
Perfiles de usuarios con información básica y rol (USUARIO o COCINERO).

### `recetas`
Recetas creadas por cocineros con ingredientes, pasos, categoría, etc.

### `favoritos`
Relación muchos-a-muchos entre usuarios y sus recetas favoritas.

## 🔒 Seguridad

El script configura Row Level Security (RLS) con políticas que garantizan:
- ✅ Usuarios solo pueden editar su propio perfil
- ✅ Solo cocineros pueden crear recetas
- ✅ Todos pueden ver recetas públicas
- ✅ Usuarios solo ven sus propios favoritos

## 📝 Notas

- Ejecuta el script solo una vez
- Si necesitas resetear, elimina las tablas antes de ejecutar de nuevo
- Las credenciales de Supabase están en `local.properties` (no commitear)

## 🆘 Troubleshooting

**Error: "relation already exists"**
- Las tablas ya existen. Elimínalas o usa `DROP TABLE IF EXISTS` antes.

**Error: "permission denied"**
- Asegúrate de estar usando el usuario admin de Supabase.

**Error: "RLS policy violation"**
- Verifica que las políticas RLS estén correctamente configuradas.
