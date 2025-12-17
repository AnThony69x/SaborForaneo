package com.example.saborforaneo

import android.app.Application
import android.util.Log
import com.example.saborforaneo.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaborForaneoApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        Log.d("SaborForaneo", "🚀 Inicializando aplicación...")
        Log.d("SaborForaneo", "📡 SUPABASE_URL: ${BuildConfig.SUPABASE_URL}")
        Log.d("SaborForaneo", "🔑 SUPABASE_KEY: ${BuildConfig.SUPABASE_ANON_KEY.take(20)}...")
        
        // Test de conexión a Supabase
        testConexionSupabase()
    }
    
    private fun testConexionSupabase() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("SaborForaneo", "🔌 Probando conexión a Supabase...")
                
                // Intentar hacer una petición simple a la tabla recetas
                val client = SupabaseClient.client
                val response = client.from("recetas")
                    .select {
                        limit(1.toLong())
                    }
                
                Log.d("SaborForaneo", "✅ Conexión exitosa con Supabase!")
                Log.d("SaborForaneo", "📊 Base de datos respondiendo correctamente")
            } catch (e: Exception) {
                Log.e("SaborForaneo", "❌ Error conectando a Supabase: ${e.message}", e)
                Log.e("SaborForaneo", "💡 Verifica las credenciales en local.properties")
            }
        }
    }
}
