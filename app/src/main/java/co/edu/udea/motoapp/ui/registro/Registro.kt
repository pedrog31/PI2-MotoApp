package co.edu.udea.motoapp.ui.registro

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.actividad.Autenticacion
import co.edu.udea.motoapp.actividad.Principal
import kotlinx.android.synthetic.main.fragmento_registro.*
import com.firebase.ui.auth.AuthUI



class Registro : Fragment() {

    private lateinit var modeloVistaRegistro: ModeloVistaRegistro

    private val observadorEstadoRegistro = Observer<String> { estado ->
        estado?.let {
            if (it == "ok")
                this.completarRegistroExitoso()
            else
                this.manejarErrorRegistro(it)
        }
    }

    private fun manejarErrorRegistro(estado: String) {
        this.mostrarMensaje(estado)
        Log.e(Registro::class.java.name, estado)
        AuthUI.getInstance()
            .signOut(this@Registro.requireContext())
            .addOnCompleteListener {
                startActivity(Intent(this@Registro.context, Autenticacion::class.java))
            }
    }

    companion object {
        fun newInstance() = Registro()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragmento_registro, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        modeloVistaRegistro = ViewModelProviders.of(this).get(ModeloVistaRegistro::class.java)
        llenarFormularioRegistro()
        completar_registro.setOnClickListener {
            this.guardarInformacionMotero()
        }
        registro_ciudad.setOnKeyListener { vista, codigoTecla, evento ->
            if (evento.getAction() == KeyEvent.ACTION_DOWN && codigoTecla == KeyEvent.KEYCODE_ENTER)
                this.guardarInformacionMotero()
            true
        }
        modeloVistaRegistro.estadoRegistro.observe(this@Registro, this.observadorEstadoRegistro)
    }

    private fun llenarFormularioRegistro() {
        registro_nombre.setText(modeloVistaRegistro.moteroActual.displayName)
        registro_correo.setText(modeloVistaRegistro.moteroActual.email)
        registro_celular.setText(modeloVistaRegistro.moteroActual.phoneNumber)
    }

    private fun guardarInformacionMotero() {
        modeloVistaRegistro.guardarInformacionMotero (
            registro_nombre.text.toString(),
            registro_correo.text.toString(),
            registro_ciudad.text.toString(),
            registro_celular.text.toString())
    }

    private fun completarRegistroExitoso() {
        this.mostrarMensaje("Registro exitoso")
        startActivity(Intent(this@Registro.context, Principal::class.java))
        activity?.finish()
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this@Registro.context, mensaje, Toast.LENGTH_LONG).show()
    }

}
