package co.edu.udea.motoapp.ui.registro

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.actividad.ActividadPrincipal
import kotlinx.android.synthetic.main.fragmento_registro.*

class FragmentoRegistro : Fragment() {

    private lateinit var viewModel: ModeloVistaRegistro

    private val observadorEstadoRegistro = Observer<Int> { estado ->
        estado?.let {
            if (estado == 0)
                this.completarRegistroExitoso()
        }
    }

    companion object {
        fun newInstance() = FragmentoRegistro()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragmento_registro, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ModeloVistaRegistro::class.java)
        llenarFormularioRegistro()
        completar_registro.setOnClickListener {
            this.guardarInformacionMotero()
        }
        viewModel.estadoRegistro.observe(this@FragmentoRegistro, this.observadorEstadoRegistro)
    }

    private fun llenarFormularioRegistro() {
        registro_nombre.setText(viewModel.moteroActual.displayName)
        registro_correo.setText(viewModel.moteroActual.email)
        registro_celular.setText(viewModel.moteroActual.phoneNumber)
    }

    private fun guardarInformacionMotero() {
        viewModel.guardarInformacionMotero (
            registro_nombre.text.toString(),
            registro_correo.text.toString(),
            registro_celular.text.toString(),
            registro_ciudad.text.toString())
    }

    private fun completarRegistroExitoso() {
        this.mostrarMensaje("Registro exitoso")
        startActivity(Intent(this@FragmentoRegistro.context, ActividadPrincipal::class.java))
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this@FragmentoRegistro.context, mensaje, Toast.LENGTH_LONG).show()
    }

}
