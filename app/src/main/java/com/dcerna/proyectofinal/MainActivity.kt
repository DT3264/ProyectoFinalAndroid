package com.dcerna.proyectofinal

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dcerna.proyectofinal.ui.screens.MultimediaScreen
import com.dcerna.proyectofinal.ui.screens.NoteDetailsScreen
import com.dcerna.proyectofinal.ui.screens.NotesScreen
import com.dcerna.proyectofinal.ui.screens.RecordatoiriosScreen
import com.dcerna.proyectofinal.ui.theme.ProyectoFinalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoFinalTheme {
                // A surface container using the 'background' color from the theme
               ComposeNotesApp()
            }
        }
    }
}

@Composable
fun ComposeNotesApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "lista") {
        composable("lista") { NotesScreen(navController) }
        composable(
            "noteDetails/{noteID}",
            arguments = listOf(navArgument("noteID") { type = NavType.StringType })
        ) {
            NoteDetailsScreen(
                navController,
                noteID = it.arguments?.getString("noteID")!!,
            )
        }
        composable(
            "multimedia/{noteID}",
            arguments = listOf(navArgument("noteID") { type = NavType.StringType })
        ) {
            MultimediaScreen(
                navController,
                noteID = it.arguments?.getString("noteID")!!,
            )
        }
        composable(
            "recordatorios/{noteID}",
            arguments = listOf(navArgument("noteID") { type = NavType.StringType })
        ) {
            RecordatoiriosScreen(
                navController,
                noteID = it.arguments?.getString("noteID")!!,
            )
        }
    }
}

