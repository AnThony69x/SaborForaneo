package com.example.saborforaneo.ui.screens.profile.dialogos

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DialogoCambiarContrasena(
    alCerrar: () -> Unit,
    alGuardar: () -> Unit
) {
    var contrasenaActual by remember { mutableStateOf("") }
    var contrasenaNueva by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = alCerrar,
        icon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null
            )
        },
        title = { Text("Cambiar Contraseña") },
        text = {
            Column {
                OutlinedTextField(
                    value = contrasenaActual,
                    onValueChange = {
                        contrasenaActual = it
                        mensajeError = ""
                    },
                    label = { Text("Contraseña actual") },
                    placeholder = { Text("••••••••") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = contrasenaNueva,
                    onValueChange = {
                        contrasenaNueva = it
                        mensajeError = ""
                    },
                    label = { Text("Nueva contraseña") },
                    placeholder = { Text("Mínimo 6 caracteres") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = {
                        confirmarContrasena = it
                        mensajeError = ""
                    },
                    label = { Text("Confirmar contraseña") },
                    placeholder = { Text("Escribe de nuevo") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (mensajeError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = mensajeError,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    when {
                        contrasenaActual.isEmpty() || contrasenaNueva.isEmpty() || confirmarContrasena.isEmpty() -> {
                            mensajeError = "Completa todos los campos"
                        }
                        contrasenaNueva.length < 6 -> {
                            mensajeError = "La contraseña debe tener al menos 6 caracteres"
                        }
                        contrasenaNueva != confirmarContrasena -> {
                            mensajeError = "Las contraseñas no coinciden"
                        }
                        else -> alGuardar()
                    }
                }
            ) {
                Text("Cambiar")
            }
        },
        dismissButton = {
            TextButton(onClick = alCerrar) {
                Text("Cancelar")
            }
        }
    )
}