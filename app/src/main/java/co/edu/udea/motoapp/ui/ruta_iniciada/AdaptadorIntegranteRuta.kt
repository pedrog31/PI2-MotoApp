package co.edu.udea.motoapp.ui.ruta_iniciada

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import co.edu.udea.motoapp.R

class AdaptadorIntegranteRuta(private val contexto: FragmentActivity):
    RecyclerView.Adapter<AdaptadorIntegranteRuta.IntegranteRutaViewHolder>() {

    val modeloVistaRutaIniciada = ViewModelProviders.of(contexto).get(ModeloVistaRutaIniciada::class.java)
    inner class IntegranteRutaViewHolder(val vistaTarjetaIntegranteRuta: View) :
        RecyclerView.ViewHolder(vistaTarjetaIntegranteRuta)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntegranteRutaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tarjeta_integrante_ruta, parent, false)
        return IntegranteRutaViewHolder(view)
    }

    override fun onBindViewHolder(holder: IntegranteRutaViewHolder, position: Int) {

    }

    override fun getItemCount() = modeloVistaRutaIniciada.listaIntegrantesRuta.value?.size ?: 0
}