package co.edu.udea.motoapp.modelo

class RutaPrivada(
    val nombre: String,
    val descripcion: String,
    val urlFoto: String,
    val breakpoints: MutableList<Parada>,
    val dificultad: String,
    val distancia: Int,
    val participantes: HashMap<String, Boolean>,
    val calificacion: Float,
    val numeroCalificaciones: Int) {

    constructor():this("","","", mutableListOf(),"",0, hashMapOf(),5F,0)
}