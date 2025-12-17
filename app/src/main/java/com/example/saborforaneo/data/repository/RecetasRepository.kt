package com.example.saborforaneo.data.repository

import android.util.Log
import com.example.saborforaneo.data.model.Receta
import com.example.saborforaneo.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class RecetasRepository {
    
    private val supabase = SupabaseClient.client
    
    /**
     * Obtiene todas las recetas
     */
    suspend fun obtenerTodasLasRecetas(): Result<List<Receta>> {
        return try {
            val recetas = supabase.from("recetas")
                .select()
                .decodeList<Receta>()
            
            Log.d("RecetasRepository", "Recetas obtenidas: ${recetas.size}")
            Result.success(recetas)
        } catch (e: Exception) {
            Log.e("RecetasRepository", "Error obteniendo recetas", e)
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene una receta por ID
     */
    suspend fun obtenerRecetaPorId(id: String): Result<Receta> {
        return try {
            val receta = supabase.from("recetas")
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingle<Receta>()
            
            Log.d("RecetasRepository", "Receta obtenida: ${receta.nombre}")
            Result.success(receta)
        } catch (e: Exception) {
            Log.e("RecetasRepository", "Error obteniendo receta", e)
            Result.failure(e)
        }
    }
    
    /**
     * Busca recetas por categoría
     */
    suspend fun buscarPorCategoria(categoria: String): Result<List<Receta>> {
        return try {
            val recetas = supabase.from("recetas")
                .select {
                    filter {
                        eq("categoria", categoria)
                    }
                }
                .decodeList<Receta>()
            
            Log.d("RecetasRepository", "Recetas por categoría: ${recetas.size}")
            Result.success(recetas)
        } catch (e: Exception) {
            Log.e("RecetasRepository", "Error buscando por categoría", e)
            Result.failure(e)
        }
    }
    
    /**
     * Busca recetas por texto
     */
    suspend fun buscarRecetas(query: String): Result<List<Receta>> {
        return try {
            val recetas = supabase.from("recetas")
                .select {
                    filter {
                        or {
                            ilike("nombre", "%$query%")
                            ilike("descripcion", "%$query%")
                        }
                    }
                }
                .decodeList<Receta>()
            
            Log.d("RecetasRepository", "Resultados búsqueda: ${recetas.size}")
            Result.success(recetas)
        } catch (e: Exception) {
            Log.e("RecetasRepository", "Error en búsqueda", e)
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene recetas populares (más vistas)
     */
    suspend fun obtenerRecetasPopulares(limite: Int = 10): Result<List<Receta>> {
        return try {
            val recetas = supabase.from("recetas")
                .select {
                    order(column = "cantidad_vistas", order = io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                    limit(limite.toLong())
                }
                .decodeList<Receta>()
            
            Log.d("RecetasRepository", "Recetas populares: ${recetas.size}")
            Result.success(recetas)
        } catch (e: Exception) {
            Log.e("RecetasRepository", "Error obteniendo populares", e)
            Result.failure(e)
        }
    }
    
    /**
     * Incrementa el contador de vistas de una receta
     */
    suspend fun incrementarVistas(recetaId: String): Result<Unit> {
        return try {
            supabase.from("recetas")
                .update({
                    set("cantidad_vistas", Columns.raw("cantidad_vistas + 1"))
                }) {
                    filter {
                        eq("id", recetaId)
                    }
                }
            
            Log.d("RecetasRepository", "Vistas incrementadas")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("RecetasRepository", "Error incrementando vistas", e)
            Result.failure(e)
        }
    }
    
    /**
     * Crea una nueva receta (solo para cocineros)
     */
    suspend fun crearReceta(receta: Receta): Result<Receta> {
        return try {
            val nuevaReceta = supabase.from("recetas")
                .insert(receta)
                .decodeSingle<Receta>()
            
            Log.d("RecetasRepository", "Receta creada: ${nuevaReceta.nombre}")
            Result.success(nuevaReceta)
        } catch (e: Exception) {
            Log.e("RecetasRepository", "Error creando receta", e)
            Result.failure(e)
        }
    }
}
