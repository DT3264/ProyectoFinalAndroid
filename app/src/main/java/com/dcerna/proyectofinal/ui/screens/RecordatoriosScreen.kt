package com.dcerna.proyectofinal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dcerna.proyectofinal.R

@Composable
fun RecordatoiriosScreen(navController: NavController, noteID: String){
    Surface(color = MaterialTheme.colors.background) {
        RecordatoriosDetails(noteID)
    }
}

@Composable
fun RecordatoriosDetails(noteID: String) {
    Column{
        Text(text = "${stringResource(R.string.ID_NOTE)}: $noteID")
        Text(stringResource(R.string.REMINDERS))
    }
}