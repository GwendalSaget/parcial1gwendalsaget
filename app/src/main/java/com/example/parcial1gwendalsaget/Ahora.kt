package com.example.parcial1gwendalsaget

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class AhoraActivity : AppCompatActivity() {

    private lateinit var eventoRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eventoRef = FirebaseDatabase.getInstance().getReference("evento")

        setContent {
            MaterialTheme {
                AhoraScreen(eventoRef)
            }
        }
    }
}

@Composable
fun AhoraScreen(eventoRef: DatabaseReference) {
    var resultadoMensaje by remember { mutableStateOf("Buscando clase...") }

    val horaActual = getCurrentHour()
    val diaActual = getCurrentDayOfWeek()

    LaunchedEffect(Unit) {
        buscarMateria(eventoRef, diaActual, horaActual) { materia ->
            resultadoMensaje = if (materia != null) {
                "Ahora hay clase de $materia"
            } else {
                "No hay clases programadas para esta hora."
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = resultadoMensaje,
            style = MaterialTheme.typography.titleLarge
        )
    }
    val context = LocalContext.current
    Spacer(modifier = Modifier.height(100.dp))
    Button(
        onClick = {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        },
        modifier = Modifier.padding( top = 100.dp)
    ) {
        Text(text = stringResource(R.string.Principal), fontSize = 20.sp)
    }
}

fun getCurrentDayOfWeek(): String {
    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    return when (dayFormat.format(Date()).lowercase(Locale.getDefault())) {
        "monday" -> "Lunes"
        "tuesday" -> "Martes"
        "wednesday" -> "Miercoles"
        "thursday" -> "Jueves"
        "friday" -> "Viernes"
        else -> "No Laboral"
    }
}

fun getCurrentHour(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.HOUR_OF_DAY)
}

fun buscarMateria(
    eventoRef: DatabaseReference,
    dia: String,
    hora: Int,
    onResult: (String?) -> Unit
) {
    eventoRef.orderByChild("dia").equalTo(dia).addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            var materiaEncontrada: String? = null
            for (child in snapshot.children) {
                val evento = child.getValue(Evento::class.java)
                if (evento != null && evento.hora == hora) {
                    materiaEncontrada = evento.nombre
                    break
                }
            }
            onResult(materiaEncontrada)
        }

        override fun onCancelled(error: DatabaseError) {
            onResult(null)
        }
    })
}
