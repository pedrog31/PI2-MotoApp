package co.edu.udea.motoapp.ui.lista_solicitudes

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
import co.edu.udea.motoapp.modelo.SolicitudAmistad
import co.edu.udea.motoapp.ui.lista_amigos.AdaptadorAmigo
import kotlinx.android.synthetic.main.fragmento_lista_solicitudes_amistad.*

class listaSolicitudesAmistad : Fragment() {

    private lateinit var modeloVistaSolicitudesAmistad: ModeloVistaSolicitudesAmistad
    private var adaptadorVistaSolicitudes: AdaptadorAmigo? = null
    private val observadorListaMoterosSolicitantes = Observer<HashMap<String, Motero>> { listaMoterosSolicitantes ->
        listaMoterosSolicitantes?.let {
            resultado_busqueda_solicitudes.text = getString(R.string.resultado_busqueda_solicitudes, listaMoterosSolicitantes.size)
            adaptadorVistaSolicitudes?.notifyDataSetChanged()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragmento_lista_solicitudes_amistad, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(lista_solicitudes) {
            activity?.let {
                modeloVistaSolicitudesAmistad = ViewModelProviders.of(it).get(ModeloVistaSolicitudesAmistad::class.java)
                if (modeloVistaSolicitudesAmistad.listaMoterosSolicitantes.value == null) {
                    modeloVistaSolicitudesAmistad.buscarSolicitudes()
                }
                modeloVistaSolicitudesAmistad.listaMoterosSolicitantes.observe(
                    it,
                    this@listaSolicitudesAmistad.observadorListaMoterosSolicitantes
                )
            }
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adaptadorVistaSolicitudes = modeloVistaSolicitudesAmistad.listaMoterosSolicitantes.value?.let {
                AdaptadorAmigo(it, this@listaSolicitudesAmistad.activity!!, getString(R.string.tipo_aceptacion))
            }
            adapter = adaptadorVistaSolicitudes
        }
    }
}
