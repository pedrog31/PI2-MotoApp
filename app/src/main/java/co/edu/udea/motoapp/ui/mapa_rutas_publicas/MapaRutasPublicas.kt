package co.edu.udea.motoapp.ui.mapa_rutas_publicas

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.modelo.Ruta
import co.edu.udea.motoapp.ui.lista_solicitudes.ModeloVistaSolicitudesAmistad
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragmento_lista_solicitudes_amistad.*

class MapaRutasPublicas : Fragment(), OnMapReadyCallback {

    private lateinit var mapaRutasPublicas: GoogleMap
    private val observadorListaMoterosSolicitantes = Observer<MutableList<Ruta>> { listaRutasPublicas ->
        listaRutasPublicas?.let {

        }
    }

    companion object {
        fun newInstance() = MapaRutasPublicas()
    }

    private lateinit var modeloVistaMapaRutasPublicas: ModeloVistaMapaRutasPublicas

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmento_mapa_rutas_publicas, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            modeloVistaMapaRutasPublicas = ViewModelProviders.of(it).get(ModeloVistaMapaRutasPublicas::class.java)
            modeloVistaMapaRutasPublicas.listaRutasPublicas.observe(it, this.observadorListaMoterosSolicitantes)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (childFragmentManager.findFragmentById(R.id.mapa_rutasPublicas) as? SupportMapFragment)?.getMapAsync(this)
    }

    override fun onMapReady(mapaRutasPublicasGoogle: GoogleMap) {
        mapaRutasPublicas = mapaRutasPublicasGoogle
        mapaRutasPublicasGoogle.setOnMarkerClickListener { marcador ->
            true
        }
        val sydney = LatLng(-34.0, 151.0)
        mapaRutasPublicas.addMarker(
            MarkerOptions().position(sydney).title("Marker in Sydney")
                .snippet("This is my spot!"))
        mapaRutasPublicas.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        modeloVistaMapaRutasPublicas.buscarRutas()
    }

}
