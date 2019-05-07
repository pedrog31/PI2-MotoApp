package co.edu.udea.motoapp.modelo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class IntegranteRuta (
    var conectado: Boolean,
    val ubicaciones: @RawValue MutableList<Ubicacion>) : Parcelable {

    constructor() : this(false, mutableListOf())

    @Parcelize
    class Ubicacion (
        val latitud: Double,
        val longitud: Double) : Parcelable {

        constructor(): this(0.0, 0.0)
    }
}