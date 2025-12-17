package com.example.saborforaneo.data.repository

import android.util.Log
import com.example.saborforaneo.data.model.Usuario
import com.example.saborforaneo.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepository {
    
    private val supabase = SupabaseClient.client
    
    /**
     * Verifica si hay una sesión activa
     */
    fun estaAutenticado(): Boolean {
        return try {
            supabase.auth.currentSessionOrNull() != null
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error verificando autenticación", e)
            false
        }
    }
    
    /**
     * Obtiene el usuario actual
     */
    suspend fun obtenerUsuarioActual(): Result<Usuario?> {
        return try {
            val currentUser = supabase.auth.currentUserOrNull()
            if (currentUser == null) {
                return Result.success(null)
            }
            
            val authId = currentUser.id
            
            val usuario = supabase.from("usuarios")
                .select {
                    filter {
                        eq("auth_id", authId)
                    }
                }
                .decodeSingle<Usuario>()
            
            Result.success(usuario)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error obteniendo usuario actual", e)
            Result.failure(e)
        }
    }
    
    /**
     * Inicia sesión con email y contraseña
     */
    suspend fun iniciarSesion(email: String, password: String): Result<Unit> {
        return try {
            val emailLimpio = email.trim().lowercase()
            
            supabase.auth.signInWith(Email) {
                this.email = emailLimpio
                this.password = password
            }
            
            Log.d("AuthRepository", "Sesión iniciada exitosamente para: $emailLimpio")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al iniciar sesión: ${e.message}", e)
            
            // Mensajes de error amigables para el usuario
            val errorMsg = when {
                e.message?.contains("Invalid login credentials", ignoreCase = true) == true ->
                    "Usuario o contraseña incorrectos. Verifica tus datos."
                e.message?.contains("Email not confirmed", ignoreCase = true) == true ->
                    "Tu cuenta no está confirmada. Por favor, crea una nueva cuenta."
                e.message?.contains("Invalid email", ignoreCase = true) == true ->
                    "El correo ingresado no es válido."
                e.message?.contains("Too many requests", ignoreCase = true) == true ->
                    "Has intentado varias veces. Espera un momento e intenta de nuevo."
                e.message?.contains("Network", ignoreCase = true) == true ->
                    "No hay conexión a internet. Revisa tu conexión."
                else ->
                    "No pudimos iniciar sesión. Intenta de nuevo."
            }
            
            Result.failure(Exception(errorMsg))
        }
    }
    
    /**
     * Registra un nuevo usuario
     */
    suspend fun registrarUsuario(nombre: String, email: String, password: String): Result<Unit> {
        return try {
            // Validar que el nombre no esté vacío
            if (nombre.isBlank()) {
                throw Exception("El nombre es obligatorio.")
            }
            
            // Limpiar email (NO hacer lowercase para mantener el formato original)
            val emailLimpio = email.trim()
            Log.d("AuthRepository", "Intentando registrar: email='$emailLimpio', nombre='$nombre', passwordLength=${password.length}")
            
            // 1. Crear usuario en Auth sin confirmación de email
            try {
                supabase.auth.signUpWith(Email) {
                    this.email = emailLimpio
                    this.password = password
                    
                    // Datos adicionales - Display Name y metadata
                    data = buildJsonObject {
                        put("display_name", nombre)
                        put("nombre", nombre)
                    }
                }
                Log.d("AuthRepository", "✅ Usuario Auth creado exitosamente")
            } catch (e: Exception) {
                Log.e("AuthRepository", "❌ Error en signUpWith: ${e.message}", e)
                Log.e("AuthRepository", "Tipo de error: ${e.javaClass.simpleName}")
                Log.e("AuthRepository", "Stack trace completo:", e)
                
                // Mensajes simples y directos
                val authError = when {
                    e.message?.contains("already registered", ignoreCase = true) == true ->
                        "Este correo ya está registrado. Intenta iniciar sesión."
                    e.message?.contains("rate limit", ignoreCase = true) == true ->
                        "Espera unos minutos antes de intentar de nuevo."
                    e.message?.contains("password", ignoreCase = true) == true ->
                        "La contraseña debe tener al menos 6 caracteres."
                    else -> {
                        // Mostrar el error real de Supabase
                        Log.e("AuthRepository", "Error completo: ${e.message}")
                        e.message?.take(200) ?: "Error al crear la cuenta"
                    }
                }
                throw Exception(authError)
            }
            
            // 2. Obtener el ID del usuario recién creado
            Log.d("AuthRepository", "Obteniendo usuario recién creado...")
            val currentUser = supabase.auth.currentUserOrNull()
            
            if (currentUser == null) {
                Log.e("AuthRepository", "❌ currentUser es NULL después de signUp")
                throw Exception("El usuario se creó pero no se pudo obtener. Verifica que 'Confirm email' esté DESACTIVADO en Supabase.")
            }
            
            val authId = currentUser.id
            Log.d("AuthRepository", "✅ Usuario Auth creado con ID: $authId")
            Log.d("AuthRepository", "Email del usuario: ${currentUser.email}")
            
            // 3. Determinar rol según el email (comparar en lowercase)
            val role = if (emailLimpio.lowercase() == "admin@saborforaneo.com") {
                "COCINERO"
            } else {
                "USUARIO"
            }
            
            // 4. Crear perfil en tabla usuarios con el nombre
            val nuevoUsuario = Usuario(
                authId = authId,
                email = emailLimpio,
                nombre = nombre.trim(),
                role = role
            )
            
            Log.d("AuthRepository", "Insertando usuario en tabla: nombre='${nuevoUsuario.nombre}', email='${nuevoUsuario.email}', role='$role'")
            
            try {
                supabase.from("usuarios").insert(nuevoUsuario)
                Log.d("AuthRepository", "✅ Usuario insertado en tabla usuarios")
            } catch (e: Exception) {
                Log.e("AuthRepository", "❌ Error al insertar en tabla usuarios: ${e.message}", e)
                
                // Mensajes específicos según el tipo de error
                val errorMsg = when {
                    e.message?.contains("foreign key", ignoreCase = true) == true ->
                        "Cuenta creada pero pendiente de confirmación. Revisa tu email."
                    e.message?.contains("duplicate", ignoreCase = true) == true ->
                        "Este email ya está registrado."
                    e.message?.contains("violates", ignoreCase = true) == true ->
                        "Cuenta creada. Confirma tu email para continuar."
                    else ->
                        "Cuenta creada. Revisa tu correo para confirmarla."
                }
                throw Exception(errorMsg)
            }
            
            Log.d("AuthRepository", "✅ Usuario registrado exitosamente: $nombre como $role")
            Result.success(Unit)
            
        } catch (e: Exception) {
            Log.e("AuthRepository", "❌ Error completo al registrar usuario: ${e.message}", e)
            e.printStackTrace()
            
            // Mensajes claros para el usuario
            val mensajeFinal = when {
                e.message?.contains("Confirma tu email", ignoreCase = true) == true ||
                e.message?.contains("Revisa tu correo", ignoreCase = true) == true ||
                e.message?.contains("confirmación", ignoreCase = true) == true ->
                    e.message ?: "Revisa tu correo para confirmar la cuenta"
                
                e.message?.contains("ya está registrado", ignoreCase = true) == true ->
                    "Este correo ya tiene una cuenta. Intenta iniciar sesión."
                
                else ->
                    e.message?.take(150) ?: "No se pudo crear la cuenta. Intenta de nuevo."
            }
            
            Result.failure(Exception(mensajeFinal))
        }
    }
    
    /**
     * Cierra la sesión actual
     */
    suspend fun cerrarSesion(): Result<Unit> {
        return try {
            supabase.auth.signOut()
            Log.d("AuthRepository", "Sesión cerrada")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al cerrar sesión", e)
            Result.failure(e)
        }
    }
    
    /**
     * Envía email de recuperación de contraseña
     */
    suspend fun recuperarContrasena(email: String): Result<Unit> {
        return try {
            supabase.auth.resetPasswordForEmail(email)
            Log.d("AuthRepository", "Email de recuperación enviado")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al enviar email de recuperación", e)
            Result.failure(e)
        }
    }
}
