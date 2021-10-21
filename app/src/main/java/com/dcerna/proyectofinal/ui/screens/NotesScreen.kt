package com.dcerna.proyectofinal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NotesScreen(navController: NavController) {
    val itemsList = (0..10).toList()
    val itemsIndexedList = ('A'..'J').toList()
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(itemsList) {
            GetButton("$it", navController)
        }

        item {
            GetButton("2", navController)
        }

        itemsIndexed(itemsIndexedList) { index, item ->
            GetButton("i: $index, V: $item", navController)
        }
    }
}

@Composable
fun GetButton(noteID: String, navController: NavController){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable{
                navController.navigate(route = "noteDetails/$noteID")
            },
        elevation = 10.dp
    ) {
        Text(noteID, style = TextStyle(fontSize = 30.sp))
    }

}