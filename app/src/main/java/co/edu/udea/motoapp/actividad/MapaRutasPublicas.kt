package co.edu.udea.motoapp.actividad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.ui.mapa_rutas_publicas.ModeloVistaMapaRutasPublicas
import co.edu.udea.motoapp.ui.mapa_rutas_publicas.MapaRutasPublicas

class MapaRutasPublicas : AppCompatActivity() {

    private lateinit var modeloVistaMapaRutasPublicas: ModeloVistaMapaRutasPublicas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_mapa_rutas_publicas)
        modeloVistaMapaRutasPublicas = ViewModelProviders.of(this).get(ModeloVistaMapaRutasPublicas::class.java)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MapaRutasPublicas.newInstance())
                .commitNow()
        }
    }
}
