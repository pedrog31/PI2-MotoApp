package co.edu.udea.motoapp.modelo

class ParadaRuta (
    val nombre: String,
    val latitud: Double,
    val longitud: Double) {

    constructor(): this("", 0.0, 0.0)
}