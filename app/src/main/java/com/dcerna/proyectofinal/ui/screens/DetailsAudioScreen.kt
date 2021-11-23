package com.dcerna.proyectofinal.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.Log.DEBUG
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.dcerna.proyectofinal.data.Multimedia
import com.dcerna.proyectofinal.data.NotasBD
import kotlinx.coroutines.delay
import java.io.File

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun DetailsAudioScreen(multimediaID: String){
    val context = LocalContext.current
    val db = NotasBD.getInstance(context)
    var multimedia = db.DAONotas().getMultimediaPorIDMultimedia(multimediaID)
    var recorder = MediaRecorder()
    var player = MediaPlayer()
    Scaffold(
    ) {
        Surface(color = MaterialTheme.colors.background) {
            DetailsAudio(multimedia, db, recorder, player)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailsAudio(multimedia: Multimedia, db: NotasBD, recorder: MediaRecorder, player: MediaPlayer) {
    val context = LocalContext.current
    val archivo = File(
        context.getExternalFilesDir(null),
        "${multimedia.idMultimedia}.${multimedia.tipo}"
    )
    val archivoExiste = archivo.exists()
    // 1: No hay archivo
    // 2: Grabando archivo
    // 3: Archivo listo para reproducir
    // 4: Archivo reproduciéndose
    val estadoArchivo = remember {
        mutableStateOf(if(!archivoExiste) 1 else 3)
    }

    val launcherPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            graba(recorder, estadoArchivo, archivo)
        }
    }

    val descripcion = remember {
        mutableStateOf(multimedia.descripcion)
    }
    val timeText = remember {
        mutableStateOf("")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 20.dp)){

        // create variable for current time
        var tiempoRestante by remember {
            mutableStateOf(0)
        }
        // create variable for isTimerRunning
        var isTimerRunning by remember {
            mutableStateOf(false)
        }


        LaunchedEffect(key1 = tiempoRestante, key2 = isTimerRunning) {
            if(tiempoRestante > 0 && isTimerRunning) {
                delay(1000L)
                tiempoRestante -= 1
            }
        }
        if(player.isPlaying){
            Text(text = "$tiempoRestante")
        }
        if(estadoArchivo.value == 1)
            Button(onClick = {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) -> {
                        graba(recorder, estadoArchivo, archivo)
                    }
                    else -> {
                        launcherPermiso.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }
            }) {
                Text("Grabar")
            }
        if(estadoArchivo.value == 2)
            Button(onClick = {
                recorder.stop()
                recorder.release()
                estadoArchivo.value = 3

            }) {
                Text("Detener")
            }
        if(estadoArchivo.value == 3)
            Button(onClick = {
                player.setDataSource(archivo.path)
                player.setOnPreparedListener {
                    tiempoRestante = it.duration/1000
                    isTimerRunning = true
                    it.start()
                    estadoArchivo.value = 4
                }
                player.setOnCompletionListener {
                    isTimerRunning = false
                    it.reset()
                    estadoArchivo.value = 3
                }
                player.prepareAsync()
            }) {
                Text(text = "Reproducir archivo")
            }
        if(estadoArchivo.value == 4)
            Button(onClick = {
                player.stop()
                player.reset()
                estadoArchivo.value = 3
            }) {
                Text("Detener")
            }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            value = descripcion.value,
            onValueChange = {
                descripcion.value = it
            }
        )
    }
    multimedia.descripcion = descripcion.value
    db.DAONotas().update(multimedia)
}

@RequiresApi(Build.VERSION_CODES.O)
fun graba(recorder: MediaRecorder, estadoArchivo: MutableState<Int>, archivo: File){
    recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
    recorder.setOutputFile(archivo)
    recorder.prepare()
    recorder.start()
    estadoArchivo.value = 2
}