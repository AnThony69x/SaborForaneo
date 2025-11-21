package com.example.saborforaneo.data.model

data class Usuario(
    val id: String,
    val nombre: String,
    val email: String,
    val imagenUrl: String? = null,
    val preferencias: Preferencias = Preferencias()
)

data class Preferencias(
    val esVegetariano: Boolean = false,
    val esVegano: Boolean = false,
    val restricciones: List<String> = emptyList(),
    val paisesPreferidos: List<String> = emptyList()
)