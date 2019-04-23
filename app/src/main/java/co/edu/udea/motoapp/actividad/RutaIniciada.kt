package co.edu.udea.motoapp.actividad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.modelo.RutaPrivada
import co.edu.udea.motoapp.ui.ruta_iniciada.RutaIniciada

class RutaIniciada : AppCompatActivity() {
    var rutaActual: RutaPrivada? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_ruta_iniciada)
        val data = intent.extras
        rutaActual = data.getParcelable<RutaPrivada>("ruta")

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RutaIniciada.newInstance())
                .commitNow()
        }
    }

}
