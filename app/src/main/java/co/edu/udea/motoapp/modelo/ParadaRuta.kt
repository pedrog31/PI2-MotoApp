package co.edu.udea.motoapp.modelo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ParadaRuta (
    val nombre: String,
    val latitud: Double,
    val longitud: Double): Parcelable {

    constructor(): this("", 0.0, 0.0)
}