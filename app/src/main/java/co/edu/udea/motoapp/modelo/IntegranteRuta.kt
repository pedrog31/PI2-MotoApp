package co.edu.udea.motoapp.modelo

class IntegranteRuta (
    val inicioRuta: Boolean,
    val latitud: Double,
    val longitud: Double) {

    constructor() : this(false, 0.0,0.0)
}