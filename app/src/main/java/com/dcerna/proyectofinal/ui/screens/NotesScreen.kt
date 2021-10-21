package com.dcerna.proyectofinal.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dcerna.proyectofinal.data.Multimedia
import com.dcerna.proyectofinal.data.Nota
import com.dcerna.proyectofinal.data.NotasBD
import com.dcerna.proyectofinal.data.Recordatorio

@Composable
fun NotesScreen(navController: NavController) {
    var context = LocalContext.current
    var db = NotasBD.getInstance(context)
    var notas = db.DAONotas().getNotas()
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        item {
            GetButton(noteID = "a", text = "", navController = navController)
        }
        items(notas) {
            GetButton(
                it.idNota.toString(),
                "${it.idNota},${it.titulo},${it.descripcion}",
                navController
            )
        }
    }
}

@Composable
fun GetButton(noteID: String, text: String, navController: NavController) {
    var context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable {
                addNote(context)
                navController.navigate(route = "noteDetails/$noteID")
            },
        elevation = 10.dp
    ) {
        Text(text, style = TextStyle(fontSize = 30.sp))
    }
}

fun addNote(context: Context) {
    var db = NotasBD.getInstance(context)
    db.DAONotas().save(
        Nota(
            descripcion = "Test",
            fechaCreacion = 0,
            titulo = "Titulo prueba",
            nota = "Esta es una nota"
        )
    )
    var notas = db.DAONotas().getNotas()
    var lastNote = notas.last()
    var recordatorio = Recordatorio(
        idNota = lastNote.idNota,
        fechaRecordatorio = 10
    )
    println("Agregando recordatorio")
    println("${recordatorio.idNota},${recordatorio.idRecordatorio},${recordatorio.fechaRecordatorio}")
    db.DAONotas().save(
        recordatorio
    )
    var multimedia =
        Multimedia(
            idNota = lastNote.idNota,
            descripcion = "Mel",
            ruta = "sdcard/",
            tipo = "mp3"
        )
    println("Agregando multimedia")
    println("${multimedia.idNota},${multimedia.idMultimedia},${multimedia.ruta},${multimedia.tipo},${multimedia.descripcion}")
    db.DAONotas().save(multimedia)
}
