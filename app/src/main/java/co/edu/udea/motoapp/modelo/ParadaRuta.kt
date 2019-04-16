package co.edu.udea.motoapp.modelo

class ParadaRuta (
    val latitud: Float,
    val longitud: Float,
    val nombre: String) {

    constructor(): this(0.0F, 0.0F, "")
}