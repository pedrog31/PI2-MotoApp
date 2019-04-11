package co.edu.udea.motoapp.ui.perfil

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.actividad.Autenticacion
import co.edu.udea.motoapp.actividad.Registro
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.util.TransformacionImagen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragmento_perfil.*



class Perfil : Fragment() {

    lateinit var motero: Motero
    val moteroFirebase = FirebaseAuth.getInstance().currentUser

    val escuchadorMotero = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {

        }

        override fun onDataChange(resultado: DataSnapshot) {
            val motero = resultado.getValue(Motero::class.java)
            if (motero == null) {
                manejarUsuarioNoRegistrado()
            } else {
                this@Perfil.motero = motero
                actualizarVistaPerfil()
            }
        }
    }

    private fun manejarUsuarioNoRegistrado() {
        Toast.makeText(this@Perfil.context, "Usuario no registrado", Toast.LENGTH_LONG).show()
        startActivity(Intent(this@Perfil.context, Registro::class.java))
    }

    private fun actualizarVistaPerfil() {
        valor_texto_nombre.setText(motero.nombre)
        valor_texto_celular.setText(motero.celular)
        valor_texto_ciudad.setText(motero.ciudad)
        valor_texto_correo.setText(motero.correo)
        if (moteroFirebase != null && moteroFirebase.photoUrl != null) {
            Picasso.get()
                .load(moteroFirebase.photoUrl)
                .centerCrop()
                .transform(TransformacionImagen(100, 0))
                .fit()
                .into(imagen)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmento_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val identificadorUsuarioFirebase = FirebaseAuth.getInstance().uid
        if (identificadorUsuarioFirebase == null)
            manejarUsuarioNoAutenticado()
        else
            FirebaseDatabase
                .getInstance()
                .reference
                .child("moteros")
                .child(identificadorUsuarioFirebase)
                .addListenerForSingleValueEvent(escuchadorMotero)
    }

    private fun manejarUsuarioNoAutenticado() {
        Toast.makeText(this@Perfil.context, "Usuario no inicio sesion", Toast.LENGTH_LONG).show()
        startActivity(Intent(this@Perfil.context, Autenticacion::class.java))
    }
}
