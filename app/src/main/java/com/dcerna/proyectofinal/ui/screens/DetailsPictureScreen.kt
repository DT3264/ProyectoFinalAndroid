package com.dcerna.proyectofinal.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import android.content.ContentResolver
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dcerna.proyectofinal.R
import com.dcerna.proyectofinal.data.Multimedia
import com.dcerna.proyectofinal.data.NotasBD
import java.io.*


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun DetailsPictureScreen(multimediaID: String){
    val context = LocalContext.current
    val db = NotasBD.getInstance(context)
    var multimedia = db.DAONotas().getMultimediaPorIDMultimedia(multimediaID)
    Scaffold(
    ) {
        Surface(color = MaterialTheme.colors.background) {
            DetailsPicture(multimedia, db)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun DetailsPicture(multimedia: Multimedia, db: NotasBD) {
    var initialBitmap = ImageBitmap(1,1)
    val context = LocalContext.current
    val archivo = File(
        context.getExternalFilesDir(null),
        "${multimedia.idMultimedia}.${multimedia.tipo}"
    )
    var uriFoto = FileProvider.getUriForFile(
        context,
        "com.dcerna.proyectofinal",
        archivo
    )
    if(archivo.exists() && initialBitmap.width==1){
        val contentResolver: ContentResolver = context.contentResolver
        val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, uriFoto)
        initialBitmap = ImageDecoder.decodeBitmap(source).asImageBitmap()
    }

    val imageBitmap: MutableState<ImageBitmap> = remember {
        mutableStateOf(initialBitmap)
    }

    val launcherExplorador = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        // Se guardó el contenido de la imagen en la uri especificada
        if(it != null) {
            val contentResolver: ContentResolver = context.contentResolver
            copiaImagen(it, archivo.absolutePath, contentResolver)
            val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, it)
            val bitmap = ImageDecoder.decodeBitmap(source)
            imageBitmap.value = bitmap.asImageBitmap()
        }
    }

    val launcherFoto = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if(it){
            // Se guardó el contenido de la imagen en la uri especificada
            val contentResolver: ContentResolver = context.contentResolver
            val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, uriFoto)
            val bitmap = ImageDecoder.decodeBitmap(source)
            imageBitmap.value = bitmap.asImageBitmap()
        }
    }


    Column() {
        Image(imageBitmap.value, null, modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp))
        MuestraParteInferior(multimedia, launcherFoto, launcherExplorador, uriFoto, archivo.exists(), db)
    }
}
fun copiaImagen(uri: Uri, archivo: String, contentResolver: ContentResolver) {
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
fun MuestraParteInferior(
    multimedia: Multimedia,
    launcher: ManagedActivityResultLauncher<Uri, Boolean>,
    launcherExplorador: ManagedActivityResultLauncher<Array<String>, Uri?>,
    uriFoto: Uri,
    existeArchivo: Boolean,
    db: NotasBD
) {
    val descripcion = remember {
        mutableStateOf(multimedia.descripcion)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()){
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            value = descripcion.value,
            onValueChange = {
                descripcion.value = it
            }
        )
        if(!existeArchivo) {
            Button(
                modifier = Modifier.padding(top = 20.dp),
                onClick = { launcher.launch(uriFoto) }) {
                Text(text = stringResource(R.string.TAKE_PICTURE))
            }

            Button(
                modifier = Modifier.padding(top = 20.dp),
                onClick = { launcherExplorador.launch(arrayOf("image/*")) }) {
                Text(text = "Seleccionar foto")
            }
        }
    }
    multimedia.descripcion = descripcion.value
    db.DAONotas().update(multimedia)
}