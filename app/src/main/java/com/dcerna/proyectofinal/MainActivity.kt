package com.dcerna.proyectofinal

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcerna.proyectofinal.ui.theme.ProyectoFinalTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            ProyectoFinalTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        scaffoldState = scaffoldState,
                    ) {
                        LazyColumnDemo(scaffoldState, scope)
                    }
                }
            }
        }
    }
}

@Composable
fun LazyColumnDemo(scaffoldState: ScaffoldState, scope: CoroutineScope) {
    val itemsList = (0..5).toList()
    val itemsIndexedList = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(itemsList) {
            GetButton("$it", scaffoldState, scope)
        }

        item {
            GetButton("2", scaffoldState, scope)
        }

        itemsIndexed(itemsIndexedList) { index, item ->
            GetButton("i: $index, V: $item", scaffoldState, scope)
        }
    }
}

@Composable
fun GetButton(text: String, scaffoldState: ScaffoldState, scope: CoroutineScope){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable{ scope.launch {
                scaffoldState.snackbarHostState
                    .showSnackbar("Seleccionado $text",duration = SnackbarDuration.Short)
            }},
        elevation = 10.dp
    ) {
        Text(text, style = TextStyle(fontSize = 30.sp))
    }

}