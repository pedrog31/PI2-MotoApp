package co.edu.udea.motoapp.modelo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
open class Ruta(
    open val descripcion: String,
    open val distancia: Int,
    open val experiencia: Int,
    open val nivelDificultad: String,
    open val nombre: String,
    open var urlFoto: String,
    open val paradas: @RawValue MutableList<ParadaRuta>,
    open val calificacion: Float,
    open val numeroCalificaciones: Int): Parcelable {

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