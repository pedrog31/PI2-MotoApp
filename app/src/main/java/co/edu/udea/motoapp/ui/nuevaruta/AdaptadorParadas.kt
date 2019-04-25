package co.edu.udea.motoapp.ui.nuevaruta

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.modelo.ParadaRuta
import co.edu.udea.motoapp.ui.lista_amigos.AdaptadorAmigo
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.tarjeta_parada_agregada.view.*

class AdaptadorParadas: RecyclerView.Adapter<AdaptadorParadas.ParadaViewHolder>(){



    var paradas: MutableList<ParadaRuta>  = ArrayList()
    lateinit var context:Context

    fun RecyclerAdapter(paradas : MutableList<ParadaRuta>, context: Context){
        this.paradas = paradas
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorParadas.ParadaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ParadaViewHolder(layoutInflater.inflate(R.layout.tarjeta_parada_agregada, parent, false))
    }

    override fun getItemCount(): Int {
        return paradas.size
    }

    override fun onBindViewHolder(holder: AdaptadorParadas.ParadaViewHolder, position: Int) {
        val item = paradas.get(position)
        holder.bind(item, context)
    }

    inner class ParadaViewHolder(val vistaTarjetaParada: View) : RecyclerView.ViewHolder(vistaTarjetaParada){
        val itemParada = vistaTarjetaParada.findViewById(R.id.textView_parada_item) as TextView
        fun bind(parada:ParadaRuta, context: Context) {
            itemParada.text = parada.nombre
            vistaTarjetaParada.boton_eliminar_parada.setOnClickListener(View.OnClickListener {
                paradas.remove(parada)
                notifyDataSetChanged()
            })


        }

    }
}





