package com.dcerna.proyectofinal

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.dcerna.proyectofinal.data.Nota
import com.dcerna.proyectofinal.data.NotasBD
import com.dcerna.proyectofinal.data.Recordatorio
import java.time.LocalDateTime
import java.time.ZoneId

class BootReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val db = NotasBD.getInstance(context)
        val recordatorios = db.DAONotas().getRecordatorios()
        val time = LocalDateTime.now();
        val zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
        val fechaActual = time.atZone(zoneId).toEpochSecond();
        recordatorios.forEach {
            if(fechaActual >= it.fechaRecordatorio)
                agendaRecordatorio(context, it)
        }
        val notas = db.DAONotas().getNotas()
        notas.forEach {
            if(it.esTarea && fechaActual >= it.fechaLimite)
                agendaNota(context, it)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun agendaRecordatorio(context: Context, recordatorio: Recordatorio) {
        var alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var alarmIntent = Intent(context, MiReceiverAlarma::class.java).let { intent ->
            intent.putExtra("idRecordatorio", recordatorio.idRecordatorio)
            intent.putExtra("idNota", recordatorio.idNota)
            PendingIntent.getBroadcast(context, recordatorio.idRecordatorio.toInt(), intent, 0)
        }
//        var time = LocalDateTime.now();
//        var zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
//        var epoch = time.atZone(zoneId).toEpochSecond();
        alarmMgr.set(
            AlarmManager.RTC,
            recordatorio.fechaRecordatorio,
//            epoch + (5*1000),
            alarmIntent
        )
    }
    fun agendaNota(context: Context, nota: Nota) {
        var alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var alarmIntent = Intent(context, MiReceiverAlarma::class.java).let { intent ->
            intent.putExtra("idNota", nota.idNota)
            intent.putExtra("esFechaLimite", true)
            PendingIntent.getBroadcast(context, (nota.idNota*1000).toInt(), intent, 0)
        }
        alarmMgr.set(
            AlarmManager.RTC,
            nota.fechaLimite,
            alarmIntent
        )
    }
}