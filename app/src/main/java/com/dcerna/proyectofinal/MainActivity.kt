package com.dcerna.proyectofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dcerna.proyectofinal.ui.screens.MultimediaScreen
import com.dcerna.proyectofinal.ui.screens.NoteDetailsScreen
import com.dcerna.proyectofinal.ui.screens.NotesScreen
import com.dcerna.proyectofinal.ui.screens.RecordatoiriosScreen
import com.dcerna.proyectofinal.ui.theme.ProyectoFinalTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : ComponentActivity() {
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

@ExperimentalAnimationApi
@Composable
fun ComposeNotesApp() {
    val navController = rememberAnimatedNavController()
    val tweenSpec = tween<IntOffset>(
        durationMillis = 2000,
        easing = CubicBezierEasing(0.08f, 0.93f, 0.68f, 1.27f)
    )
    AnimatedNavHost(navController, startDestination = "lista") {
        composable("lista",
            enterTransition = { initial, _ ->
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )

            },
            exitTransition = { _, target ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )

            },
            popEnterTransition = { initial, _ ->
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )

            },
            popExitTransition = { _, target ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )

            }
        ) { NotesScreen(navController) }
        composable(
            "noteDetails/{noteID}",
            arguments = listOf(navArgument("noteID") { type = NavType.StringType }),
            enterTransition = { initial, _ ->
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )

            },
            exitTransition = { _, target ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )

            },
            popEnterTransition = { initial, _ ->
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )

            },
            popExitTransition = { _, target ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )

            }
        )  {
            NoteDetailsScreen(
                navController,
                noteID = it.arguments?.getString("noteID")!!,
            )
        }
        composable(
            "multimedia/{noteID}",
            arguments = listOf(navArgument("noteID") { type = NavType.StringType }),
            enterTransition = { initial, _ ->
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )

            },
            exitTransition = { _, target ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )

            },
            popEnterTransition = { initial, _ ->
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )

            },
            popExitTransition = { _, target ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )

            }
        ) {
            MultimediaScreen(
                noteID = it.arguments?.getString("noteID")!!,
            )
        }
        composable(
            "recordatorios/{noteID}",
            arguments = listOf(navArgument("noteID") { type = NavType.StringType }),
            enterTransition = { initial, _ ->
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )

            },
            exitTransition = { _, target ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )

            },
            popEnterTransition = { initial, _ ->
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )

            },
            popExitTransition = { _, target ->
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )

            }
        ) {
            RecordatoiriosScreen(
                noteID = it.arguments?.getString("noteID")!!,
            )
        }
    }
}

