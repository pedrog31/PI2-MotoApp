package co.edu.udea.motoapp.modelo

class Motero(
    val nombre: String,
    val correo: String,
    val celular: String,
    val ciudad: String,
    val urlFoto: String,
    val amigos: HashMap<String, Boolean>,
    val retos: MutableList<String>,
    val fcmToken: String) {

    constructor() : this("", "", "", "", "", hashMapOf(), mutableListOf(), "") {

    }

}