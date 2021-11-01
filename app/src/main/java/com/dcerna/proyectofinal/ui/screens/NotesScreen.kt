package com.dcerna.proyectofinal.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dcerna.proyectofinal.R
import com.dcerna.proyectofinal.data.Nota
import com.dcerna.proyectofinal.data.NotasBD
import com.dcerna.proyectofinal.util.getDate
import java.util.*

@ExperimentalComposeUiApi
@Composable
fun NotesScreen(navController: NavController) {

    val context = LocalContext.current
    val db = NotasBD.getInstance(context)

    val dialogAgregar = remember { mutableStateOf(false) }
    val query = rememberSaveable { mutableStateOf("")}
    val notasActual = db.DAONotas().getLiveNotas().observeAsState()

    val comparadorNotas = Comparator { n1: Nota, n2: Nota ->
        var toReturn = 0
        if (n1.esTarea && n2.esTarea) if (n1.fechaLimite < n2.fechaLimite) toReturn =
            -1 else toReturn = 1
        else if (!n1.esTarea && !n2.esTarea) if (n1.fechaCreacion < n2.fechaCreacion) toReturn =
            1 else toReturn = -1
        else if (!n1.esTarea) toReturn = 1 else toReturn = -1
        toReturn
    }

    var notas: List<Nota>? = notasActual.value?.sortedWith(comparadorNotas)
    if(query.value!=""){
        notas = notas?.filter {
            it.titulo.lowercase(Locale.getDefault())
                .contains(query.value.lowercase(Locale.getDefault()))
                    || it.nota.lowercase(Locale.getDefault())
                .contains(query.value.lowercase(Locale.getDefault()))
        }
    }


    if (dialogAgregar.value) {
        MuestraDialogAgregar(dialogAgregar, navController)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 4.dp,
                title = {
                    Text(stringResource(R.string.NOTES))
                },
                backgroundColor =  MaterialTheme.colors.primarySurface,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { dialogAgregar.value = true }) {
                Icon(Icons.Filled.Add, null)
            }
        },
    ) {

        Column {
            SearchView(query)
            Column() {
                notas?.forEach { n -> GetNota(n, navController, context) }
            }
        }
    }
}


@ExperimentalComposeUiApi
@Composable
fun SearchView(
    query: MutableState<String>,
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = query.value,
        onValueChange = { value ->
            query.value = value
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
            if (query.value != "") {
                IconButton(
                    onClick = {
                        query.value = ""
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
fun MuestraDialogAgregar(dialogState: MutableState<Boolean>, navController: NavController) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = {
            dialogState.value = false
        },
        title = {
            Text(text = stringResource(R.string.ADD_NOTE))
        },
        text = {
            Column {
                Text(stringResource(R.string.ADD_NOTE_QUESTION))
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
                    Text(stringResource(R.string.NOTE))
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
                    Text(stringResource(R.string.REMINDER))
                }
            }
        }
    )
}

@Composable
fun GetNota(
    nota: Nota,
    navController: NavController,
    context: Context
) {
    val estaCompletada = remember { mutableStateOf(nota.estaCompletada) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                navController.navigate(route = "noteDetails/${nota.idNota}")
            },
        elevation = 10.dp
    ) {
        val fontSize = 24
        val fechaAUsar = if (nota.esTarea) nota.fechaLimite else nota.fechaCreacion
        val fecha = getDate(fechaAUsar, "dd/MM/yyyy")
        val hora = getDate(fechaAUsar, "hh:mm:ss")
        val textoDeFecha = if(nota.esTarea) stringResource(R.string.TASK) else stringResource(R.string.NOTE)
        Column {
            Text(
                "$textoDeFecha $fecha $hora",
                modifier = Modifier.padding(start = 15.dp, end = 15.dp),
                style = TextStyle(fontSize = fontSize.sp),
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (nota.esTarea) {
                    Checkbox(
                        modifier = Modifier.padding(start = 15.dp, end = 15.dp),
                        checked = estaCompletada.value,
                        onCheckedChange = {
                            actualizaNota(nota, it, context)
                            estaCompletada.value = it
                        })
                }
                if (nota.titulo.isNotBlank())
                    Text(
                        nota.titulo,
                        modifier = Modifier.padding(
                            start = if (!nota.esTarea) 15.dp else 0.dp,
                            end = if (!nota.esTarea) 15.dp else 0.dp
                        ),
                        style = TextStyle(fontSize = fontSize.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
            }
            if (nota.nota.isNotBlank())
                Text(
                    nota.nota,
                    modifier = Modifier.padding(start = 15.dp, end = 15.dp),
                    style = TextStyle(fontSize = fontSize.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
        }
    }
}

fun actualizaNota(nota: Nota, estado: Boolean, context: Context) {
    val db = NotasBD.getInstance(context)
    nota.estaCompletada = estado
    db.DAONotas().update(nota)
}

fun insertaNota(context: Context, esTarea: Boolean = false): Long {
    val tiempoActual = Calendar.getInstance().timeInMillis
    val nota = Nota(esTarea = esTarea, fechaCreacion = tiempoActual)
    val db = NotasBD.getInstance(context)
    db.DAONotas().save(nota)

    // La nota más reciente es la última en la lista
    return db.DAONotas().getNotas().last().idNota
}
