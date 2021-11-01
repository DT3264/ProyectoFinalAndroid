package com.dcerna.proyectofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.*
import com.dcerna.proyectofinal.ui.screens.MultimediaScreen
import com.dcerna.proyectofinal.ui.screens.NoteDetailsScreen
import com.dcerna.proyectofinal.ui.screens.NotesScreen
import com.dcerna.proyectofinal.ui.screens.RecordatoiriosScreen
import com.dcerna.proyectofinal.ui.theme.ProyectoFinalTheme
import com.google.accompanist.navigation.animation.AnimatedComposeNavigator
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
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

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun ComposeNotesApp() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController, startDestination = "lista") {
        mycomposable("lista",
        ) { NotesScreen(navController) }
        mycomposable(
            "noteDetails/{noteID}",
            arguments = listOf(navArgument("noteID") { type = NavType.StringType }),
        )  {
            NoteDetailsScreen(
                navController,
                noteID = it.arguments?.getString("noteID")!!,
            )
        }
        mycomposable(
            "multimedia/{noteID}",
            arguments = listOf(navArgument("noteID") { type = NavType.StringType }),
        ) {
            MultimediaScreen(
                noteID = it.arguments?.getString("noteID")!!,
            )
        }
        mycomposable(
            "recordatorios/{noteID}",
            arguments = listOf(navArgument("noteID") { type = NavType.StringType }),
            ) {
            RecordatoiriosScreen(
                noteID = it.arguments?.getString("noteID")!!,
            )
        }
    }
}


@ExperimentalAnimationApi
public fun NavGraphBuilder.mycomposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    addDestination(
        AnimatedComposeNavigator.Destination(
            provider[AnimatedComposeNavigator::class],
            content,
            enterTransition = { _, _ ->
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )

            },
            exitTransition = { _, _ ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )

            },
            popEnterTransition = { _, _ ->
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )

            },
            popExitTransition = { _, _ ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ).apply {
            this.route = route
            arguments.forEach { (argumentName, argument) ->
                addArgument(argumentName, argument)
            }
        }
    )
}