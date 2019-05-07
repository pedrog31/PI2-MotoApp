package co.edu.udea.motoapp.ui.nuevaruta

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.ui.lista_amigos.AdaptadorAmigo
import kotlinx.android.synthetic.main.tarjeta_amigo_checkbox.view.*
import kotlinx.android.synthetic.main.tarjeta_amigo_motero.view.*

class AdaptadorAmigos(private val mapAmigosMoteros: HashMap<String, Motero>,
                      private val amigosList: HashMap<String,HashMap<String,Any>>,
                      private val contexto: FragmentActivity):
    RecyclerView.Adapter<AdaptadorAmigos.AmigoViewHolder>() {


    inner class AmigoViewHolder(val vistaTarjetaAmigoCheckbox: View) : RecyclerView.ViewHolder(vistaTarjetaAmigoCheckbox)
    private val keysAmigosMoteros: MutableSet<String> = mapAmigosMoteros.keys
    private val amigosMoteros: MutableCollection<Motero> = mapAmigosMoteros.values

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmigoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tarjeta_amigo_checkbox, parent, false)
        return AmigoViewHolder(view)
    }

    override fun getItemCount(): Int = mapAmigosMoteros.size

    override fun onBindViewHolder(holder: AmigoViewHolder, posicion: Int) {
        val amigoMotero = amigosMoteros.elementAt(posicion)
        holder.vistaTarjetaAmigoCheckbox.checkBox_amigo.text =  amigoMotero.nombre
        if(amigosList.contains(keysAmigosMoteros.elementAt(posicion))){
            holder.vistaTarjetaAmigoCheckbox.checkBox_amigo.toggle()
        }
        holder.vistaTarjetaAmigoCheckbox.checkBox_amigo.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                val readWriteMap = hashMapOf("conectado" to false, "latitud" to 0, "longitud" to 0)
                amigosList.put(keysAmigosMoteros.elementAt(posicion),readWriteMap)
            }else{
                amigosList.remove(keysAmigosMoteros.elementAt(posicion))
            }
        }
    }


}