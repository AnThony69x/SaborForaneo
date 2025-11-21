package com.example.saborforaneo.ui.screens.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class TemaColor(val nombreMostrar: String, val colorPrimario: Long) {
    VERDE("Verde Clásico", 0xFF4CAF50),
    ROJO("Rojo Picante", 0xFFE53935),
    AZUL("Azul Océano", 0xFF6B9FBF),
    NARANJA("Naranja Tropical", 0xFFFF6F00),
    MORADO("Morado Chef", 0xFF8E24AA)
}

data class EstadoPerfil(
    val nombreUsuario: String = "AnThony69x",
    val correoUsuario: String = "anthony@saborforaneo.com",
    val temaOscuro: Boolean = false,
    val notificaciones: Boolean = true,
    val notificacionesActivas: Boolean = false,
    val ubicacionActiva: Boolean = false,
    val esVegetariano: Boolean = false,
    val esVegano: Boolean = false,
    val temaColorSeleccionado: TemaColor = TemaColor.VERDE,

    val recetasVistas: Int = 23,
    val recetasFavoritas: Int = 12,
    val diasRacha: Int = 5,
    val tiempoTotalCocinando: String = "8h 30min",
    val categoriaFavorita: String = "🇪🇨 Ecuatoriana"
)

class PerfilViewModel : ViewModel() {
    private val _estado = MutableStateFlow(EstadoPerfil())
    val estado: StateFlow<EstadoPerfil> = _estado.asStateFlow()

    fun cambiarTemaOscuro(activado: Boolean) {
        _estado.value = _estado.value.copy(temaOscuro = activado)
    }

    fun cambiarNotificaciones(activado: Boolean) {
        _estado.value = _estado.value.copy(notificaciones = activado)
    }

    fun cambiarNotificacionesActivas(activado: Boolean) {
        _estado.value = _estado.value.copy(notificacionesActivas = activado)
    }

    fun cambiarUbicacionActiva(activado: Boolean) {
        _estado.value = _estado.value.copy(ubicacionActiva = activado)
    }

    fun actualizarNombre(nuevoNombre: String) {
        _estado.value = _estado.value.copy(nombreUsuario = nuevoNombre)
    }

    fun actualizarCorreo(nuevoCorreo: String) {
        _estado.value = _estado.value.copy(correoUsuario = nuevoCorreo)
    }

    fun cambiarVegetariano(activado: Boolean) {
        _estado.value = _estado.value.copy(esVegetariano = activado)
    }

    fun cambiarVegano(activado: Boolean) {
        _estado.value = _estado.value.copy(esVegano = activado)
    }

    fun cambiarTemaColor(tema: TemaColor) {
        _estado.value = _estado.value.copy(temaColorSeleccionado = tema)
    }
}