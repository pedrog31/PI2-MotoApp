package co.edu.udea.motoapp.ui.nuevoamigo

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
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.ui.lista_amigos.AdaptadorAmigo
import com.google.firebase.database.FirebaseDatabase



class NuevoAmigo : Fragment() {

    companion object {
        fun newInstance() = NuevoAmigo()
    }

    private val observadorPalabraClave = Observer<String> { palabraClave ->
        palabraClave?.let {
            buscarAmigos(palabraClave)
        }
    }
    private val observadorListaAmigos = Observer<HashMap<String, Motero>> { listaAmigos ->
        listaAmigos?.let {
            adaptadorVistaNuevosAmigos?.notifyDataSetChanged()
        }
    }

    private lateinit var modeloVistaNuevoAmigo: ModeloVistaNuevoAmigo
    private var adaptadorVistaNuevosAmigos: AdaptadorAmigo? = null
    private lateinit var vistaNuevoAmigo: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        vistaNuevoAmigo = inflater.inflate(R.layout.fragmento_nuevo_amigo, container, false) as RecyclerView
        return vistaNuevoAmigo
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            modeloVistaNuevoAmigo = ViewModelProviders.of(it).get(ModeloVistaNuevoAmigo::class.java)
            modeloVistaNuevoAmigo.palabraClave.observe(it, observadorPalabraClave)
            modeloVistaNuevoAmigo.listaAmigos.observe(it, this@NuevoAmigo.observadorListaAmigos)
            vistaNuevoAmigo.layoutManager = LinearLayoutManager(context)
            adaptadorVistaNuevosAmigos = modeloVistaNuevoAmigo.listaAmigos.value?.values?.let { AdaptadorAmigo(it, this@NuevoAmigo.context!!) }
            vistaNuevoAmigo.adapter = adaptadorVistaNuevosAmigos
        }
    }

    private fun buscarAmigos(palabraClave: String) {
        val query = FirebaseDatabase
            .getInstance()
            .getReference("moteros")
            .orderByChild("nombre")
            .startAt(palabraClave)
            .endAt("${palabraClave}\uf8ff")
        query.removeEventListener(modeloVistaNuevoAmigo.escuchadorIdentificadorAmigos)
        modeloVistaNuevoAmigo.buscarAmigos()
    }

}
