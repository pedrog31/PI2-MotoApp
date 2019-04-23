package co.edu.udea.motoapp.ui.ruta_iniciada

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.modelo.RutaPrivada


class RutaIniciada : Fragment() {
    var rutaActual: RutaPrivada? = null

    companion object {
        fun newInstance() = RutaIniciada()
    }

    private lateinit var viewModel: ModeloVistaRutaIniciada

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val data = this.activity!!.intent.extras
        rutaActual = data.getParcelable<RutaPrivada>("ruta")
        return inflater.inflate(R.layout.fragmento_ruta_iniciada, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ModeloVistaRutaIniciada::class.java)
        // TODO: Use the ViewModel
    }

}
