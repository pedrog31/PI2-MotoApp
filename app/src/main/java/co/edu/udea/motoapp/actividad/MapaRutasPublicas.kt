package co.edu.udea.motoapp.actividad
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.ui.rutas_publicas.ModeloVistaRutasPublicas
import co.edu.udea.motoapp.ui.rutas_publicas.MapaRutasPublicas
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MapaRutasPublicas : AppCompatActivity() {

    private lateinit var modeloVistaRutasPublicas: ModeloVistaRutasPublicas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Dexter
            .withActivity(this@MapaRutasPublicas)
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
                        this@MapaRutasPublicas,
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
        setContentView(R.layout.actividad_mapa_rutas_publicas)
        modeloVistaRutasPublicas = ViewModelProviders.of(this).get(ModeloVistaRutasPublicas::class.java)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MapaRutasPublicas.newInstance())
                .commitNow()
    }
}
