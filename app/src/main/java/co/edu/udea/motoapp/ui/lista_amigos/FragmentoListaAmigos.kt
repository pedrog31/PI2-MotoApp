package co.edu.udea.motoapp.ui.lista_amigos

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.modelo.Motero

class FragmentoListaAmigos : Fragment() {

    private lateinit var modeloVistaAmigos: ModeloVistaAmigos
    private var adaptadorVistaAmigos: AmigoRecyclerViewAdaptador? = null
    private val observadorEstadoRegistro = Observer<HashMap<String, Motero>> { estado ->
        estado?.let {
            adaptadorVistaAmigos?.notifyDataSetChanged()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmento_amigo_lista, container, false)
        if (view is RecyclerView) {
            with(view) {
                modeloVistaAmigos = ViewModelProviders.of(this@FragmentoListaAmigos).get(ModeloVistaAmigos::class.java)
                if (modeloVistaAmigos.listaAmigos.value == null)
                    modeloVistaAmigos.buscarAmigos()
                modeloVistaAmigos.listaAmigos.observe(this@FragmentoListaAmigos, this@FragmentoListaAmigos.observadorEstadoRegistro)
                layoutManager = LinearLayoutManager(context)
                adaptadorVistaAmigos = modeloVistaAmigos.listaAmigos.value?.values?.let { AmigoRecyclerViewAdaptador(it, this@FragmentoListaAmigos.context!!) }
                adapter = adaptadorVistaAmigos
            }
        }
        return view
    }
}
