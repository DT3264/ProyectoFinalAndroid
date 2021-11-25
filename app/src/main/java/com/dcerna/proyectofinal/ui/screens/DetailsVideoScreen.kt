package com.dcerna.proyectofinal.ui.screens

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.widget.VideoView
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import com.dcerna.proyectofinal.data.Multimedia
import com.dcerna.proyectofinal.data.NotasBD
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun DetailsVideoScreen(multimediaID: String){
    val context = LocalContext.current
    val db = NotasBD.getInstance(context)
    var multimedia = db.DAONotas().getMultimediaPorIDMultimedia(multimediaID)
    Scaffold(
    ) {
        Surface(color = MaterialTheme.colors.background) {
            DetailsVideo(multimedia, db)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun DetailsVideo(multimedia: Multimedia, db: NotasBD) {
    val context = LocalContext.current
    val archivo = File(
        context.getExternalFilesDir(null),
        "${multimedia.idMultimedia}.${multimedia.tipo}"
    )
    val archivoExiste = archivo.exists()

    val videoGrabado = remember {
        mutableStateOf(archivoExiste)
    }


    var uriVideo = FileProvider.getUriForFile(
        context,
        "com.dcerna.proyectofinal",
        archivo
    )

    val launcherExplorador = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        // Se guardó el contenido de la imagen en la uri especificada
        if(it != null) {
            val contentResolver: ContentResolver = context.contentResolver
            copiaImagen(it, archivo.absolutePath, contentResolver)
            videoGrabado.value = true
        }
    }

    val launcherVideo = rememberLauncherForActivityResult(ActivityResultContracts.CaptureVideo()) {
        if(it){
            videoGrabado.value=true
        }
    }

    Column{
        if(videoGrabado.value) MyPlayer(uriVideo)
        MuestraParteInferiorVideo(multimedia, launcherVideo, launcherExplorador, uriVideo, archivoExiste, db)
    }
}
fun copiaVideo(uri: Uri, archivo: String, contentResolver: ContentResolver) {
    val inputStream: InputStream? =
        contentResolver.openInputStream(uri)
    val outputStream: OutputStream = FileOutputStream(archivo)
    val buf = ByteArray(1024)
    var len: Int
    if (inputStream != null) {
        while (inputStream.read(buf).also { len = it } > 0) {
            outputStream.write(buf, 0, len)
        }
    }
    outputStream.close()
    inputStream?.close()
}
@Composable
fun MuestraParteInferiorVideo(
    multimedia: Multimedia,
    launcherVideo: ManagedActivityResultLauncher<Uri, Boolean>,
    launcherExplorador: ManagedActivityResultLauncher<Array<String>, Uri?>,
    uriVideo: Uri,
    archivoExiste: Boolean,
    db: NotasBD
) {
    val descripcion = remember {
        mutableStateOf(multimedia.descripcion)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().fillMaxHeight()){
        TextField(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp, start = 20.dp, end = 20.dp),
            value = descripcion.value,
            onValueChange = {
                descripcion.value = it
            }
        )
        if(!archivoExiste) {
            Button(
                modifier = Modifier.padding(top = 20.dp),
                onClick = { launcherVideo.launch(uriVideo) }
            ) {
                Text(text = "Grabar video")
            }
            Button(
                modifier = Modifier.padding(top = 20.dp),
                onClick = { launcherExplorador.launch(arrayOf("video/*")) }) {
                Text(text = "Seleccionar video")
            }
        }
    }
    multimedia.descripcion = descripcion.value
    db.DAONotas().update(multimedia)
}

@Composable
fun MyPlayer(uri: Uri) {
    AndroidView(
        factory = { localContext ->
            val videoView = VideoView(localContext)
            videoView.setVideoURI(uri)
            videoView.setOnCompletionListener {
                videoView.start()
            }
            videoView.start()
            videoView
        }
    )
}