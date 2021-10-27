package com.dcerna.proyectofinal.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dcerna.proyectofinal.Fecha
import com.dcerna.proyectofinal.Hora
import com.dcerna.proyectofinal.R
import com.dcerna.proyectofinal.data.DAONotas
import com.dcerna.proyectofinal.data.Nota
import com.dcerna.proyectofinal.data.NotasBD
import com.dcerna.proyectofinal.data.Recordatorio
import java.util.*


@Composable
fun RecordatoiriosScreen(noteID: String){
    val context = LocalContext.current
    val showMenu = remember { mutableStateOf(false) }
    val db = NotasBD.getInstance(context)
    val recordatorios = db.DAONotas().getRecordatoriosPorIDNota(noteID)
    val datePickerDialog = getDatePickerDialogRecordatorio(context, noteID )
    val dialogEliminar = remember { mutableStateOf(false) }


    Surface(color = MaterialTheme.colors.background) {
    }

    if (dialogEliminar.value) {
       MuestraDialogEliminarRecordatorio(dialogEliminar)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 4.dp,
                title = {
                    Text("I'm a TopAppBar")
                },
                backgroundColor =  MaterialTheme.colors.primarySurface,

                actions = {
                    IconButton(onClick = { showMenu.value = !showMenu.value }) {
                        Icon(Icons.Filled.MoreVert, null)
                    }
                    DropdownMenu(
                        expanded = showMenu.value,
                        onDismissRequest = { showMenu.value = false }
                    ) {
                        DropdownMenuItem(onClick = { /*TODO*/ }) {
                            Text("Multimedia")
                        }
                        DropdownMenuItem(onClick = { /*TODO*/ }) {
                            Text("Recordatorios")
                        }
                        DropdownMenuItem(onClick = { /*TODO*/ }) {
                            Text("Borrar")
                        }

                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { datePickerDialog.show()}) {
                Icon(Icons.Filled.Add, null)
            }
        }

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


        }
        Column{
            Text(text = "${stringResource(R.string.ID_NOTE)}: $noteID")
            Text(stringResource(R.string.REMINDERS))
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(recordatorios) {recordatorio ->
                    mostrarRecordatorio(context,recordatorio, dialogEliminar)
                }
            }

        }

    }

}
@Composable
fun mostrarRecordatorio(context: Context,recordatorio: Recordatorio, dialogEliminar: MutableState<Boolean>){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable {
                dialogEliminar.value = true
               // MuestraDialogEliminarRecordatorio(dialogoEliminar, recordatorio)
            }
        ,
        elevation = 10.dp
    ) {
        Text(text = "ID: " + recordatorio.idRecordatorio.toString()+ " Fecha: "+ recordatorio.fechaRecordatorio, style = TextStyle(fontSize = 30.sp))
    }
}


fun getDatePickerDialogRecordatorio(
    context: Context,
    noteID: String
): DatePickerDialog {
    val fechaActual = Calendar.getInstance()
    val anioActual = fechaActual[Calendar.YEAR]
    val mesActual = fechaActual[Calendar.MONTH]
    val diaActual = fechaActual[Calendar.DAY_OF_MONTH]

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            // Si selecciona la fecha, define la fecha y muestra el siguiente diálogo
            val fechaSeleccionada = Fecha(year, month, day)
            val timePickerDialog = getTimePickerDialogRecordatorio(context, fechaSeleccionada, noteID)
            timePickerDialog.show()
        },
        // La fecha actual donde el calendario inicia
        anioActual, mesActual, diaActual
    )

    // Fecha mínima para que no escoja una fecha anterior
    datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis

    return datePickerDialog
}

fun getTimePickerDialogRecordatorio(
    context: Context,
    fechaSeleccionada: Fecha,
    noteID: String
): TimePickerDialog {
    val fechaActual = Calendar.getInstance()
    val horaActual = fechaActual[Calendar.HOUR_OF_DAY]
    val minutoActual = fechaActual[Calendar.MINUTE]

    return TimePickerDialog(
        context,
        { _, hour, minute ->
            val horaSeleccionada = Hora(hour, minute)
            val fechaYHoraSeleccionada = Calendar.getInstance()
            fechaYHoraSeleccionada.set(
                fechaSeleccionada.anio,
                fechaSeleccionada.mes,
                fechaSeleccionada.dia,
                horaSeleccionada.horas,
                horaSeleccionada.minutos
            )
            println("Fecha seleccionada: ${fechaYHoraSeleccionada.timeInMillis}")
            println(noteID)
            // Aquí se hace lo que se requiera con la fecha/hora
            val db = NotasBD.getInstance(context)
             val recordatorio= Recordatorio(idNota=noteID.toLong(), fechaRecordatorio=fechaYHoraSeleccionada.timeInMillis.toLong())
            db.DAONotas().save(recordatorio)

        },
        horaActual, minutoActual, false
    )
}

@Composable
fun MuestraDialogEliminarRecordatorio(dialogState: MutableState<Boolean>) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = {
            dialogState.value = false
        },
        title = {
            Text(text = "Eliminar")
        },
        text = {
            Column {
                Text("¿Seguro que deseas eliminar?")
            }
        },
        buttons = {
            Column(
                modifier = Modifier.padding(all = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        //val eliminar = eliminarRecordatorio(context,recordatorio)

                    }
                ) {
                    Text("Eliminar")
                }
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        dialogState.value = false

                    }
                ) {
                    Text("Cancelar")
                }
            }
        }
    )
}

fun eliminarRecordatorio(context: Context, recordatorio: Recordatorio){
    val db = NotasBD.getInstance(context)
    db.DAONotas().delete(recordatorio)


}