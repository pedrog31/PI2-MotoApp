package co.edu.udea.motoapp.modelo

class Motero(
    val nombre: String,
    val correo: String,
    val celular: String,
    val ciudad: String,
    val urlFoto: String,
    val retos: MutableList<String>) {

    constructor() : this("", "", "", "", "",mutableListOf()) {

    }

}