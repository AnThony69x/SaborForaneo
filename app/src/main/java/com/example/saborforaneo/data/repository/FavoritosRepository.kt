package com.example.saborforaneo.data.repository

import android.util.Log
import com.example.saborforaneo.data.model.Favorito
import com.example.saborforaneo.data.model.Receta
import com.example.saborforaneo.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from

class FavoritosRepository {
    
    private val supabase = SupabaseClient.client
    
    /**
     * Obtiene todos los favoritos del usuario actual
     */
    suspend fun obtenerFavoritos(): Result<List<Receta>> {
        return try {
            val usuarioId = supabase.auth.currentUserOrNull()?.id
                ?: return Result.failure(Exception("Usuario no autenticado"))
            
            // Obtener IDs de recetas favoritas
            val favoritos = supabase.from("favoritos")
                .select {
                    filter {
                        eq("usuario_id", usuarioId)
                    }
                }
                .decodeList<Favorito>()
            
            // Obtener las recetas completas
            val recetaIds = favoritos.map { it.recetaId }
            if (recetaIds.isEmpty()) {
                return Result.success(emptyList())
            }
            
            val recetas = supabase.from("recetas")
                .select {
                    filter {
                        isIn("id", recetaIds)
                    }
                }
                .decodeList<Receta>()
            
            Log.d("FavoritosRepository", "Favoritos obtenidos: ${recetas.size}")
            Result.success(recetas)
        } catch (e: Exception) {
            Log.e("FavoritosRepository", "Error obteniendo favoritos", e)
            Result.failure(e)
        }
    }
    
    /**
     * Agrega una receta a favoritos
     */
    suspend fun agregarFavorito(recetaId: String): Result<Unit> {
        return try {
            val usuarioId = supabase.auth.currentUserOrNull()?.id
                ?: return Result.failure(Exception("Usuario no autenticado"))
            
            val favorito = Favorito(
                usuarioId = usuarioId,
                recetaId = recetaId
            )
            
            supabase.from("favoritos").insert(favorito)
            
            Log.d("FavoritosRepository", "Favorito agregado")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FavoritosRepository", "Error agregando favorito", e)
            Result.failure(e)
        }
    }
    
    /**
     * Elimina una receta de favoritos
     */
    suspend fun eliminarFavorito(recetaId: String): Result<Unit> {
        return try {
            val usuarioId = supabase.auth.currentUserOrNull()?.id
                ?: return Result.failure(Exception("Usuario no autenticado"))
            
            supabase.from("favoritos")
                .delete {
                    filter {
                        eq("usuario_id", usuarioId)
                        eq("receta_id", recetaId)
                    }
                }
            
            Log.d("FavoritosRepository", "Favorito eliminado")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FavoritosRepository", "Error eliminando favorito", e)
            Result.failure(e)
        }
    }
    
    /**
     * Verifica si una receta está en favoritos
     */
    suspend fun esFavorito(recetaId: String): Result<Boolean> {
        return try {
            val usuarioId = supabase.auth.currentUserOrNull()?.id
                ?: return Result.success(false)
            
            val favoritos = supabase.from("favoritos")
                .select {
                    filter {
                        eq("usuario_id", usuarioId)
                        eq("receta_id", recetaId)
                    }
                }
                .decodeList<Favorito>()
            
            Result.success(favoritos.isNotEmpty())
        } catch (e: Exception) {
            Log.e("FavoritosRepository", "Error verificando favorito", e)
            Result.failure(e)
        }
    }
}
