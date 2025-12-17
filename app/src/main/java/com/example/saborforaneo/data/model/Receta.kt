package com.example.saborforaneo.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Receta(
    @SerialName("id")
    val id: String = "",
    
    @SerialName("cocinero_id")
    val cocineroId: String? = null,
    
    @SerialName("nombre")
    val nombre: String,
    
    @SerialName("descripcion")
    val descripcion: String,
    
    @SerialName("ingredientes")
    val ingredientes: List<String>,
    
    @SerialName("pasos")
    val pasos: List<String>,
    
    @SerialName("tiempo_preparacion")
    val tiempoPreparacion: Int,
    
    @SerialName("porciones")
    val porciones: Int,
    
    @SerialName("dificultad")
    val dificultadStr: String, // "FACIL", "MEDIA", "DIFICIL"
    
    @SerialName("precio")
    val precioStr: String, // "ECONOMICO", "MODERADO", "COSTOSO"
    
    @SerialName("categoria")
    val categoria: String,
    
    @SerialName("tags")
    val tags: List<String> = emptyList(),
    
    @SerialName("imagenes")
    val imagenes: List<String> = emptyList(),
    
    @SerialName("video_url")
    val videoUrl: String? = null,
    
    @SerialName("cantidad_vistas")
    val cantidadVistas: Int = 0,
    
    @SerialName("calificacion_promedio")
    val calificacionPromedio: Double? = null
) {
    // Campos para compatibilidad con tu código existente
    val imagenUrl: String
        get() = if (imagenes.isNotEmpty()) imagenes[0] else ""
    
    val pais: String = ""
    val esFavorito: Boolean = false
    val esVegetariana: Boolean
        get() = tags.contains("vegetariana")
    val esVegana: Boolean
        get() = tags.contains("vegana")
    
    val dificultad: Dificultad
        get() = when (dificultadStr.uppercase()) {
            "FACIL" -> Dificultad.FACIL
            "MEDIA" -> Dificultad.MEDIA
            "DIFICIL" -> Dificultad.DIFICIL
            else -> Dificultad.MEDIA
        }
    
    val precio: Precio
        get() = when (precioStr.uppercase()) {
            "ECONOMICO" -> Precio.ECONOMICO
            "MODERADO" -> Precio.MODERADO
            "COSTOSO" -> Precio.COSTOSO
            else -> Precio.MODERADO
        }
}

enum class Dificultad {
    FACIL, MEDIA, DIFICIL
}

enum class Precio {
    ECONOMICO, MODERADO, COSTOSO
}