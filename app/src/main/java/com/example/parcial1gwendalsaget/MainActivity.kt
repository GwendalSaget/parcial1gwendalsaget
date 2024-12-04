package com.example.parcial1gwendalsaget

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcial1gwendalsaget.ui.theme.Parcial1gwendalsagetTheme
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var eventoRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventoRef = FirebaseDatabase.getInstance().getReference("evento")

        setContent {
            Parcial1gwendalsagetTheme {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {
                    Text("Gestion de Eventos", fontSize = 30.sp, modifier = Modifier.padding(bottom = 16.dp))
                    val context = LocalContext.current
                    Button(
                        onClick = {
                            val intent = Intent(context, FormActivity::class.java)
                            context.startActivity(intent)
                        }
                    ) {
                        Text(text = stringResource(R.string.Principal2), fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            val intent = Intent(context, Horario::class.java)
                            context.startActivity(intent)
                        }
                    ) {
                        Text(text = stringResource(R.string.Principal3), fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            val intent = Intent(context, AhoraActivity::class.java)
                            context.startActivity(intent)
                        }
                    ) {
                        Text(text = stringResource(R.string.Principal4), fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}