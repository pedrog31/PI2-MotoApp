package co.edu.udea.motoapp.ui.ruta_iniciada

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.util.TransformacionImagen
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragmento_ruta_iniciada.*
import kotlinx.android.synthetic.main.tarjeta_amigo_motero.view.*


class RutaIniciada : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = RutaIniciada()
    }

    private lateinit var mapaRutasPublicas: GoogleMap
    private lateinit var modeloVistaRutaIniciada: ModeloVistaRutaIniciada

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragmento_ruta_iniciada, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let { actividadRutaIniciada ->
            modeloVistaRutaIniciada = ViewModelProviders.of(actividadRutaIniciada).get(ModeloVistaRutaIniciada::class.java)
            modeloVistaRutaIniciada.rutaActual.value?.let {
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
                }
            }
        }
    }

    private fun mostrarMapaRuta() {
        acordeon_mapa_ruta.toggle()
        acordeon_informacion_ruta.toggle()
        if(acordeon_informacion_ruta.isExpanded)
            boton_mapa_ruta.text = getString(R.string.boton_mapa_ruta_ocultar)
        else
            boton_mapa_ruta.text = getString(R.string.boton_mapa_ruta_mostrar)
    }

    override fun onMapReady(p0: GoogleMap?) {

    }

}
