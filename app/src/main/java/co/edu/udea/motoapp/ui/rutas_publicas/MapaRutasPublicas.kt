package co.edu.udea.motoapp.ui.rutas_publicas

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tarjeta_informacion_ruta.*

class MapaRutasPublicas : Fragment(), OnMapReadyCallback {

    private lateinit var proveedorUbicacion: FusedLocationProviderClient
    private lateinit var modeloVistaRutasPublicas: ModeloVistaRutasPublicas
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
            modeloVistaRutasPublicas = ViewModelProviders.of(it).get(ModeloVistaRutasPublicas::class.java)
            modeloVistaRutasPublicas.listaRutasPublicas.observe(it, this.observadorListaMoterosSolicitantes)
            proveedorUbicacion = LocationServices.getFusedLocationProviderClient(it)
        }
        (childFragmentManager.findFragmentById(R.id.mapa_rutasPublicas) as? SupportMapFragment)?.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(mapaRutasPublicasGoogle: GoogleMap) {
        mapaRutasPublicas = mapaRutasPublicasGoogle
        mapaRutasPublicas.isMyLocationEnabled = true
        mapaRutasPublicasGoogle.setOnMarkerClickListener { marcador ->
            crearDialogoRutaSeleccionada(marcador)
            true
        }
        proveedorUbicacion.lastLocation.addOnSuccessListener {
            mapaRutasPublicas.animateCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 12F)
            )
        }
        modeloVistaRutasPublicas.buscarRutas()
    }

    private fun marcarRutasMapa() {
        mapaRutasPublicas.clear()
        modeloVistaRutasPublicas.listaRutasPublicas.value?.forEachIndexed { posicion, ruta ->
            mapaRutasPublicas.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(ruta.paradas[0].latitud, ruta.paradas[0].longitud)
                    )
            ).tag = posicion
        }
    }

    private fun crearDialogoRutaSeleccionada(marcador: Marker) {
        val rutaSeleccionada = this.modeloVistaRutasPublicas.listaRutasPublicas.value!![marcador.tag as Int]
        this.mostrarInformacionRuta(rutaSeleccionada,
            AlertDialog
            .Builder(this.requireContext())
            .setView(R.layout.tarjeta_informacion_ruta)
            .setPositiveButton(R.string.agregar_ruta_publica) { _, _ ->
                modeloVistaRutasPublicas.agregarRutaPublicaMotero(rutaSeleccionada, requireContext())
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
        )
    }

    private fun mostrarInformacionRuta(rutaSeleccionada: Ruta, dialogoInfoRuta: AlertDialog) {
        dialogoInfoRuta.show()
        dialogoInfoRuta.nombre_ruta_publica.text = rutaSeleccionada.nombre
        dialogoInfoRuta.descripcion_ruta_publica.text = rutaSeleccionada.descripcion
        dialogoInfoRuta.experiencia_ruta_publica.text = getString(
            R.string.experiencia_ruta, rutaSeleccionada.experiencia
        )
        dialogoInfoRuta.distancia_ruta_publica.text = getString(
            R.string.distancia_ruta, rutaSeleccionada.distancia
        )
        dialogoInfoRuta.dificultad_ruta_publica.text = getString(
            R.string.dificultad_ruta, rutaSeleccionada.nivelDificultad
        )
        dialogoInfoRuta.calificacion_ruta_publica.text = getString(
            R.string.calificacion_ruta, String.format("%.02f", rutaSeleccionada.calificacion)
        )
        val paradas = StringBuilder()
        rutaSeleccionada.paradas.forEach {
            paradas.append("- ${it.nombre}\n")
        }
        dialogoInfoRuta.parada_ruta_publica_valor.text = paradas
        if (rutaSeleccionada.urlFoto != "")
            Picasso.get().load(rutaSeleccionada.urlFoto).centerCrop().fit().into(dialogoInfoRuta.imagen_ruta_publica)
    }
}
