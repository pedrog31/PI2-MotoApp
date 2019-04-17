package co.edu.udea.motoapp.ui.rutas_privadas

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.modelo.RutaPrivada


class ListaRutasPrivadas : Fragment() {

    companion object {
        fun nuevaInstancia() = ListaRutasPrivadas()
    }

    private var adaptadorVistaAmigos: AdaptadorRuta? = null
    private val observadorListaRutasPrivadas = Observer<HashMap<String, RutaPrivada>> { listaRutas ->
        listaRutas?.let {
            adaptadorVistaAmigos?.notifyDataSetChanged()
        }
    }

    private lateinit var modeloVistaListaRutasPrivadas: ModeloVistaListaRutasPrivadas

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmento_lista_rutas_privadas, container, false)
        if (view is RecyclerView) {
            with(view) {
                activity?.let {
                    modeloVistaListaRutasPrivadas = ViewModelProviders.of(it).get(ModeloVistaListaRutasPrivadas::class.java)
                    if (modeloVistaListaRutasPrivadas.listaRuta.value == null) {
                        modeloVistaListaRutasPrivadas.buscarRutas()
                    }
                    modeloVistaListaRutasPrivadas.listaRuta.observe(it, this@ListaRutasPrivadas.observadorListaRutasPrivadas)
                }
                layoutManager = LinearLayoutManager(context)
                adaptadorVistaAmigos = modeloVistaListaRutasPrivadas.listaRuta.value?.let {
                    AdaptadorRuta(it, this@ListaRutasPrivadas.activity!!)
                }
                adapter = adaptadorVistaAmigos
            }
        }
        return view
    }
}
