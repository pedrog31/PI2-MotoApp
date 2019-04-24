package co.edu.udea.motoapp.modelo

import android.os.Parcelable
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class RutaPrivada (
    override val descripcion: String,
    override val distancia: Int,
    override val experiencia: Int,
    override val nivelDificultad: String,
    override val nombre: String,
    override val urlFoto: String,
    override val paradas: @RawValue MutableList<ParadaRuta>,
    override val calificacion: Float,
    override val numeroCalificaciones: Int,
    val propietario: String,
    val participantes: HashMap<String, Boolean>): Parcelable, Ruta(
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

    fun esPropietario() : Boolean {
        return propietario == FirebaseAuth.getInstance().uid
    }
}
