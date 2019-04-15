package co.edu.udea.motoapp.ui.perfil

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.actividad.Autenticacion
import co.edu.udea.motoapp.actividad.Registro
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.util.TransformacionImagen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragmento_perfil.*

class Perfil : Fragment() {

    private lateinit var modeloVistaMotero: ModeloVistaMotero

    private val observadorPerfil = Observer<Motero> { motero ->
        when {
            motero == null -> manejarUsuarioNoRegistrado()
            motero.correo == "" -> manejarUsuarioNoAutenticado()
            else -> actualizarVistaPerfil(motero)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragmento_perfil, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            modeloVistaMotero = ViewModelProviders.of(it).get(ModeloVistaMotero::class.java)
            modeloVistaMotero.motero.observe(it, observadorPerfil)
            modeloVistaMotero.buscarMotero()
        }
    }

    private fun actualizarVistaPerfil(motero: Motero) {
        valor_texto_nombre.text = motero.nombre
        valor_texto_celular.text = motero.celular
        valor_texto_ciudad.text = motero.ciudad
        valor_texto_correo.text = motero.correo
        if (modeloVistaMotero.moteroFirebase.photoUrl != null) {
            Picasso.get()
                .load(modeloVistaMotero.moteroFirebase.photoUrl)
                .centerCrop()
                .transform(TransformacionImagen(100, 0))
                .fit()
                .into(imagen)
        }
    }

    private fun manejarUsuarioNoAutenticado() {
        Toast.makeText(this@Perfil.context, "Usuario no inicio sesion", Toast.LENGTH_LONG).show()
        startActivity(Intent(this@Perfil.context, Autenticacion::class.java))
    }

    private fun manejarUsuarioNoRegistrado() {
        Toast.makeText(this@Perfil.context, "Usuario no registrado", Toast.LENGTH_LONG).show()
        startActivity(Intent(this@Perfil.context, Registro::class.java))
    }
}
