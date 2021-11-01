package com.dcerna.proyectofinal.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateFormat.format
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcerna.proyectofinal.R
import com.dcerna.proyectofinal.data.NotasBD
import com.dcerna.proyectofinal.data.Recordatorio
import com.dcerna.proyectofinal.util.Fecha
import com.dcerna.proyectofinal.util.Hora
import com.dcerna.proyectofinal.util.getDate
import java.lang.String.format
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun RecordatoiriosScreen(noteID: String){
    val showMenu = remember { mutableStateOf(false) }
    val dialogEliminar = remember { mutableStateOf(false) }
    val recordatorioEliminar = remember{ mutableStateOf(Recordatorio(0,0,0))}

    val context = LocalContext.current
    val db = NotasBD.getInstance(context)
    val recordatorios = db.DAONotas().getLiveRecordatoriosPorIDNota(noteID).observeAsState()
    val datePickerDialog = getDatePickerDialogRecordatorio(context, noteID )

    if (dialogEliminar.value) {
       muestraDialogEliminarRecordatorio(dialogEliminar, context, recordatorioEliminar)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 4.dp,
                title = {
                    Text(stringResource(R.string.REMINDERS))
                },
                backgroundColor =  MaterialTheme.colors.primarySurface,
            )
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
            Column(modifier = Modifier.fillMaxHeight()) {
                recordatorios.value?.forEach { r -> mostrarRecordatorio(r, dialogEliminar, recordatorioEliminar) }
            }

        }

    }

}
@Composable
fun mostrarRecordatorio(recordatorio: Recordatorio, dialogEliminar: MutableState<Boolean>, recordatorioEliminar: MutableState<Recordatorio>){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable {
                dialogEliminar.value = true
                recordatorioEliminar.value = recordatorio
            }
        ,
        elevation = 10.dp
    ) {
        Text(text =  stringResource(R.string.DATE) + " " + getDate(recordatorio.fechaRecordatorio, "dd/MM/yyyy hh:mm a"), style = TextStyle(fontSize = 20.sp))
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
            // Aquí se hace lo que se requiera con la fecha/hora
            val db = NotasBD.getInstance(context)
            val recordatorio= Recordatorio(idNota=noteID.toLong(), fechaRecordatorio=fechaYHoraSeleccionada.timeInMillis.toLong())
            db.DAONotas().save(recordatorio)
            

        },
        horaActual, minutoActual, false
    )
}

@Composable
fun muestraDialogEliminarRecordatorio(dialogState: MutableState<Boolean>, context: Context, recordatorioEliminar: MutableState<Recordatorio>) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = {
            dialogState.value = false
        },
        title = {
            Text(stringResource(R.string.ERASE))
        },
        text = {

            Column {
                val recordatorio=recordatorioEliminar.value
                Text(stringResource(R.string.ERASE_REMINDER_QUESTION)+ " "+ getDate(recordatorio.fechaRecordatorio, "dd/MM/yyyy hh:mm a")+"?")
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
                        val eliminar = eliminarRecordatorio(context, recordatorioEliminar)
                        dialogState.value = false
                    }
                ) {
                    Text(stringResource(R.string.ERASE))
                }
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        dialogState.value = false

                    }
                ) {
                    Text(stringResource(R.string.CANCEL))
                }
            }
        }
    )
}

fun eliminarRecordatorio(context: Context, recordatorioEliminar: MutableState<Recordatorio>){
    val recordatorio=recordatorioEliminar.value
    val db = NotasBD.getInstance(context)
    db.DAONotas().delete(recordatorio)
}