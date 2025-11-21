package com.example.saborforaneo.ui.screens.profile.dialogos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DialogoRestricciones(
    esVegetariano: Boolean,
    esVegano: Boolean,
    alCerrar: () -> Unit,
    alGuardar: (Boolean, Boolean) -> Unit
) {
    var vegetariano by remember { mutableStateOf(esVegetariano) }
    var vegano by remember { mutableStateOf(esVegano) }

    AlertDialog(
        onDismissRequest = alCerrar,
        icon = {
            Icon(
                imageVector = Icons.Default.Restaurant,
                contentDescription = null
            )
        },
        title = { Text("Restricciones Alimentarias") },
        text = {
            Column {
                Text(
                    text = "Selecciona tus preferencias dietéticas:",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Surface(
                    onClick = { vegetariano = !vegetariano },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = if (vegetariano)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = vegetariano,
                            onCheckedChange = { vegetariano = it }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("🥗 Vegetariano", fontWeight = FontWeight.Bold)
                            Text(
                                "Sin carne ni pescado",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    onClick = { vegano = !vegano },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = if (vegano)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = vegano,
                            onCheckedChange = { vegano = it }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("🌱 Vegano", fontWeight = FontWeight.Bold)
                            Text(
                                "Sin productos de origen animal",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { alGuardar(vegetariano, vegano) }) {
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