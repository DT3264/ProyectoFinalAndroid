package com.dcerna.proyectofinal.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dcerna.proyectofinal.data.Nota
import com.dcerna.proyectofinal.data.NotasBD
import java.util.*

@ExperimentalComposeUiApi
@Composable
fun NotesScreen(navController: NavController) {
    val context = LocalContext.current
    val db = NotasBD.getInstance(context)
    val notas = remember { mutableStateOf(db.DAONotas().getNotas()) }
    val notasInicial = db.DAONotas().getNotas()

    val dialogAgregar = remember { mutableStateOf(false) }
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    if (dialogAgregar.value) {
        MuestraDialogAgregar(dialogAgregar, navController)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { dialogAgregar.value = true }) {
                Icon(Icons.Filled.Add, null)
            }
        },
    ) {

        Column() {
            SearchView(textState, notas, notasInicial)
            LazyColumn() {
                items(notas.value) {
                    GetNota(
                        it.idNota.toString(),
                        "${it.idNota},${it.titulo},${it.descripcion}",
                        navController,
                    )
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun SearchView(
    state: MutableState<TextFieldValue>,
    notas: MutableState<List<Nota>>,
    notasInicial: List<Nota>,
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
            if (value.text != "") {
                notas.value =
                    notasInicial.filter {
                        it.titulo.contains(value.text) || it.nota.contains(value.text)
                    }
            } else {
                notas.value = notasInicial
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value = TextFieldValue("")
                        notas.value = notasInicial
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape,
    )
}

@Composable
fun GetNota(noteID: String, text: String, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable {
                navController.navigate(route = "noteDetails/$noteID")
            },
        elevation = 10.dp
    ) {
        Text(text, style = TextStyle(fontSize = 30.sp))
    }
}

@Composable
fun MuestraDialogAgregar(dialogState: MutableState<Boolean>, navController: NavController) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = {
            dialogState.value = false
        },
        title = {
            Text(text = "Agregar")
        },
        text = {
            Column {
                Text("¿Qué desea agregar?")
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
                        val idNuevaNota = insertaNota(context)
                        dialogState.value = false
                        navController.navigate(route = "noteDetails/$idNuevaNota")

                    }
                ) {
                    Text("Nota")
                }
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val idNuevaNota = insertaNota(context, esTarea = true)
                        dialogState.value = false
                        navController.navigate(route = "noteDetails/$idNuevaNota")
                    }
                ) {
                    Text("Tarea")
                }
            }
        }
    )
}

fun insertaNota(context: Context, esTarea: Boolean = false): Long {
    val tiempoActual = Calendar.getInstance().timeInMillis
    val nota = Nota(esTarea = esTarea, fechaCreacion = tiempoActual)
    val db = NotasBD.getInstance(context)
    db.DAONotas().save(nota)

    // La nota más reciente es la última en la lista
    return db.DAONotas().getNotas().last().idNota
}
