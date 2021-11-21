package com.dcerna.proyectofinal.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.dcerna.proyectofinal.R
@Composable
fun DetailsAudioScreen(multimediaID: String){
    Scaffold(
    ) {
        Surface(color = MaterialTheme.colors.background) {
            DetailsAudio(multimediaID)
        }
    }
}

@Composable
fun DetailsAudio(multimediaID: String) {
    Column {
        Text(text = "${stringResource(R.string.ID_NOTE)}: $multimediaID")
        Text(stringResource(R.string.MULTIMEDIA))
    }
}