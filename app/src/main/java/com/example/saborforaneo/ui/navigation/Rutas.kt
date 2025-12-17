package com.example.saborforaneo.ui.navigation

sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Onboarding : Rutas("onboarding")
    object Login : Rutas("login")
    object Registro : Rutas("registro")
    object ConfirmacionEmail : Rutas("confirmacion_email/{email}") {
        fun crearRuta(email: String) = "confirmacion_email/$email"
    }
    object RecuperarContrasena : Rutas("recuperar_contrasena")
    object TerminosCondiciones : Rutas("terminos_condiciones")
    object Inicio : Rutas("inicio")
    object Busqueda : Rutas("busqueda")
    object Favoritos : Rutas("favoritos")
    object Perfil : Rutas("perfil")
    object DetalleReceta : Rutas("detalle/{recetaId}") {
        fun crearRuta(recetaId: String) = "detalle/$recetaId"
    }
}