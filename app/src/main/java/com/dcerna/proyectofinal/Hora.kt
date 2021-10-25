package com.dcerna.proyectofinal

class Hora {
    var horas: Int = 0
    var minutos: Int = 0

    constructor(horas: Int, minutos: Int) {
        this.horas = horas
        this.minutos = minutos
    }

}


fun HoraDefecto():Hora{
    return Hora(-1, 0)
}