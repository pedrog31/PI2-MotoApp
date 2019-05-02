package co.edu.udea.motoapp.ui.rutas_privadas

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.actividad.RutaIniciada
import co.edu.udea.motoapp.modelo.RutaPrivada
import co.edu.udea.motoapp.util.TransformacionImagen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragmento_lista_rutas_privadas.view.*
import kotlinx.android.synthetic.main.tarjeta_invitacion_ruta.view.*
import kotlinx.android.synthetic.main.tarjeta_ruta_privada.view.*
import kotlinx.android.synthetic.main.tarjeta_ruta_privada.view.texto_estado_ruta_privada

class AdaptadorRuta (
    private val mapRutasPrivadas: HashMap<String, RutaPrivada>,
    private val contexto: FragmentActivity,
    private val tipo: Int) : RecyclerView.Adapter<AdaptadorRuta.RutaViewHolder>()  {

    inner class RutaViewHolder(val vistaTarjetaRuta: View) : RecyclerView.ViewHolder(vistaTarjetaRuta)
    private val keysRutasPrivadas: MutableSet<String> = mapRutasPrivadas.keys
    private val rutasPrivadas: MutableCollection<RutaPrivada> = mapRutasPrivadas.values
    val moteroId = FirebaseAuth.getInstance().uid


    val repositorioInvitacionRutas = FirebaseDatabase.getInstance().reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutaViewHolder {
        val view: View
        when(tipo){
            0->view=LayoutInflater.from(parent.context).inflate(R.layout.tarjeta_invitacion_ruta, parent, false)
            1->view=LayoutInflater.from(parent.context).inflate(R.layout.tarjeta_ruta_privada, parent, false)
            else->view= LayoutInflater.from(parent.context).inflate(R.layout.tarjeta_ruta_privada, parent, false)
        }
        return RutaViewHolder(view)
    }

    override fun onBindViewHolder(rutaViewHolder: RutaViewHolder, posicion: Int) {
        val keyRutaPrivada = keysRutasPrivadas.elementAt(posicion)
        val rutaPrivada = rutasPrivadas.elementAt(posicion)
        val recursos = this.contexto.resources
        when(tipo){
            0->{
                rutaViewHolder.vistaTarjetaRuta.texto_nombre_invitacion_ruta.text = rutaPrivada.nombre
                rutaViewHolder.vistaTarjetaRuta.texto_estado_invitacion_ruta.text =  recursos.getString(R.string.texto_estado_ruta_privada, rutaPrivada.estado)
                if (rutaPrivada.urlFoto != "" && rutaPrivada.urlFoto != "null")
                    Picasso.get()
                        .load(rutaPrivada.urlFoto)
                        .centerCrop()
                        .transform(TransformacionImagen(100, 0))
                        .fit()
                        .into(rutaViewHolder.vistaTarjetaRuta.imagen_invitacion_ruta)
                if(rutaPrivada.integrantes.containsKey(moteroId)){
                    rutaViewHolder.vistaTarjetaRuta.boton_aceptar_ruta.isClickable=false
                    rutaViewHolder.vistaTarjetaRuta.boton_aceptar_ruta.visibility=View.INVISIBLE
                    rutaViewHolder.vistaTarjetaRuta.boton_rechazar_ruta.isClickable=false
                    rutaViewHolder.vistaTarjetaRuta.boton_rechazar_ruta.visibility=View.INVISIBLE
                    rutaViewHolder.itemView.setOnClickListener{
                        val rutaIniciadaIntent = Intent(contexto, RutaIniciada::class.java)
                        rutaIniciadaIntent.putExtra("ruta", rutaPrivada)
                        rutaIniciadaIntent.putExtra("key_ruta", keyRutaPrivada)
                        contexto.startActivity(rutaIniciadaIntent)
                    }
                }

                rutaViewHolder.vistaTarjetaRuta.boton_aceptar_ruta.setOnClickListener {
                    repositorioInvitacionRutas.child("moteros/$moteroId/invitacionRuta/$keyRutaPrivada").setValue("true")
                        .addOnSuccessListener {
                            rutaViewHolder.vistaTarjetaRuta.boton_aceptar_ruta.isClickable=false
                            rutaViewHolder.vistaTarjetaRuta.boton_aceptar_ruta.visibility=View.INVISIBLE
                            rutaViewHolder.vistaTarjetaRuta.boton_rechazar_ruta.isClickable=false
                            rutaViewHolder.vistaTarjetaRuta.boton_rechazar_ruta.visibility=View.INVISIBLE
                            val onje = hashMapOf("inicioRuta" to false, "latitud" to 0, "longitud" to 0)
                            repositorioInvitacionRutas.child("rutasPrivadas").child(keyRutaPrivada).child("integrantes").child(moteroId!!).setValue(onje)
                            notifyDataSetChanged()
                        }
                        .addOnFailureListener{

                        }

                }
                rutaViewHolder.vistaTarjetaRuta.boton_rechazar_ruta.setOnClickListener {
                    repositorioInvitacionRutas.child("moteros/$moteroId/invitacionRuta/$keyRutaPrivada").removeValue()
                    mapRutasPrivadas.remove(keyRutaPrivada)
                    notifyDataSetChanged()
                }
            }
            1->{
                rutaViewHolder.vistaTarjetaRuta.texto_nombre_ruta_privada.text =  rutaPrivada.nombre
                rutaViewHolder.vistaTarjetaRuta.texto_dificultad_ruta_privada.text =  recursos.getString(R.string.dificultad_ruta, rutaPrivada.nivelDificultad)
                rutaViewHolder.vistaTarjetaRuta.texto_experiencia_ruta_privada.text =  recursos.getString(R.string.experiencia_ruta, rutaPrivada.experiencia)
                rutaViewHolder.vistaTarjetaRuta.texto_distancia_ruta_privada.text =  recursos.getString(R.string.distancia_ruta, rutaPrivada.distancia)
                rutaViewHolder.vistaTarjetaRuta.texto_calificacion_ruta_privada.text =  recursos.getString(R.string.calificacion_ruta, String.format("%.02f", rutaPrivada.calificacion))
                rutaViewHolder.vistaTarjetaRuta.texto_estado_ruta_privada.text =  recursos.getString(R.string.texto_estado_ruta_privada, rutaPrivada.estado)
                if (rutaPrivada.urlFoto != "" && rutaPrivada.urlFoto != "null")
                    Picasso.get()
                        .load(rutaPrivada.urlFoto)
                        .centerCrop()
                        .transform(TransformacionImagen(40, 0))
                        .fit()
                        .into(rutaViewHolder.vistaTarjetaRuta.imagen_ruta_privada)
                rutaViewHolder.itemView.setOnClickListener { _ ->
                    val rutaIniciadaIntent = Intent(contexto, RutaIniciada::class.java)
                    rutaIniciadaIntent.putExtra("ruta", rutaPrivada)
                    rutaIniciadaIntent.putExtra("key_ruta", keyRutaPrivada)
                    contexto.startActivity(rutaIniciadaIntent)
                }
            }
        }
    }

    override fun getItemCount(): Int = mapRutasPrivadas.size
}
