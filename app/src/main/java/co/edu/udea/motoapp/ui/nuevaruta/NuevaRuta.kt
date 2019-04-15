package co.edu.udea.motoapp.ui.nuevaruta

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.edu.udea.motoapp.R

class NuevaRuta : Fragment() {

    companion object {
        fun newInstance() = NuevaRuta()
    }

    private lateinit var viewModel: ModeloVistaNuevaRuta

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragmento_nueva_ruta, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ModeloVistaNuevaRuta::class.java)
        // TODO: Use the ViewModel
    }

}
