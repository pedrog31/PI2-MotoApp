package co.edu.udea.motoapp.ui.ruta_iniciada

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.util.TransformacionImagen
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tarjeta_integrante_ruta.view.*

class AdaptadorIntegranteRuta(private val contexto: FragmentActivity):
    RecyclerView.Adapter<AdaptadorIntegranteRuta.IntegranteRutaViewHolder>() {

    val modeloVistaRutaIniciada = ViewModelProviders.of(contexto).get(ModeloVistaRutaIniciada::class.java)
    inner class IntegranteRutaViewHolder(val vistaTarjetaIntegranteRuta: View) :
        RecyclerView.ViewHolder(vistaTarjetaIntegranteRuta)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntegranteRutaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tarjeta_integrante_ruta, parent, false)
        return IntegranteRutaViewHolder(view)
    }

    override fun onBindViewHolder(integranteRutaViewHolder: IntegranteRutaViewHolder, position: Int) {
        val IntegranteRutaKey = modeloVistaRutaIniciada.listaIntegrantesRuta.value?.keys?.elementAt(position) ?: return
        val moteroIntegranteRuta = modeloVistaRutaIniciada.listaMoterosIntegrantesRuta.value?.get(IntegranteRutaKey) ?: return
        val integranteRuta = modeloVistaRutaIniciada.listaIntegrantesRuta.value?.get(IntegranteRutaKey) ?: return
        integranteRutaViewHolder.vistaTarjetaIntegranteRuta.texto_nombre_integrante_ruta.text = moteroIntegranteRuta.nombre
        if(moteroIntegranteRuta.urlFoto != "null")
            Picasso.get()
                .load(moteroIntegranteRuta.urlFoto)
                .centerCrop()
                .transform(TransformacionImagen(100, 0))
                .fit()
                .into(
                    integranteRutaViewHolder.vistaTarjetaIntegranteRuta.imagen_integrante_ruta
                )
        integranteRutaViewHolder.vistaTarjetaIntegranteRuta.boton_eliminar_integrante_ruta.setOnClickListener {
            modeloVistaRutaIniciada.eliminarIntegranteRuta(contexto, IntegranteRutaKey)
        }
        if(modeloVistaRutaIniciada.rutaActual!!.esPropietario())
            integranteRutaViewHolder.vistaTarjetaIntegranteRuta.boton_eliminar_integrante_ruta.visibility = View.VISIBLE
        if(integranteRuta.inicioRuta) {
            integranteRutaViewHolder.vistaTarjetaIntegranteRuta.boton_eliminar_integrante_ruta.visibility = View.GONE
            integranteRutaViewHolder.vistaTarjetaIntegranteRuta.imagen_integrante_iniciado.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        val count = modeloVistaRutaIniciada.listaIntegrantesRuta.value?.size ?: 0
        return count
    }
}