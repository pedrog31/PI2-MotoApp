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
import co.edu.udea.motoapp.excepcion.ExcepcionAutenticacion
import co.edu.udea.motoapp.modelo.Motero
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ListaAmigos : Fragment() {

    private lateinit var modeloVistaAmigos: ModeloVistaAmigos
    private var adaptadorVistaAmigos: AdaptadorAmigo? = null
    private val observadorListaAmigos = Observer<HashMap<String, Motero>> { listaAmigos ->
        listaAmigos?.let {
            adaptadorVistaAmigos?.notifyDataSetChanged()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmento_amigo_lista, container, false)
        if (view is RecyclerView) {
            with(view) {
                activity?.let {
                    modeloVistaAmigos = ViewModelProviders.of(it).get(ModeloVistaAmigos::class.java)
                    if (modeloVistaAmigos.listaAmigos.value == null) {
                        val moteroId = FirebaseAuth.getInstance().uid ?: throw ExcepcionAutenticacion()
                        val query = FirebaseDatabase.getInstance().reference.child("moteros/${moteroId}/amigos")
                        modeloVistaAmigos.buscarAmigos(query)
                    }
                    modeloVistaAmigos.listaAmigos.observe(it, this@ListaAmigos.observadorListaAmigos)
                }
               layoutManager = LinearLayoutManager(context)
                adaptadorVistaAmigos = modeloVistaAmigos.listaAmigos.value?.let {
                    AdaptadorAmigo(it, this@ListaAmigos.activity!!,null)
                }
                adapter = adaptadorVistaAmigos
            }
        }
        return view
    }
}
