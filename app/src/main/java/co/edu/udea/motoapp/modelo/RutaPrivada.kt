package co.edu.udea.motoapp.modelo

class RutaPrivada (
    descripcion: String,
    distancia: Int,
    experiencia: Int,
    nivelDificultad: String,
    nombre: String,
    urlFoto: String,
    paradas: MutableList<ParadaRuta>,
    calificacion: Float,
    numeroCalificaciones: Int,
    val propietario: String,
    val participantes: HashMap<String, Boolean>): Ruta(
        descripcion = descripcion,
        distancia = distancia,
        experiencia = experiencia,
        nivelDificultad = nivelDificultad,
        nombre = nombre,
        urlFoto = urlFoto,
        paradas = paradas,
        calificacion = calificacion,
        numeroCalificaciones = numeroCalificaciones) {

    constructor(ruta: Ruta, propietario: String) :this(
        ruta.descripcion,
        ruta.distancia,
        ruta.experiencia,
        ruta.nivelDificultad,
        ruta.nombre,
        ruta.urlFoto,
        ruta.paradas,
        ruta.calificacion,
        ruta.numeroCalificaciones,
        propietario,
        hashMapOf()
    )

    constructor() :this(Ruta(), "")
}
