package com.dcerna.proyectofinal.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "multimedias"
)
data class Multimedia(
    @PrimaryKey(autoGenerate = true)
    var idMultimedia: Long = 0L,
    @ColumnInfo(index = true)
    var idNota: Long = 0L,
    var descripcion: String,
    var ruta: String,
    var tipo: String
)