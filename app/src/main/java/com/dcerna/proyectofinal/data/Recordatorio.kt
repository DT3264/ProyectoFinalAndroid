package com.dcerna.proyectofinal.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "recordatorios"
)
data class Recordatorio(
    @PrimaryKey(autoGenerate = true)
    var idRecordatorio: Long = 0L,
    @ColumnInfo(index = true)
    var idNota: Long = 0L,
    var fechaRecordatorio: Long,

)
