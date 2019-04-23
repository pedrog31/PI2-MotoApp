package co.edu.udea.motoapp.ui.rutas_privadas

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.actividad.MapaRutasPublicas
import co.edu.udea.motoapp.actividad.RutaIniciada
import co.edu.udea.motoapp.modelo.RutaPrivada
import co.edu.udea.motoapp.util.TransformacionImagen
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tarjeta_ruta_privada.view.*

class AdaptadorRuta (
    private val mapRutasPrivadas: HashMap<String, RutaPrivada>,
    private val contexto: FragmentActivity) : RecyclerView.Adapter<AdaptadorRuta.RutaViewHolder>()  {

    inner class RutaViewHolder(val vistaTarjetaRuta: View) : RecyclerView.ViewHolder(vistaTarjetaRuta)
    private val keysRutasPrivadas: MutableSet<String> = mapRutasPrivadas.keys
    private val rutasPrivadas: MutableCollection<RutaPrivada> = mapRutasPrivadas.values
    private val modeloVistaListaRutasPrivadas = ViewModelProviders.of(contexto).get(ModeloVistaListaRutasPrivadas::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tarjeta_ruta_privada, parent, false)
        return RutaViewHolder(view)
    }

    override fun onBindViewHolder(rutaViewHolder: RutaViewHolder, posicion: Int) {
        val rutaPrivada = rutasPrivadas.elementAt(posicion)
        val recursos = this.contexto.resources
        rutaViewHolder.vistaTarjetaRuta.texto_nombre_ruta_privada.text =  rutaPrivada.nombre
        rutaViewHolder.vistaTarjetaRuta.texto_dificultad_ruta_privada.text =  recursos.getString(R.string.dificultad_ruta, rutaPrivada.nivelDificultad)
        rutaViewHolder.vistaTarjetaRuta.texto_experiencia_ruta_privada.text =  recursos.getString(R.string.experiencia_ruta, rutaPrivada.experiencia)
        rutaViewHolder.vistaTarjetaRuta.texto_distancia_ruta_privada.text =  recursos.getString(R.string.distancia_ruta, rutaPrivada.distancia)
        rutaViewHolder.vistaTarjetaRuta.texto_calificacion_ruta_privada.text =  recursos.getString(R.string.calificacion_ruta, String.format("%.02f", rutaPrivada.calificacion))
        rutaViewHolder.vistaTarjetaRuta.texto_estado_ruta_privada.text =  recursos.getString(R.string.texto_estado_ruta_privada)
        if (rutaPrivada.urlFoto != "" && rutaPrivada.urlFoto != "null")
            Picasso.get()
                .load(rutaPrivada.urlFoto)
                .centerCrop()
                .transform(TransformacionImagen(40, 0))
                .fit()
                .into(rutaViewHolder.vistaTarjetaRuta.imagen_ruta_privada)

        rutaViewHolder.itemView.setOnClickListener { v ->
            val position = posicion
            val rutaIniciadaIntent = Intent(contexto, RutaIniciada::class.java)
            rutaIniciadaIntent.putExtra("ruta", rutaPrivada)
            contexto.startActivity(rutaIniciadaIntent)

        }
    }

    override fun getItemCount(): Int = mapRutasPrivadas.size
}
