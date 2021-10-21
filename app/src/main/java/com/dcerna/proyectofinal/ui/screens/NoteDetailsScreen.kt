package com.dcerna.proyectofinal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun NoteDetailsScreen(navController: NavController, noteID: String){
    Surface(color = MaterialTheme.colors.background) {
        NoteDetails(noteID, navController)
    }
}

@Composable
fun NoteDetails(noteID: String, navController: NavController) {
    Column{
        Text("ID nota: $noteID. Detalles Nota")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable{
                    navController.navigate(route = "multimedia/$noteID")
                },
            elevation = 10.dp
        ) {
            Text("A multimedia", style = TextStyle(fontSize = 30.sp))
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable{
                    navController.navigate(route = "recordatorios/$noteID")
                },
            elevation = 10.dp
        ) {
            Text("A recordatorios", style = TextStyle(fontSize = 30.sp))
        }
    }
}