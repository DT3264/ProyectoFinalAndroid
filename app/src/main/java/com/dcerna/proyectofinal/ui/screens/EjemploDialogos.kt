package com.dcerna.proyectofinal.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dcerna.proyectofinal.Fecha
import com.dcerna.proyectofinal.Hora
import java.util.*

@Composable
fun EjemploDialogos(){
    val context = LocalContext.current
    val dialogAgregar = remember { mutableStateOf(false) }

    val showMenu = remember { mutableStateOf(false) }

    val datePickerDialog = getDatePickerDialog(context)
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
            FloatingActionButton(onClick = { dialogAgregar.value = true}) {
                Icon(Icons.Filled.Add, null)
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                datePickerDialog.show()
            }) {
                Text(text = "Selecciona fecha")
            }
        }
    }
}

fun getDatePickerDialog(
    context: Context,
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
            val timePickerDialog = getTimePickerDialog(context, fechaSeleccionada)
            timePickerDialog.show()
        },
        // La fecha actual donde el calendario inicia
        anioActual, mesActual, diaActual
    )

    // Fecha mínima para que no escoja una fecha anterior
    datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis

    return datePickerDialog
}

fun getTimePickerDialog(
    context: Context,
    fechaSeleccionada: Fecha,
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

            // Aquí se hace lo que se requiera con la fecha/hora

        },
        horaActual, minutoActual, false
    )
}