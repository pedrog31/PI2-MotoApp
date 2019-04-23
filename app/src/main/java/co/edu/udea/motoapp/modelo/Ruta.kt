package co.edu.udea.motoapp.modelo

open class Ruta(
    open val descripcion: String,
    open val distancia: Int,
    open val experiencia: Int,
    open val nivelDificultad: String,
    open val nombre: String,
    open val urlFoto: String,
    open val paradas: MutableList<ParadaRuta>,
    open val calificacion: Float,
    open val numeroCalificaciones: Int) {

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