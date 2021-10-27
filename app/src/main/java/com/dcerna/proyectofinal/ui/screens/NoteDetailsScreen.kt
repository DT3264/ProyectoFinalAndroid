package com.dcerna.proyectofinal.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dcerna.proyectofinal.R
import com.dcerna.proyectofinal.data.NotasBD

@Composable
fun NoteDetailsScreen(navController: NavController, noteID: String){
    Surface(color = MaterialTheme.colors.background) {
        NoteDetails(noteID, navController)
        //    EjemploDialogos()
    }
}

@Composable
fun NoteDetails(noteID: String, navController: NavController) {
    val context = LocalContext.current
    val showMenu = remember { mutableStateOf(false) }
    val dialogAgregar = remember { mutableStateOf(false) }
    val db = NotasBD.getInstance(context)
    if (dialogAgregar.value) {
        MuestraDialogEliminar(dialogAgregar, navController, noteID)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 4.dp,
                title = {
                    Text(stringResource(R.string.NOTE_DETAILS))
                },
                backgroundColor = MaterialTheme.colors.primarySurface,
                actions = {
                    IconButton(onClick = { showMenu.value = !showMenu.value }) {
                        Icon(Icons.Filled.MoreVert, null)
                    }
                    DropdownMenu(expanded = showMenu.value ,
                        onDismissRequest = { showMenu.value = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            navController.navigate(route = "multimedia/$noteID")
                        }) {
                            Text("Multimedia")
                        }
                        val nota = db.DAONotas().getNota(noteID)
                        if(nota!=null){
                            if(nota.esTarea) {
                                DropdownMenuItem(onClick = {
                                    navController.navigate(route = "recordatorios/$noteID")
                                }) {
                                    Text("Recordatorios")
                                }
                            }
                        }
                        DropdownMenuItem(onClick = { dialogAgregar.value = true }) {
                            Text("Borrar")
                        }
                    }
                })
        }
    )
    {
        Column(
            Modifier.padding(8.dp)
        ){
            Text(text = "Titulo: ")
            val nota = db.DAONotas().getNota(noteID)
            var titulo = ""
            var textNote = ""
            if(nota!=null){
                titulo = nota.titulo
                textNote = nota.nota

            }
            val textStateTitle = remember{ mutableStateOf(titulo)}
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = textStateTitle.value,
                onValueChange = {textStateTitle.value = it}
            )
            Text(text = "Nota: ")
            val textStateNota = remember{ mutableStateOf(textNote)}
            TextField(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                value = textStateNota.value,
                onValueChange = {textStateNota.value = it}
            )
            if(nota!=null) {
                nota.titulo = textStateTitle.value
                nota.nota = textStateNota.value
                db.DAONotas().update(nota)
            }

            //if(nota.esTarea) {
            /*
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { datePickerDialog.show() })
            {
                Text("Hora: \nHola a todso")
            }
            //}
            if(nota!=null) {
                nota.titulo = textStateTitle.value
                nota.descripcion = textStateDes.value
                nota.nota = textStateNota.value
                db.DAONotas().update(nota)
            }*/
//            Text(text = "${stringResource(R.string.ID_NOTE)}: $noteID")
//            Text(stringResource(R.string.NOTE_DETAILS))
//            Button(
//                onClick = {
//                    navController.navigate(route = "multimedia/$noteID")
//                }
//            ) {
//                Text(stringResource(R.string.TO_MULTIMEDIA), style = TextStyle(fontSize = 30.sp))
//            }
//            Spacer(modifier = Modifier.padding(all = 16.dp))
//            Button(
//                onClick = {
//                    navController.navigate(route = "recordatorios/$noteID")
//                }
//            ) {
//                Text(stringResource(R.string.TO_REMINDER), style = TextStyle(fontSize = 30.sp))
//            }
//            Button(
//                onClick = {
//                    navController.navigate(route = "recordatorios/$noteID")
//                }
//            ) {
//                Text(stringResource(R.string.TO_REMINDER), style = TextStyle(fontSize = 30.sp))
//            }
        }
    }
}

fun eliminarNota(context: Context, noteID: String, navController: NavController) {
    val db = NotasBD.getInstance(context)
    val nota = db.DAONotas().getNota(noteID)
    //val multi = db.DAONotas().getMultimediasPorIDNota(noteID)

    db.DAONotas().delete(nota)
    //db.DAONotas().delete()
    navController.navigate(route = "lista")
}

@Composable
fun MuestraDialogEliminar(dialogState: MutableState<Boolean>, navController: NavController, noteid: String) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = {
            dialogState.value = false
        },
        title = {
            Text(text = "Eliminar")
        },
        text = {
            Column {
                Text("¿Desea eliminar la nota?")
            }
        },
        buttons = {
            Column(
                modifier = Modifier.padding(all = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        eliminarNota(context, noteid, navController )
                        dialogState.value = false
                    }
                ) {
                    Text("Sí")
                }
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        dialogState.value = false
                    }
                ) {
                    Text("No")
                }
            }
        }
    )
}