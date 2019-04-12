package co.edu.udea.motoapp.ui.lista_amigos

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.ui.nuevoamigo.ModeloVistaNuevoAmigo
import co.edu.udea.motoapp.util.TransformacionImagen
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tarjeta_amigo_motero.view.*

class AdaptadorAmigo(
    private val mapAmigosMoteros: HashMap<String, Motero>,
    private val contexto: FragmentActivity,
    val permiteAgregar: Boolean
):
    RecyclerView.Adapter<AdaptadorAmigo.AmigoViewHolder>() {

    inner class AmigoViewHolder(val vistaTarjetaMotero: View) : RecyclerView.ViewHolder(vistaTarjetaMotero)
    private val keysAmigosMoteros: MutableSet<String> = mapAmigosMoteros.keys
    private val amigosMoteros: MutableCollection<Motero> = mapAmigosMoteros.values
    private val modeloVistaNuevoAmigo: ModeloVistaNuevoAmigo = ViewModelProviders.of(contexto).get(ModeloVistaNuevoAmigo::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmigoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tarjeta_amigo_motero, parent, false)
        return AmigoViewHolder(view)
    }

    override fun onBindViewHolder(amigoViewHolder: AmigoViewHolder, posicion: Int) {
        val amigoMotero = amigosMoteros.elementAt(posicion)
        val recursos = this.contexto.resources
        amigoViewHolder.vistaTarjetaMotero.texto_nombre_amigo.text =  recursos.getString(R.string.amigo_nombre, amigoMotero.nombre)
        amigoViewHolder.vistaTarjetaMotero.texto_celular_amigo.text = recursos.getString(R.string.amigo_celular, amigoMotero.celular)
        amigoViewHolder.vistaTarjetaMotero.texto_correo_amigo.text = recursos.getString(R.string.amigo_correo, amigoMotero.correo)
        amigoViewHolder.vistaTarjetaMotero.texto_ciudad_amigo.text = recursos.getString(R.string.amigo_ciudad, amigoMotero.ciudad)
        if (amigoMotero.urlFoto != "null")
            Picasso.get()
                .load(amigoMotero.urlFoto)
                .centerCrop()
                .transform(TransformacionImagen(100, 0))
                .fit()
                .into(amigoViewHolder.vistaTarjetaMotero.imagen_amigo)
        if (permiteAgregar) {
            amigoViewHolder.vistaTarjetaMotero.boton_agregar_amigo.visibility = View.VISIBLE
            amigoViewHolder.vistaTarjetaMotero.boton_agregar_amigo.setOnClickListener {
                modeloVistaNuevoAmigo.agregarAmigo(keysAmigosMoteros.elementAt(posicion), contexto)
            }
        }
    }

    override fun getItemCount(): Int = mapAmigosMoteros.size
}
