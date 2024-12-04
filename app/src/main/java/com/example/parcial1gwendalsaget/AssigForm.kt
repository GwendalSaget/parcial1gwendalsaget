package com.example.parcial1gwendalsaget

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DatabaseReference

@Composable
fun EventoForm(eventoRef: DatabaseReference) {

    var nombre by remember { mutableStateOf("") }
    var horatext by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var hora = 0
    val opciones = listOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(opciones[0]) }

    Column(modifier = Modifier.padding(16.dp)) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            OutlinedButton(
                onClick = { expanded = true }
            ) {
                Text(text = selectedOption)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOption = opcion
                            expanded = false
                            dia = opcion
                        },
                        text = opcion
                    )
                }
            }
        }
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = horatext,
            onValueChange = {
                if (it.length <= 2) {
                    horatext = it.filter { int -> int.isDigit() }
                    hora = horatext.toIntOrNull() ?: 0
                }
            },
            label = { Text("Hora") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        )

        Button(
            onClick = {
                if (nombre.isNotEmpty() && horatext.isNotEmpty()) {
                    val eventoId = eventoRef.push().key ?: ""
                    val newEvento = Evento(eventoId, nombre, hora, dia)

                    eventoRef.child(eventoId).setValue(newEvento)
                        .addOnSuccessListener {
                            message = "¡Evento añadido exitosamente!"
                        }
                        .addOnFailureListener { exception ->
                            message = "Error al añadir evento: ${exception.message}"
                        }

                    nombre = ""
                    horatext = ""
                } else {
                    message = "Los campos: Nombre y Hora son obligatorios."
                }
            },
            modifier = Modifier.padding(top = 16.dp).align(Alignment.End)
        ) {
            Text("Añadir")
        }
        if (message.isNotEmpty()) {
            Text(message, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun DropdownMenuItem(onClick: () -> Unit, text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clickable(onClick = onClick)
            .border(1.dp, MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = text, style = MaterialTheme.typography.titleMedium)
        }
    }
}
