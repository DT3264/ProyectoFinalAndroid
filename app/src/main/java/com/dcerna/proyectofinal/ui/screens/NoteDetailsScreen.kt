package com.dcerna.proyectofinal.ui.screens

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dcerna.proyectofinal.MiReceiverAlarma
import com.dcerna.proyectofinal.R
import com.dcerna.proyectofinal.data.NotasBD
import com.dcerna.proyectofinal.util.Fecha
import com.dcerna.proyectofinal.util.Hora
import com.dcerna.proyectofinal.util.getDate
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun NoteDetailsScreen(navController: NavController, noteID: String) {
    Surface(color = MaterialTheme.colors.background) {
        NoteDetails(noteID, navController)
        //    EjemploDialogos()
    }
}

@Composable
fun NoteDetails(noteID: String, navController: NavController) {
    val context = LocalContext.current
    val showMenu = remember { mutableStateOf(false) }
    val dialogAgregar = remember { mutableStateOf(false) }
    val db = NotasBD.getInstance(context)
    val datePickerDialog = getDatePickerDialogAdd(context, noteID)
    if (dialogAgregar.value) {
        MuestraDialogEliminar(dialogAgregar, navController, noteID)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 4.dp,
                title = {
                    Text(stringResource(R.string.NOTE_DETAILS))
                },
                backgroundColor = MaterialTheme.colors.primarySurface,
                actions = {
                    IconButton(onClick = { showMenu.value = !showMenu.value }) {
                        Icon(Icons.Filled.MoreVert, null)
                    }
                    DropdownMenu(expanded = showMenu.value,
                        onDismissRequest = { showMenu.value = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            showMenu.value = false
                            navController.navigate(route = "multimedia/$noteID")
                        }) {
                            Text(stringResource(R.string.MULTIMEDIA))
                        }
                        val nota = db.DAONotas().getNota(noteID)
                        if (nota != null) {
                            if (nota.esTarea) {
                                DropdownMenuItem(onClick = {
                                    showMenu.value = false
                                    navController.navigate(route = "recordatorios/$noteID")
                                }) {
                                    Text(stringResource(id = R.string.REMINDERS))
                                }
                            }
                        }
                        DropdownMenuItem(onClick = {
                            showMenu.value = false
                            dialogAgregar.value = true
                        }) {
                            Text(stringResource(id = R.string.ERASE))
                        }
                    }
                })
        },

        bottomBar = {
            val nota = db.DAONotas().getLiveNota(noteID).observeAsState()
            if (nota != null) {
                if (nota.value?.esTarea == true) {
                    BottomAppBar(

                        Modifier
                            .padding(8.dp)
                    ) {
                        TextButton(
                            onClick = {
                                datePickerDialog.show()
                            },
                            Modifier.fillMaxWidth()
                        ) {
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                                        withStyle(style = SpanStyle(color = Color.White)) {
                                            append("${stringResource(id = R.string.DATE)}: " + nota.value?.fechaLimite?.let {
                                                getDate(
                                                    it, "dd/MM/yyyy hh:mm a"
                                                )
                                            })
                                        }
                                    }
                                }
                            )
                        }


                    }
                }
            }

        }

    )
    {
        Column(
            Modifier.padding(8.dp)
        ) {
            Text(text = "${stringResource(R.string.TITLE)}: ")
            val nota = db.DAONotas().getNota(noteID)
            var titulo = ""
            var textNote = ""
            if (nota != null) {
                titulo = nota.titulo
                textNote = nota.nota

            }
            val textStateTitle = remember { mutableStateOf(titulo) }
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = textStateTitle.value,
                onValueChange = { textStateTitle.value = it }
            )
            Text("${stringResource(R.string.NOTE)}: ")
            val textStateNota = remember { mutableStateOf(textNote) }
            TextField(

                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                value = textStateNota.value,
                onValueChange = { textStateNota.value = it }
            )
            if (nota != null) {
                nota.titulo = textStateTitle.value
                nota.nota = textStateNota.value
                db.DAONotas().update(nota)
            }
        }


    }
}


fun eliminarNota(context: Context, noteID: String, navController: NavController) {
    val db = NotasBD.getInstance(context)
    val nota = db.DAONotas().getNota(noteID)
    db.DAONotas().deleteAllMultimedio(noteID)
    db.DAONotas().deleteAllRecordatorios(noteID)
    db.DAONotas().delete(nota)
    navController.popBackStack()
}

@Composable
fun MuestraDialogEliminar(
    dialogState: MutableState<Boolean>,
    navController: NavController,
    noteid: String
) {
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
                Text(stringResource(R.string.ERASE_NOTE_QUESTION))
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
                        dialogState.value = false
                        eliminarNota(context, noteid, navController)
                    }
                ) {
                    Text(stringResource(R.string.YES))
                }
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        dialogState.value = false
                    }
                ) {
                    Text(stringResource(R.string.NO))
                }
            }
        }
    )
}

fun getDatePickerDialogAdd(
    context: Context, noteID: String
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
            val timePickerDialog = getTimePickerDialogAdd(context, fechaSeleccionada, noteID)
            timePickerDialog.show()
        },
        // La fecha actual donde el calendario inicia
        anioActual, mesActual, diaActual
    )

    // Fecha mínima para que no escoja una fecha anterior
    datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis

    return datePickerDialog
}

fun getTimePickerDialogAdd(
    context: Context,
    fechaSeleccionada: Fecha,
    noteID: String,
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
            val db = NotasBD.getInstance(context)
            val nota = db.DAONotas().getNota(noteID)
            nota.fechaLimite = fechaYHoraSeleccionada.timeInMillis;
            db.DAONotas().update(nota)
            var alarmMgr = context.getSystemService(ALARM_SERVICE) as AlarmManager
            var alarmIntent = Intent(context, MiReceiverAlarma::class.java).let { intent ->
                intent.putExtra("idNota", nota.idNota)
                intent.putExtra("esFechaLimite", true)
                PendingIntent.getBroadcast(context, (nota.idNota*1000).toInt(), intent, 0)
            }
            alarmMgr.set(
                AlarmManager.RTC,
                fechaYHoraSeleccionada.timeInMillis,
                alarmIntent
            )

        },
        horaActual, minutoActual, false
    )
}