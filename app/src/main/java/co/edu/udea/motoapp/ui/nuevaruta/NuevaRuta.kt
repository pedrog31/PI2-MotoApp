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
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.excepcion.ExcepcionAutenticacion
import co.edu.udea.motoapp.modelo.ParadaRuta
import co.edu.udea.motoapp.modelo.RutaPrivada
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView
import com.seatgeek.placesautocomplete.model.Place
import kotlinx.android.synthetic.main.fragmento_nueva_ruta.*
import java.util.*


class NuevaRuta : Fragment() {

    lateinit var mRecyclerView : RecyclerView
    val mAdapter : AdaptadorParadas = AdaptadorParadas()
    val breakpointsParadas = mutableListOf<ParadaRuta>()
    companion object {
        fun newInstance() = NuevaRuta()
    }

    private lateinit var viewModel: ModeloVistaNuevaRuta
    var destinoSeleccionado = false

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
        val repositorioRutasPrivadas = FirebaseDatabase.getInstance().reference
        setUpRecyclerView()



        boton_agregar_parada.setOnClickListener{
            this.agregarParada()
        }

        boton_agregar_ruta.setOnClickListener {
            var final =ParadaRuta()
            final=breakpoints.removeAt(1)
            breakpoints.addAll(breakpointsParadas)
            breakpoints.add(final)

            val ruta = RutaPrivada(
                descripcion_ruta.text.toString(),0,
                0,
                "Dificil",
                nombre_ruta.text.toString(),
                "", breakpoints,
                0.0F,
                0,
                moteroActual.uid,
                hashMapOf()
            )
            val key = repositorioRutasPrivadas.push().getKey()

            repositorioRutasPrivadas.child("rutasPrivadas").child(key.toString())
                .setValue(
                    ruta
                )
                .addOnSuccessListener {
                    repositorioRutasPrivadas.child("moteros").child(moteroActual.uid).child("rutas").child(key.toString()).setValue("true")
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

                    boton_agregar_parada.visibility=View.VISIBLE
                    textView_lugar_llegada.visibility=View.VISIBLE
                    places_autocomplete_lugar_llegada.visibility=View.VISIBLE

                }
            }
        )

        places_autocomplete_lugar_llegada.setOnPlaceSelectedListener(
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


        places_autocomplete_paradas.setOnPlaceSelectedListener(
            object : OnPlaceSelectedListener {
                override fun onPlaceSelected(p0: Place) {
                    address = geocoder.getFromLocationName(p0.description, 5)
                    val location = (address as MutableList<Address>?)?.get(0)
                    breakpointsParadas.add(ParadaRuta(p0.description, location!!.latitude, location.longitude))
                    mAdapter.notifyDataSetChanged()
                    places_autocomplete_paradas.setText("")
                    places_autocomplete_paradas.visibility=View.GONE
                    boton_agregar_parada.visibility=View.VISIBLE
                }
            }
        )
        places_autocomplete_paradas.setOnClearListener {
            boton_agregar_parada.visibility=View.VISIBLE
            places_autocomplete_paradas.visibility=View.GONE
        }

        viewModel = ViewModelProviders.of(this).get(ModeloVistaNuevaRuta::class.java)
        // TODO: Use the ViewModel
    }


    private fun agregarParada() {
        boton_agregar_parada.visibility = View.GONE
        places_autocomplete_paradas.visibility = View.VISIBLE
        recycler_view_paradas.visibility = View.VISIBLE
    }

    fun setUpRecyclerView(){
        mRecyclerView =  recycler_view_paradas as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this.context)
        mAdapter.RecyclerAdapter(breakpointsParadas, this.requireContext())
        mRecyclerView.adapter = mAdapter
    }
}
