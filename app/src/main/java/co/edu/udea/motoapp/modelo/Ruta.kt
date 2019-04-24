package co.edu.udea.motoapp.modelo

open class Ruta(
    val descripcion: String,
    val distancia: Int,
    val experiencia: Int,
    val nivelDificultad: String,
    val nombre: String,
    var urlFoto: String,
    val paradas: MutableList<ParadaRuta>,
    val calificacion: Float,
    val numeroCalificaciones: Int) {

    constructor() : this(
        "",
        0,
        0,
        "",
        "",
        "",
        mutableListOf(),
        0.0F,
        0)
}