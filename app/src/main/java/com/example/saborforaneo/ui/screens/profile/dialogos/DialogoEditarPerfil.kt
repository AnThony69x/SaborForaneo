package com.example.saborforaneo.ui.screens.profile.dialogos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DialogoEditarPerfil(
    nombreActual: String,
    correoActual: String,
    alCerrar: () -> Unit,
    alGuardar: (String, String) -> Unit
) {
    var nombre by remember { mutableStateOf(nombreActual) }
    var correo by remember { mutableStateOf(correoActual) }
    var mensajeError by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = alCerrar,
        icon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
        },
        title = { Text("Editar Perfil") },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        mensajeError = ""
                    },
                    label = { Text("Nombre completo") },
                    placeholder = { Text("Tu nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = correo,
                    onValueChange = {
                        correo = it
                        mensajeError = ""
                    },
                    label = { Text("Correo electrónico") },
                    placeholder = { Text("tu@correo.com") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                        nombre.isEmpty() || correo.isEmpty() -> {
                            mensajeError = "Por favor completa todos los campos"
                        }
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                            mensajeError = "Correo electrónico inválido"
                        }
                        else -> alGuardar(nombre, correo)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = alCerrar) {
                Text("Cancelar")
            }
        }
    )
}