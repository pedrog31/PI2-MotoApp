package co.edu.udea.motoapp.ui.inicio

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.ui.perfil.FragmentoPerfil


class FragmentInicio : Fragment() {

    companion object {
        fun nuevaInstancia() = FragmentInicio()
    }

    private lateinit var viewModel: ModeloVistaInicio

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ModeloVistaInicio::class.java)
    }
}
