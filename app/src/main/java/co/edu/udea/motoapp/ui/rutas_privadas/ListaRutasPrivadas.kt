package co.edu.udea.motoapp.ui.rutas_privadas

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.edu.udea.motoapp.R


class ListaRutasPrivadas : Fragment() {

    companion object {
        fun nuevaInstancia() = ListaRutasPrivadas()
    }

    private lateinit var viewModel: ModeloVistaListaRutasPrivadas

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmento_lista_rutas_privadas, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ModeloVistaListaRutasPrivadas::class.java)
        // TODO: Use the ViewModel
    }

}
