# 🍽️ Sabor Foráneo
Aplicación móvil de recetas ecuatorianas e internacionales desarrollada en **Kotlin** con **Jetpack Compose**.

## 📱 Características
- ✅ **Interfaz Moderna**: Material Design 3 con animaciones fluidas.
- 🌓 **Temas Personalizables**: Modo claro/oscuro + 5 paletas de colores.
- 🔔 **Notificaciones Push**: Alertas de recetas del día.
- 📍 **Ubicación**: Recomendaciones basadas en tu región.
- 🔍 **Búsqueda Avanzada**: Filtros por categoría, dificultad y precio.
- ⭐ **Favoritos**: Guarda tus recetas preferidas.
- 👤 **Perfil Personalizable**: Preferencias alimentarias.

## 🛠️ Tecnologías
| Categoría | Tecnología |
|-----------|------------|
| Lenguaje | Kotlin |
| UI | Jetpack Compose |
| Arquitectura | MVVM |
| Navegación | Navigation Compose |
| Permisos | Runtime Permissions (Android 13+) |
| Notificaciones | NotificationCompat |

## 📂 Estructura del Proyecto
app/src/main/kotlin/com/example/saborforaneo/
├── data/
│   ├── mock/            # Datos de recetas (JSON)
│   └── model/           # Modelos de datos
├── notifications/        # Sistema de notificaciones
├── permissions/          # Manejo de permisos
├── ui/
│   ├── components/      # Componentes reutilizables
│   ├── navigation/      # Sistema de navegación
│   ├── screens/         # Pantallas de la app
│   └── theme/           # Temas y colores
└── MainActivity.kt

## 🚀 Instalación
https://github.com/AnThony69x/SaborForaneo.git
Abre Android Studio, sincroniza Gradle y ejecuta (Android 8.0+)

## 📋 Pantallas
Splash, Onboarding, Login, Home, Búsqueda, Detalle, Favoritos, Perfil

## 📌 Permisos
Notificaciones (Android 13+), Ubicación (GPS)

## 🎨 Temas
Verde, Rojo, Azul, Naranja, Morado

## 👨‍💻 Autor
AnThony69x

## 📄 Licencia
MIT
