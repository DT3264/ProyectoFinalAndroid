package com.dcerna.proyectofinal.ui.screens

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
    val showMenu = remember { mutableStateOf(false) }
    val dialogAgregar = remember { mutableStateOf(false) }
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
                        DropdownMenuItem(onClick = {
                            navController.navigate(route = "recordatorios/$noteID")
                        }) {
                            Text("Recordatorios")
                        }
                        DropdownMenuItem(onClick = { dialogAgregar.value = true }) {
                            Text("Borrar")
                        }
                    }
                })
        }
    ){
        Column{
            Text(text = "${stringResource(R.string.ID_NOTE)}: $noteID")
            Text(stringResource(R.string.NOTE_DETAILS))
            Button(
                onClick = {
                    navController.navigate(route = "multimedia/$noteID")
                }
            ) {
                Text(stringResource(R.string.TO_MULTIMEDIA), style = TextStyle(fontSize = 30.sp))
            }
            Spacer(modifier = Modifier.padding(all = 16.dp))
            Button(
                onClick = {
                    navController.navigate(route = "recordatorios/$noteID")
                }
            ) {
                Text(stringResource(R.string.TO_REMINDER), style = TextStyle(fontSize = 30.sp))
            }
            Button(
                onClick = {
                    navController.navigate(route = "recordatorios/$noteID")
                }
            ) {
                Text(stringResource(R.string.TO_REMINDER), style = TextStyle(fontSize = 30.sp))
            }
        }
    }
}