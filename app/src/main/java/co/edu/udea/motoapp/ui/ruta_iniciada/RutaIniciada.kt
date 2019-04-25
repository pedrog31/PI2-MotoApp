package co.edu.udea.motoapp.ui.ruta_iniciada

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.modelo.IntegranteRuta
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.util.TransformacionImagen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragmento_ruta_iniciada.*
import kotlinx.android.synthetic.main.tarjeta_estado_integrantes_ruta.*


class RutaIniciada : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = RutaIniciada()
    }

    private lateinit var mapaRutaGoogle: GoogleMap
    private lateinit var modeloVistaRutaIniciada: ModeloVistaRutaIniciada
    private lateinit var adaptadorIntegranteRuta: AdaptadorIntegranteRuta
    private val observadorListaMoteroIntegrantesRuta = Observer<HashMap<String, Motero>> { _ ->
            adaptadorIntegranteRuta.notifyDataSetChanged()
    }
    private val observadorListaIntegrantesRuta = Observer<HashMap<String, IntegranteRuta>> { _ ->
            adaptadorIntegranteRuta.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragmento_ruta_iniciada, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let { actividadRutaIniciada ->
            modeloVistaRutaIniciada =
                ViewModelProviders.of(actividadRutaIniciada).get(ModeloVistaRutaIniciada::class.java)
            modeloVistaRutaIniciada.rutaActual?.let {
                actividadRutaIniciada.title = getString(R.string.titulo_actividad_ruta_iniciada, it.nombre)
                texto_descripcion_ruta.text = it.descripcion
                Picasso.get()
                    .load(it.urlFoto)
                    .centerCrop()
                    .transform(TransformacionImagen(40, 0))
                    .fit()
                    .into(imagen_ruta)
                boton_mapa_ruta.setOnClickListener {
                    mostrarMapaRuta()
                    (childFragmentManager.findFragmentById(R.id.mapa_rutas_privadas) as? SupportMapFragment)?.getMapAsync(
                        this
                    )
                }
                boton_iniciar_ruta.setOnClickListener {
                    alertarIntegrantesInicioRuta()
                }
                boton_eliminar_ruta.setOnClickListener {
                    modeloVistaRutaIniciada.eliminarRuta(this@RutaIniciada.requireContext())
                }
            }
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

    private fun alertarIntegrantesInicioRuta() {
        modeloVistaRutaIniciada.buscarIntegrantesRuta()
        this.mostrarDialogoEstadoRuta(
            AlertDialog
                .Builder(this.requireContext())
                .setView(R.layout.tarjeta_estado_integrantes_ruta)
                .setCancelable(false)
                .setPositiveButton(R.string.boton_iniciar_ruta, { _, _ ->
                    modeloVistaRutaIniciada.iniciarRuta(requireContext())
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
        )
    }

    private fun mostrarDialogoEstadoRuta(dialogoEstadoRuta: AlertDialog) {
        dialogoEstadoRuta.show()
        if (modeloVistaRutaIniciada.rutaActual == null) return
        adaptadorIntegranteRuta = AdaptadorIntegranteRuta(activity!!)
        modeloVistaRutaIniciada.listaMoterosIntegrantesRuta.observe(
            activity!!,
            observadorListaMoteroIntegrantesRuta
        )
        modeloVistaRutaIniciada.listaIntegrantesRuta.observe(
            activity!!,
            observadorListaIntegrantesRuta
        )
        dialogoEstadoRuta.indicador_cargando_ruta.animate()
        dialogoEstadoRuta.texto_mensaje_estado_ruta.text = getString(R.string.estado_ruta_mensaje_espera)
        dialogoEstadoRuta.lista_integrantes_ruta.layoutManager = LinearLayoutManager(activity!!)
        dialogoEstadoRuta.lista_integrantes_ruta.adapter = adaptadorIntegranteRuta
    }
}
