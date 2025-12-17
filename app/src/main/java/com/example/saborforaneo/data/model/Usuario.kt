package com.example.saborforaneo.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    @SerialName("id")
    val id: String = "",
    
    @SerialName("auth_id")
    val authId: String = "",
    
    @SerialName("email")
    val email: String = "",
    
    @SerialName("nombre")
    val nombre: String = "",
    
    @SerialName("role")
    val role: String = "USUARIO", // "USUARIO" o "COCINERO"
    
    @SerialName("telefono")
    val telefono: String? = null,
    
    @SerialName("direccion")
    val direccion: String? = null,
    
    @SerialName("foto_perfil")
    val fotoPerfil: String? = null,
    
    @SerialName("created_at")
    val createdAt: String? = null,
    
    // Campos de compatibilidad
    val imagenUrl: String? = fotoPerfil,
    val preferencias: Preferencias = Preferencias()
)

@Serializable
data class Preferencias(
    val esVegetariano: Boolean = false,
    val esVegano: Boolean = false,
    val restricciones: List<String> = emptyList(),
    val paisesPreferidos: List<String> = emptyList()
)