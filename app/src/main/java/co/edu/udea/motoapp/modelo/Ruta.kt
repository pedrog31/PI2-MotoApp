package co.edu.udea.motoapp.modelo

class Ruta(
    val descripcion: String,
    val distancia: Int,
    val experiencia: Int,
    val nivel: String,
    val nombre: String,
    val urlFoto: String,
    val paradas: MutableList<ParadaRuta>) {

    constructor() : this("", 0, 0, "", "", "", mutableListOf())
}