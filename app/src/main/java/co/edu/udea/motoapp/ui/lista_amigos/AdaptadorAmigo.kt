package co.edu.udea.motoapp.ui.lista_amigos

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.util.TransformacionImagen
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragmento_amigo.view.*

class AdaptadorAmigo(private val amigosMoteros: MutableCollection<Motero>, private val contexto: Context):
    RecyclerView.Adapter<AdaptadorAmigo.AmigoViewHolder>() {

    inner class AmigoViewHolder(val vistaFragmentoAmigo: View) : RecyclerView.ViewHolder(vistaFragmentoAmigo)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmigoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragmento_amigo, parent, false)
        return AmigoViewHolder(view)
    }

    override fun onBindViewHolder(amigoViewHolder: AmigoViewHolder, posicion: Int) {
        val amigoMotero = amigosMoteros.elementAt(posicion)
        val recursos = this.contexto.resources
        amigoViewHolder.vistaFragmentoAmigo.texto_nombre_amigo.text =  recursos.getString(R.string.amigo_nombre, amigoMotero.nombre)
        amigoViewHolder.vistaFragmentoAmigo.texto_celular_amigo.text = recursos.getString(R.string.amigo_celular, amigoMotero.celular)
        amigoViewHolder.vistaFragmentoAmigo.texto_correo_amigo.text = recursos.getString(R.string.amigo_correo, amigoMotero.correo)
        amigoViewHolder.vistaFragmentoAmigo.texto_ciudad_amigo.text = recursos.getString(R.string.amigo_ciudad, amigoMotero.ciudad)
        if (amigoMotero.urlFoto != "null")
            Picasso.get()
                .load(amigoMotero.urlFoto)
                .centerCrop()
                .transform(TransformacionImagen(100, 0))
                .fit()
                .into(amigoViewHolder.vistaFragmentoAmigo.imagen_amigo)
        with(amigoViewHolder.vistaFragmentoAmigo) {
            tag = amigoMotero
        }
    }

    override fun getItemCount(): Int = amigosMoteros.size
}
