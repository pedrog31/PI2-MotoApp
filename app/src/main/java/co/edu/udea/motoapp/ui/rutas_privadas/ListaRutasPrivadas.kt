package co.edu.udea.motoapp.ui.rutas_privadas

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
import kotlinx.android.synthetic.main.fragmento_lista_rutas_privadas.*


class ListaRutasPrivadas : Fragment() {

    lateinit var invitacionRutasRV : RecyclerView
    lateinit var rutasPrivadasRV : RecyclerView

    companion object {
        fun nuevaInstancia() = ListaRutasPrivadas()
    }

    private lateinit var modeloVistaListaRutasPrivadas: ModeloVistaListaRutasPrivadas
    private var adaptadorVistaAmigos: AdaptadorRuta? = null
    private var adaptadorVistaInvitacionRutas: AdaptadorRuta? = null

    private val observadorListaRutasPrivadas = Observer<HashMap<String, RutaPrivada>> { listaRutas ->
        listaRutas?.let {
            adaptadorVistaAmigos?.notifyDataSetChanged()
            resultado_busqueda_rutas_privadas.text = getString(R.string.resultado_busqueda_rutas_privadas, it.size)
        }
    }

    private val observadorListaInvitacionRutas = Observer<HashMap<String, RutaPrivada>> { listaRutas ->
        listaRutas?.let {
            adaptadorVistaInvitacionRutas?.notifyDataSetChanged()
            resultado_busqueda_invitaciones_ruta.text = getString(R.string.resultado_busqueda_invitaciones_ruta, it.size)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragmento_lista_rutas_privadas, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        iniciarAdapterSolicitudesRuta()
        iniciarAdapterRutasPrivadas()
    }

    fun iniciarAdapterSolicitudesRuta(){
        invitacionRutasRV = list_invitacion_rutas as RecyclerView
        invitacionRutasRV.setHasFixedSize(true)
        activity?.let {
            modeloVistaListaRutasPrivadas = androidx.lifecycle.ViewModelProviders.of(it).get(co.edu.udea.motoapp.ui.rutas_privadas.ModeloVistaListaRutasPrivadas::class.java)
            if (modeloVistaListaRutasPrivadas.listaInvitacionRutas.value == null) {
                modeloVistaListaRutasPrivadas.buscarInvitacionRutas()
            }
            modeloVistaListaRutasPrivadas.listaInvitacionRutas.observe(it, this@ListaRutasPrivadas.observadorListaInvitacionRutas)
        }
        invitacionRutasRV.layoutManager = LinearLayoutManager(this.context,RecyclerView.HORIZONTAL, false)
        adaptadorVistaInvitacionRutas = modeloVistaListaRutasPrivadas.listaInvitacionRutas.value?.let {
            AdaptadorRuta(it, this@ListaRutasPrivadas.activity!!, 0)
        }
        invitacionRutasRV.adapter = adaptadorVistaInvitacionRutas
    }

    fun iniciarAdapterRutasPrivadas(){
        rutasPrivadasRV = list as RecyclerView
        rutasPrivadasRV.setHasFixedSize(true)

        activity?.let {
            modeloVistaListaRutasPrivadas = androidx.lifecycle.ViewModelProviders.of(it).get(co.edu.udea.motoapp.ui.rutas_privadas.ModeloVistaListaRutasPrivadas::class.java)
            if (modeloVistaListaRutasPrivadas.listaRuta.value == null) {
                modeloVistaListaRutasPrivadas.buscarRutas()
            }
            modeloVistaListaRutasPrivadas.listaRuta.observe(it, this@ListaRutasPrivadas.observadorListaRutasPrivadas)
        }
        rutasPrivadasRV.layoutManager = LinearLayoutManager(this.context)
        adaptadorVistaAmigos = modeloVistaListaRutasPrivadas.listaRuta.value?.let {
            co.edu.udea.motoapp.ui.rutas_privadas.AdaptadorRuta(it, this@ListaRutasPrivadas.activity!!, 1)
        }
        rutasPrivadasRV.adapter = adaptadorVistaAmigos
    }

}
