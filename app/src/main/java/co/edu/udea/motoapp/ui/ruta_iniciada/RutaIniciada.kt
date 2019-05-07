package co.edu.udea.motoapp.ui.ruta_iniciada

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.util.TransformacionImagen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragmento_ruta_iniciada.*


class RutaIniciada : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = RutaIniciada()
    }

    private lateinit var mapaRutaGoogle: GoogleMap
    private lateinit var modeloVistaRutaIniciada: ModeloVistaRutaIniciada

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragmento_ruta_iniciada, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let { actividadRutaIniciada ->
            modeloVistaRutaIniciada =
                ViewModelProviders.of(actividadRutaIniciada).get(ModeloVistaRutaIniciada::class.java)
            modeloVistaRutaIniciada.rutaActual?.let {
                this.manejarAccionesRuta()
                actividadRutaIniciada.title = getString(R.string.titulo_actividad_ruta_iniciada, it.nombre)
                texto_descripcion_ruta.text = it.descripcion
                if (it.urlFoto != "")
                    Picasso.get()
                        .load(it.urlFoto)
                        .centerCrop()
                        .transform(TransformacionImagen(40, 0))
                        .fit()
                        .into(imagen_ruta)
            }
            modeloVistaRutaIniciada.estadoRuta.observeForever {
                manejarAccionesRuta()
            }
        }
    }

    private fun manejarAccionesRuta() {
        if (this.modeloVistaRutaIniciada.rutaActual!!.esPropietario()) {
            if (modeloVistaRutaIniciada.rutaActual!!.estadoCreada()) {
                boton_iniciar_ruta.visibility = View.VISIBLE
                boton_eliminar_ruta.visibility = View.VISIBLE
                boton_iniciar_ruta.setOnClickListener {
                    boton_iniciar_ruta.visibility = View.GONE
                    boton_eliminar_ruta.visibility = View.GONE
                    iniciarRuta()
                }
                boton_eliminar_ruta.setOnClickListener {
                    this@RutaIniciada.activity?.let { it1 -> modeloVistaRutaIniciada.eliminarRuta(it1) }
                }
            } else if (this.modeloVistaRutaIniciada.rutaActual!!.estadoIniciada()) {
                boton_finalizar_ruta.visibility = View.VISIBLE
                boton_finalizar_ruta.setOnClickListener {
                    boton_finalizar_ruta.visibility = View.GONE
                    finalizarRuta()
                }
            }
        } else  {
            if (modeloVistaRutaIniciada.rutaActual!!.estadoCreada())
                boton_eliminar_ruta_invitado.visibility = View.VISIBLE
            if (modeloVistaRutaIniciada.rutaActual!!.estadoIniciada())
                boton_iniciar_ruta_invitado.visibility = View.VISIBLE
            boton_iniciar_ruta.setOnClickListener {
                iniciarRutaIntegrante()
                boton_eliminar_ruta_invitado.visibility = View.GONE
                boton_iniciar_ruta_invitado.visibility = View.GONE
            }
            boton_eliminar_ruta_invitado.setOnClickListener {
                this@RutaIniciada.activity?.let { it1 -> modeloVistaRutaIniciada.eliminarRutaInvitada(it1) }
                boton_eliminar_ruta_invitado.visibility = View.GONE
                boton_iniciar_ruta_invitado.visibility = View.GONE
            }
        }
        boton_mapa_ruta.setOnClickListener {
            mostrarMapaRuta()
            (childFragmentManager.findFragmentById(R.id.mapa_rutas_privadas) as? SupportMapFragment)?.getMapAsync(
                this
            )
        }
    }

    private fun finalizarRuta() {
        this.activity?.let {
            this.modeloVistaRutaIniciada.finalizarRuta(it)
        }
    }

    private fun iniciarRuta() {
        this.activity?.let {
            this.modeloVistaRutaIniciada.iniciarRuta(it)
            iniciarRutaIntegrante()
        }
    }

    private fun iniciarRutaIntegrante() {
        this.activity?.let {
            this.modeloVistaRutaIniciada.iniciarRutaIntegrante(it)
        }
    }


    private fun mostrarMapaRuta() {
        boton_mapa_ruta.setOnClickListener { ocultarMapaRuta() }
        acordeon_informacion_ruta.collapse()
        acordeon_mapa_ruta.expand()
        boton_mapa_ruta.text = getString(R.string.boton_mapa_ruta_ocultar)
    }

    private fun ocultarMapaRuta() {
        boton_mapa_ruta.setOnClickListener { mostrarMapaRuta() }
        acordeon_informacion_ruta.expand()
        acordeon_mapa_ruta.collapse()
        boton_mapa_ruta.text = getString(R.string.boton_mapa_ruta_mostrar)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(mapaRutaGoogle: GoogleMap) {
        this.mapaRutaGoogle = mapaRutaGoogle
        this.mapaRutaGoogle.isMyLocationEnabled = true
        anadirMarcadoresParadas()
    }

    private fun anadirMarcadoresParadas() {
        val listaParadas = modeloVistaRutaIniciada.rutaActual?.paradas ?: return
        mapaRutaGoogle.animateCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(listaParadas[0].latitud, listaParadas[0].longitud), 6F)
        )
        listaParadas.forEachIndexed { posicion, paradaRuta ->
            this.mapaRutaGoogle.addMarker(
                MarkerOptions()
                    .position(LatLng(paradaRuta.latitud, paradaRuta.longitud))
                    .title(paradaRuta.nombre)
                    .snippet(
                        when (posicion) {
                            0 -> "Inicio"
                            listaParadas.size.minus(1) -> "Final"
                            else -> "Parada $posicion"
                        }
                    )
            )
        }
    }
}
