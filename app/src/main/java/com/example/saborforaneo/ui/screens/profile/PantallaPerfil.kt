package com.example.saborforaneo.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.saborforaneo.ui.components.BarraNavegacionInferior
import com.example.saborforaneo.ui.screens.profile.componentes.*
import com.example.saborforaneo.ui.screens.profile.dialogos.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(
    navegarALogin: () -> Unit,
    controladorNav: NavController,
    modeloVista: PerfilViewModel = viewModel()
) {
    val estado by modeloVista.estado.collectAsState()
    val estadoSnackbar = remember { SnackbarHostState() }
    val alcance = rememberCoroutineScope()

    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }
    var mostrarDialogoEditarPerfil by remember { mutableStateOf(false) }
    var mostrarDialogoCambiarContrasena by remember { mutableStateOf(false) }
    var mostrarDialogoRestricciones by remember { mutableStateOf(false) }
    var mostrarDialogoAcercaDe by remember { mutableStateOf(false) }
    var mostrarDialogoTerminos by remember { mutableStateOf(false) }
    var mostrarDialogoPrivacidad by remember { mutableStateOf(false) }
    var mostrarDialogoSelectorTema by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Perfil",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        bottomBar = {
            BarraNavegacionInferior(controladorNav = controladorNav)
        },
        snackbarHost = {
            SnackbarHost(hostState = estadoSnackbar)
        }
    ) { valoresPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(valoresPadding),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // ========== HEADER DEL PERFIL ==========
            item {
                SeccionPerfil(
                    nombreUsuario = estado.nombreUsuario,
                    correoUsuario = estado.correoUsuario,
                    alEditarPerfil = { mostrarDialogoEditarPerfil = true }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ========== PREFERENCIAS ==========
            item {
                SeccionPreferencias(
                    temaOscuro = estado.temaOscuro,
                    temaColor = estado.temaColorSeleccionado,
                    alCambiarTema = { activado ->
                        modeloVista.cambiarTemaOscuro(activado)
                        alcance.launch {
                            estadoSnackbar.showSnackbar(
                                message = if (activado) "🌙 Tema oscuro activado" else "☀️ Tema claro activado",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    alAbrirSelectorTema = { mostrarDialogoSelectorTema = true }
                )
            }

            item { DivisorSeccion() }

            // ========== PERMISOS ==========
            item {
                Text(
                    text = "Permisos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            item {
                SeccionPermisosNotificaciones(
                    notificacionesActivas = estado.notificacionesActivas,
                    onCambiarNotificaciones = { activado ->
                        modeloVista.cambiarNotificacionesActivas(activado)
                        alcance.launch {
                            estadoSnackbar.showSnackbar(
                                message = if (activado) "🔔 Notificaciones activadas" else "🔕 Notificaciones desactivadas",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                )
            }

            item {
                SeccionPermisosUbicacion(
                    ubicacionActiva = estado.ubicacionActiva,
                    onCambiarUbicacion = { activado ->
                        modeloVista.cambiarUbicacionActiva(activado)
                        alcance.launch {
                            estadoSnackbar.showSnackbar(
                                message = if (activado) "📍 Ubicación activada" else "📍 Ubicación desactivada",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                )
            }

            item { DivisorSeccion() }

            // ========== CUENTA ==========
            item {
                SeccionCuenta(
                    descripcionRestricciones = when {
                        estado.esVegano -> "Vegano"
                        estado.esVegetariano -> "Vegetariano"
                        else -> "Sin restricciones"
                    },
                    alAbrirRestricciones = { mostrarDialogoRestricciones = true },
                    alCambiarContrasena = { mostrarDialogoCambiarContrasena = true }
                )
            }

            item { DivisorSeccion() }

            // ========== INFORMACIÓN ==========
            item {
                SeccionInformacion(
                    alAbrirAcercaDe = { mostrarDialogoAcercaDe = true },
                    alAbrirTerminos = { mostrarDialogoTerminos = true },
                    alAbrirPrivacidad = { mostrarDialogoPrivacidad = true }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ========== BOTÓN CERRAR SESIÓN ==========
            item {
                Button(
                    onClick = { mostrarDialogoCerrarSesion = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cerrar Sesión",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // ========================================
    // DIÁLOGOS
    // ========================================

    if (mostrarDialogoCerrarSesion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCerrarSesion = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null
                )
            },
            title = { Text(text = "Cerrar Sesión") },
            text = { Text(text = "¿Estás seguro que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoCerrarSesion = false
                        navegarALogin()
                    }
                ) {
                    Text("Sí, cerrar sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoCerrarSesion = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (mostrarDialogoEditarPerfil) {
        DialogoEditarPerfil(
            nombreActual = estado.nombreUsuario,
            correoActual = estado.correoUsuario,
            alCerrar = { mostrarDialogoEditarPerfil = false },
            alGuardar = { nuevoNombre, nuevoCorreo ->
                modeloVista.actualizarNombre(nuevoNombre)
                modeloVista.actualizarCorreo(nuevoCorreo)
                mostrarDialogoEditarPerfil = false
                alcance.launch {
                    estadoSnackbar.showSnackbar(
                        message = "✅ Perfil actualizado correctamente",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        )
    }

    if (mostrarDialogoCambiarContrasena) {
        DialogoCambiarContrasena(
            alCerrar = { mostrarDialogoCambiarContrasena = false },
            alGuardar = {
                mostrarDialogoCambiarContrasena = false
                alcance.launch {
                    estadoSnackbar.showSnackbar(
                        message = "✅ Contraseña cambiada correctamente",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        )
    }

    if (mostrarDialogoRestricciones) {
        DialogoRestricciones(
            esVegetariano = estado.esVegetariano,
            esVegano = estado.esVegano,
            alCerrar = { mostrarDialogoRestricciones = false },
            alGuardar = { vegetariano, vegano ->
                modeloVista.cambiarVegetariano(vegetariano)
                modeloVista.cambiarVegano(vegano)
                mostrarDialogoRestricciones = false
                alcance.launch {
                    estadoSnackbar.showSnackbar(
                        message = "✅ Preferencias alimentarias actualizadas",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        )
    }

    if (mostrarDialogoAcercaDe) {
        DialogoAcercaDe(alCerrar = { mostrarDialogoAcercaDe = false })
    }

    if (mostrarDialogoTerminos) {
        DialogoTerminos(alCerrar = { mostrarDialogoTerminos = false })
    }

    if (mostrarDialogoPrivacidad) {
        DialogoPrivacidad(alCerrar = { mostrarDialogoPrivacidad = false })
    }

    if (mostrarDialogoSelectorTema) {
        DialogoSelectorTema(
            temaActual = estado.temaColorSeleccionado,
            alCerrar = { mostrarDialogoSelectorTema = false },
            alSeleccionar = { nuevoTema ->
                modeloVista.cambiarTemaColor(nuevoTema)
                alcance.launch {
                    estadoSnackbar.showSnackbar(
                        message = "🎨 Tema ${nuevoTema.nombreMostrar} aplicado",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        )
    }
}