package co.edu.udea.motoapp.ui.mapa_rutas_publicas

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.modelo.Ruta
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapaRutasPublicas : Fragment(), OnMapReadyCallback {

    private lateinit var proveedorUbicacion: FusedLocationProviderClient
    private lateinit var modeloVistaMapaRutasPublicas: ModeloVistaMapaRutasPublicas
    private lateinit var mapaRutasPublicas: GoogleMap
    private val observadorListaMoterosSolicitantes = Observer<MutableList<Ruta>> { _ -> marcarRutasMapa() }
    companion object {
        fun newInstance() = MapaRutasPublicas()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragmento_mapa_rutas_publicas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let {
            modeloVistaMapaRutasPublicas = ViewModelProviders.of(it).get(ModeloVistaMapaRutasPublicas::class.java)
            modeloVistaMapaRutasPublicas.listaRutasPublicas.observe(it, this.observadorListaMoterosSolicitantes)
            proveedorUbicacion = LocationServices.getFusedLocationProviderClient(it)
        }
        (childFragmentManager.findFragmentById(R.id.mapa_rutasPublicas) as? SupportMapFragment)?.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(mapaRutasPublicasGoogle: GoogleMap) {
        mapaRutasPublicas = mapaRutasPublicasGoogle
        mapaRutasPublicas.isMyLocationEnabled = true
        mapaRutasPublicasGoogle.setOnMarkerClickListener { marcador ->
            mostrarInformacionRuta(marcador)
            true
        }
        proveedorUbicacion.lastLocation.addOnSuccessListener {
            mapaRutasPublicas.animateCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 12F)
            )
        }
        modeloVistaMapaRutasPublicas.buscarRutas()
    }

    private fun marcarRutasMapa() {
        mapaRutasPublicas.clear()
        modeloVistaMapaRutasPublicas.listaRutasPublicas.value?.forEach {
            mapaRutasPublicas.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(it.paradas[0].latitud, it.paradas[0].longitud)
                    ))
        }
    }

    private fun mostrarInformacionRuta(marcador: Marker) {

    }

}
