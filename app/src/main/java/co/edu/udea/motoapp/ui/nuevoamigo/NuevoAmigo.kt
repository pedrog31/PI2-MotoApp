package co.edu.udea.motoapp.ui.nuevoamigo

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.ui.lista_amigos.AdaptadorAmigo
import co.edu.udea.motoapp.ui.perfil.ModeloVistaMotero
import kotlinx.android.synthetic.main.fragmento_nuevo_amigo.*


class NuevoAmigo : Fragment() {

    companion object {
        fun newInstance() = NuevoAmigo()
    }

    private val observadorPalabraClave = Observer<String> { palabraClave ->
        palabraClave?.let {
            buscarAmigos()
        }
    }
    private val observadorListaAmigos = Observer<HashMap<String, Motero>> { listaAmigos ->
        listaAmigos?.let {
            adaptadorVistaNuevosAmigos?.notifyDataSetChanged()
            resultado_busqueda.text = getString(R.string.resultado_busqueda, listaAmigos.size)
            resultado_busqueda.visibility = View.VISIBLE
        }
    }

    private lateinit var modeloVistaNuevoAmigo: ModeloVistaNuevoAmigo
    private var adaptadorVistaNuevosAmigos: AdaptadorAmigo? = null
    private lateinit var modeloVistaMotero: ModeloVistaMotero

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragmento_nuevo_amigo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            modeloVistaNuevoAmigo = ViewModelProviders.of(it).get(ModeloVistaNuevoAmigo::class.java)
            modeloVistaMotero = ViewModelProviders.of(it).get(ModeloVistaMotero::class.java)
            modeloVistaNuevoAmigo.palabraClave.observe(it, observadorPalabraClave)
            modeloVistaNuevoAmigo.listaMoteros.observe(it, this@NuevoAmigo.observadorListaAmigos)
            lista_amigos.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun buscarAmigos() {
        modeloVistaNuevoAmigo.buscarAmigos()
        if (adaptadorVistaNuevosAmigos == null) {
            adaptadorVistaNuevosAmigos = modeloVistaNuevoAmigo.listaMoteros.value?.let {
                AdaptadorAmigo(it, this@NuevoAmigo.activity!!, true)
            }
            lista_amigos.adapter = adaptadorVistaNuevosAmigos
        }
    }

}
