package co.edu.udea.motoapp.actividad

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.modelo.RutaPrivada
import co.edu.udea.motoapp.ui.ruta_iniciada.ModeloVistaRutaIniciada
import co.edu.udea.motoapp.ui.ruta_iniciada.RutaIniciada
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class RutaIniciada : AppCompatActivity() {
    private lateinit var modeloVistaRutaIniciada: ModeloVistaRutaIniciada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Dexter
            .withActivity(this@RutaIniciada)
            .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(escuchadorPermisos)
            .check()
    }

    val escuchadorPermisos = object : MultiplePermissionsListener {
        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
            report?.let {
                if(it.areAllPermissionsGranted()) {
                    mostrarFragmentoMapaRutasPublicas()
                } else {
                    Toast.makeText(
                        this@RutaIniciada,
                        "Se necesitan permisos de ubicacion para acceder a esta opción :(",
                        Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }

        override fun onPermissionRationaleShouldBeShown(
            permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
            token?.continuePermissionRequest()
        }
    }

    private fun mostrarFragmentoMapaRutasPublicas() {
        modeloVistaRutaIniciada = ViewModelProviders.of(this).get(ModeloVistaRutaIniciada::class.java)
        modeloVistaRutaIniciada.rutaActual = intent?.extras?.getParcelable("ruta") ?: return
        modeloVistaRutaIniciada.keyRutaActual = intent?.extras?.getString("key_ruta") ?: return
        modeloVistaRutaIniciada.iniciarEscuchaEstadoRuta()
        setContentView(R.layout.actividad_ruta_iniciada)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, RutaIniciada.newInstance())
            .commitNow()
    }

}
