package co.edu.udea.motoapp.actividad

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.ui.nuevoamigo.ModeloVistaNuevoAmigo
import co.edu.udea.motoapp.ui.nuevoamigo.NuevoAmigo
import kotlinx.android.synthetic.main.actividad_nuevo_amigo.*

class NuevoAmigo : AppCompatActivity() {

    private lateinit var modeloVistaNuevoAmigo: ModeloVistaNuevoAmigo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_nuevo_amigo)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NuevoAmigo.newInstance())
                .commitNow()
        }
        onSearchRequested()
        manejarIntent(intent)
        nuevoAmigoBotonFlotante.setOnClickListener {
            nuevoAmigoBotonFlotante.hide()
            onSearchRequested()
        }
        modeloVistaNuevoAmigo = ViewModelProviders.of(this).get(ModeloVistaNuevoAmigo::class.java)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        manejarIntent(intent)
    }

    private fun manejarIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            nuevoAmigoBotonFlotante.hide()
            modeloVistaNuevoAmigo.listaMoteros.value?.clear()
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                enviarPalabraClaveBusqueda(query)
            }
        }
    }

    private fun enviarPalabraClaveBusqueda(palabraClave: String) {
        nuevoAmigoBotonFlotante.show()
        modeloVistaNuevoAmigo.palabraClave.value = palabraClave
    }
}
