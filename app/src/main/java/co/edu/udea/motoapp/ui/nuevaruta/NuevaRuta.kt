package co.edu.udea.motoapp.ui.nuevaruta

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.excepcion.ExcepcionAutenticacion
import co.edu.udea.motoapp.modelo.ParadaRuta
import co.edu.udea.motoapp.modelo.RutaPrivada
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener
import kotlinx.android.synthetic.main.fragmento_nueva_ruta.*
import java.util.*


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
        val geocoder = Geocoder(context, Locale.getDefault())
        var address: List<Address>?
        val breakpoints = mutableListOf<ParadaRuta>()
        val moteroActual = FirebaseAuth.getInstance().currentUser ?: throw ExcepcionAutenticacion()
        val repositorioRutasPrivadas = FirebaseDatabase.getInstance().getReference("rutasPrivadas")

        boton_agregar_ruta.setOnClickListener {
            val ruta = RutaPrivada(
                descripcion_ruta.text.toString(),
                0,
                0,
                "Dificil",
                nombre_ruta.text.toString(),
                "", mutableListOf(),
                0.0F,
                0,
                moteroActual.uid,
                hashMapOf()
            )
            val key = repositorioRutasPrivadas.push().getKey()

            repositorioRutasPrivadas.child(key.toString())
                .setValue(
                    ruta
                )
                .addOnSuccessListener {
                    this.activity?.finish()
                }
                .addOnFailureListener {

                }
        }

        places_autocomplete.setOnPlaceSelectedListener(
            object : OnPlaceSelectedListener {
                override fun onPlaceSelected(p0: com.seatgeek.placesautocomplete.model.Place) {
                    address = geocoder.getFromLocationName(p0.description, 5)
                    val location = (address as MutableList<Address>?)?.get(0)
                    breakpoints.add(0, ParadaRuta(p0.description, location!!.latitude, location.longitude))
                    Log.d("mensaje", location.latitude.toString())
                    Log.d("mensaje", location.longitude.toString())
                    Log.d("mensaje", p0.description)
                    Log.d("mensaje", p0.matched_substrings.toString())
                    Log.d("mensaje", p0.place_id)


                }
            }
        )

        places_autocomplete2.setOnPlaceSelectedListener(
            object : OnPlaceSelectedListener {
                override fun onPlaceSelected(p0: com.seatgeek.placesautocomplete.model.Place) {
                    address = geocoder.getFromLocationName(p0.description, 5)
                    val location = (address as MutableList<Address>?)?.get(0)
                    breakpoints.add(1, ParadaRuta(p0.description, location!!.latitude, location.longitude))
                    Log.d("mensaje", location.latitude.toString())
                    Log.d("mensaje", location.longitude.toString())
                    Log.d("mensaje", p0.description)
                    Log.d("mensaje", p0.matched_substrings.toString())
                    Log.d("mensaje", p0.place_id)
                }
            }
        )

        viewModel = ViewModelProviders.of(this).get(ModeloVistaNuevaRuta::class.java)
        // TODO: Use the ViewModel
    }
}
