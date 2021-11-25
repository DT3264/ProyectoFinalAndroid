package com.dcerna.proyectofinal

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dcerna.proyectofinal.data.NotasBD
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import android.app.PendingIntent
import com.dcerna.proyectofinal.data.Nota


class MiReceiverAlarma : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val db = NotasBD.getInstance(context)
        val idNota = intent.getLongExtra("idNota", -1L)
        if(idNota == -1L) return
        val nota = db.DAONotas().getNota(idNota.toString())
        val esFechaLimite = intent.getBooleanExtra("esFechaLimite", false)
        if(esFechaLimite && aTiempo(nota.fechaLimite)){
            createNotificationChannel(context, intent)
            mostrarNotificacion(context, nota.idNota*1000, nota)
            return
        }
        val idRecordatorio = intent.getLongExtra("idRecordatorio", -1L)
        // Si no existe, el recordatorio se eliminó
        if(idRecordatorio == -1L) return
        val recordatorio = db.DAONotas().getRecordatorioPorID(idRecordatorio.toString())
        // Si el recordatorio tiene que apareceer en este momento
        // (pudo modificarse la fecha de muestra)
        if(aTiempo(recordatorio.fechaRecordatorio)){
            createNotificationChannel(context, intent)
            mostrarNotificacion(context, recordatorio.idRecordatorio, nota)
            db.DAONotas().delete(recordatorio)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun aTiempo(fechaRecordatorio: Long): Boolean {
        val ldt: LocalDateTime = LocalDateTime.now()
        val zdt: ZonedDateTime = ldt.atZone(ZoneOffset.systemDefault())
        val fechaAct: Long = zdt.toInstant().toEpochMilli()
        return fechaAct+60 >= fechaRecordatorio
    }

    private fun mostrarNotificacion(
        context: Context,
        idRecordatorio: Long,
        nota: Nota,
    ) {

        var intent = Intent(context, MainActivity::class.java).let { intent ->
            intent.putExtra("idNota", nota.idNota)
            PendingIntent.getActivity(context, (nota.idNota*1000).toInt(), intent, 0)
        }
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(nota.titulo)
            .setContentText(nota.nota)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(intent)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(idRecordatorio.toInt(), builder.build())
    }

    companion object {
        private const val CHANNEL_ID = "RECORDATORIOS-TAREAS"
        fun createNotificationChannel(ctx: Context, intent: Intent?) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name: CharSequence = "RECORDATORIOS"
                val description = "Muestra recordatorios definidos"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance)
                channel.description = description
                val notificationManager = ctx.getSystemService(
                    NotificationManager::class.java
                )
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}

