package co.edu.udea.motoapp.servicios

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import co.edu.udea.motoapp.modelo.IntegranteRuta
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UbicacionActualRuta : Service(), GoogleApiClient.ConnectionCallbacks, LocationListener {
    lateinit var idRuta: String
    lateinit var integranteRuta: IntegranteRuta
    lateinit var apiClient: GoogleApiClient
    val identificadorMotero = FirebaseAuth.getInstance().uid
    val firebaseDatabase = FirebaseDatabase
        .getInstance()
        .reference

    val escuchadorIntegranteRuta = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(datoIntegranteRuta: DataSnapshot) {
            if (datoIntegranteRuta.exists()) {
                integranteRuta = datoIntegranteRuta.getValue(IntegranteRuta::class.java) ?: return
                iniciarLocalizacion()
            }
        }
    }

    val escuchadorEstadoRuta = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(datoEstadoRuta: DataSnapshot) {
            if (datoEstadoRuta.exists() && datoEstadoRuta.value.toString() == "Finalizada")
                onDestroy()
        }
    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.hasExtra("idRuta")) {
            idRuta = intent.getStringExtra("idRuta")
            firebaseDatabase
                .child("rutasPrivadas/$idRuta/integrantes/$identificadorMotero")
                .addListenerForSingleValueEvent(escuchadorIntegranteRuta)
            firebaseDatabase
                .child("rutasPrivadas/$idRuta/estado")
                .addValueEventListener(escuchadorEstadoRuta)
        }
        return START_REDELIVER_INTENT
    }

    @SuppressLint("MissingPermission")
    private fun iniciarLocalizacion() {
        apiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addApi(LocationServices.API)
            .build()
        apiClient.connect()

        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val locationSettingsRequest =
            LocationSettingsRequest
                .Builder()
                .addLocationRequest(locationRequest)
                .build()

        val result = LocationServices.SettingsApi.checkLocationSettings(
            apiClient, locationSettingsRequest
        )

        result.setResultCallback { locationSettingsResult ->
            if (locationSettingsResult.status.isSuccess) {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                    apiClient,
                    locationRequest,
                    this@UbicacionActualRuta
                )
                mostrarMensaje("Ruta iniciada correctamente")
            } else
                if (locationSettingsResult.status.hasResolution()) {
                    mostrarMensaje("Pasaron cosas malas :(, Debes encender el GPS")
                    stopSelf()
                } else {
                    mostrarMensaje("Pasaron cosas malas :(, no tienes GPS")
                    stopSelf()
                }
        }

    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this.baseContext, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            enviarUbicacion(location.latitude, location.longitude)
        }
    }

    private fun enviarUbicacion(latitud: Double, longitud: Double) {
        val ubicacion = IntegranteRuta.Ubicacion(latitud, longitud)
        integranteRuta.ubicaciones.add(ubicacion)
        firebaseDatabase
            .child("rutasPrivadas/$idRuta/integrantes/$identificadorMotero")
            .setValue(integranteRuta)
    }

    private fun finalizarUbicacion() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
            apiClient, this
        )
        apiClient.disconnect()
        integranteRuta.conectado = false
        firebaseDatabase
            .child("rutasPrivadas/$idRuta/integrantes/$identificadorMotero")
            .setValue(integranteRuta)
    }

    override fun onDestroy() {
        finalizarUbicacion()
        super.onDestroy()
    }
}