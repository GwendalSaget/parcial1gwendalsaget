package com.example.parcial1gwendalsaget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.google.firebase.database.*

data class Evento(
    val id: String? = null,
    val nombre: String,
    val hora: Int,
    val dia: String,
) {
    constructor() : this(id = null, nombre = "", hora = 0, dia = "")
}

class EventoFragment : Fragment() {
    private lateinit var eventoRef: DatabaseReference
    private var eventoList by mutableStateOf(listOf<Evento>())
    private var dia by mutableStateOf("Lunes")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        eventoRef = FirebaseDatabase.getInstance().getReference("evento")

        eventoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val eventos = mutableListOf<Evento>()
                //eventoList = emptyList()
                snapshot.children.forEach {
                    val evento = it.getValue(Evento::class.java)?.copy(id = it.key)
                    if (evento != null ){
                        eventos.add(evento)
                    }
                }
                eventoList = eventos
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        /*DiaSpinner(
                            opciones = listOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes"),
                            selectedOption = dia,
                            onOptionSelected = { newDia ->
                                dia = newDia

                            }
                        )*/

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .fillMaxWidth()
                                .padding(top = 100.dp)
                        ) {
                            items(eventoList) { evento ->
                                EventoItem(
                                    evento = evento,
                                    onDelete = {
                                        evento.id?.let { eventoRef.child(it).removeValue() }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DiaSpinner(opciones: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = selectedOption)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(opcion)
                        expanded = false
                    },
                    text = opcion
                )
            }
        }
    }
}


@Composable
fun EventoItem(evento: Evento, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Nombre : ${evento.nombre}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Hora : ${evento.hora}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Dia : ${evento.dia}", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Borrar evento")
            }
        }
    }
}
