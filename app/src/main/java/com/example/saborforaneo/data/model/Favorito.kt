package com.example.saborforaneo.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Favorito(
    @SerialName("id")
    val id: String = "",
    
    @SerialName("usuario_id")
    val usuarioId: String,
    
    @SerialName("receta_id")
    val recetaId: String,
    
    @SerialName("created_at")
    val createdAt: String? = null
)
