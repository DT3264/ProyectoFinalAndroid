package com.dcerna.proyectofinal

class Fecha {
    var anio: Int = 0
    var mes: Int = 0
    var dia: Int = 0

    constructor(anio: Int, mes: Int, dia: Int) {
        this.anio = anio
        this.mes = mes
        this.dia = dia
    }


}
fun FechaDefecto():Fecha{
    return Fecha(-1, 0, 0)
}