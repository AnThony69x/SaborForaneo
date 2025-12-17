package com.example.saborforaneo.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saborforaneo.data.model.Receta
import com.example.saborforaneo.data.repository.RecetasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RecetasUiState(
    val cargando: Boolean = false,
    val recetas: List<Receta> = emptyList(),
    val error: String? = null
)

class RecetasViewModel : ViewModel() {
    
    private val recetasRepository = RecetasRepository()
    
    private val _uiState = MutableStateFlow(RecetasUiState())
    val uiState: StateFlow<RecetasUiState> = _uiState.asStateFlow()
    
    init {
        cargarRecetas()
    }
    
    fun cargarRecetas() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(cargando = true, error = null)
            
            recetasRepository.obtenerTodasLasRecetas().fold(
                onSuccess = { recetas ->
                    _uiState.value = _uiState.value.copy(
                        cargando = false,
                        recetas = recetas
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        cargando = false,
                        error = error.message ?: "Error al cargar recetas"
                    )
                }
            )
        }
    }
    
    fun buscarPorCategoria(categoria: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(cargando = true, error = null)
            
            recetasRepository.buscarPorCategoria(categoria).fold(
                onSuccess = { recetas ->
                    _uiState.value = _uiState.value.copy(
                        cargando = false,
                        recetas = recetas
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        cargando = false,
                        error = error.message ?: "Error en búsqueda"
                    )
                }
            )
        }
    }
}
