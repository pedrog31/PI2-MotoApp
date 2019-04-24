package co.edu.udea.motoapp.ui.nuevaruta

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.excepcion.ExcepcionAutenticacion
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.modelo.ParadaRuta
import co.edu.udea.motoapp.modelo.RutaPrivada
import co.edu.udea.motoapp.ui.lista_amigos.ModeloVistaAmigos
import co.edu.udea.motoapp.util.TransformacionImagen
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.jaiselrahman.hintspinner.HintSpinnerAdapter
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener
import com.seatgeek.placesautocomplete.model.Place
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.alert_dialog_amigos.*
import kotlinx.android.synthetic.main.alert_dialog_amigos.view.*
import kotlinx.android.synthetic.main.fragmento_nueva_ruta.*
import java.util.*
import kotlin.collections.HashMap


class NuevaRuta : Fragment() {

    private val PERMISSION_CODE = 1001
    val IMAGE_PICK_CODE = 1000


    lateinit var mRecyclerView : RecyclerView
    lateinit var amigosRecyclerView : RecyclerView

    val mAdapter : AdaptadorParadas = AdaptadorParadas()
    val breakpointsParadas = mutableListOf<ParadaRuta>()
    lateinit var filePath: Uri
    lateinit var urlFoto: String

    lateinit var geocoder: Geocoder
    lateinit var address: List<Address>
    val breakpoints = mutableListOf<ParadaRuta>()
    var amigos = hashMapOf<String,HashMap<String,Any>>()
    val moteroActual = FirebaseAuth.getInstance().currentUser ?: throw ExcepcionAutenticacion()
    val repositorioRutasPrivadas = FirebaseDatabase.getInstance().reference

    var storage = FirebaseStorage.getInstance()
    var storageRef = storage.reference

    private lateinit var modeloVistaAmigos: ModeloVistaAmigos
    lateinit var adaptador: AdaptadorAmigos
    companion object {
        fun newInstance() = NuevaRuta()
    }

    private lateinit var viewModel: ModeloVistaNuevaRuta
    var destinoSeleccionado = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inflater.inflate(R.layout.alert_dialog_amigos, container, false)
        return inflater.inflate(R.layout.fragmento_nueva_ruta, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        geocoder= Geocoder(context, Locale.getDefault())
        setUpRecyclerView()
        setupAmigosAdapter()

        var s=mutableListOf<String>("Fácil","Intermedio","Difícil")
        hint_spinner.tag="nj"

        hint_spinner.setAdapter(HintSpinnerAdapter<String>(requireContext(),s,"Seleccione dificultad:"))

        boton_agregar_parada.setOnClickListener{
            this.agregarParada()
        }

        boton_imagen_ruta.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }

        }

        boton_invitar_amigo_ruta.setOnClickListener{
            val dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_amigos,null)

            val builder = AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle("Agregar Amigos")

            var alert : AlertDialog =builder.show()

            amigosRecyclerView = dialogView.lista_amigos_checkbox as RecyclerView
            amigosRecyclerView.setHasFixedSize(true)
            amigosRecyclerView.layoutManager = LinearLayoutManager(this.context)
            amigosRecyclerView.adapter = adaptador
            if(adaptador.itemCount==0){
                textView_sin_amigos.visibility=View.VISIBLE
            }
            dialogView.boton_invita_amigos.setOnClickListener{
                alert.dismiss()
            }

        }

        boton_agregar_ruta.setOnClickListener {
            var final=breakpoints.removeAt(1)
            breakpoints.addAll(breakpointsParadas)
            breakpoints.add(final)
            val ruta = RutaPrivada(
                descripcion_ruta.text.toString(),0,
                0,
                hint_spinner.selectedItem.toString(),
                nombre_ruta.text.toString(),
                "", breakpoints,
                0.0F,
                0,
                moteroActual.uid,
                amigos
            )
            if(::filePath.isInitialized){
                agregarRutaFoto(ruta)
            }else{
                agregarRuta(ruta)
            }
        }

        places_autocomplete.setOnPlaceSelectedListener(
            object : OnPlaceSelectedListener {
                override fun onPlaceSelected(p0: com.seatgeek.placesautocomplete.model.Place) {
                    address = geocoder.getFromLocationName(p0.description, 5)
                    val location = (address as MutableList<Address>?)?.get(0)
                    breakpoints.add(0, ParadaRuta(p0.description, location!!.latitude, location.longitude))

                    boton_agregar_parada.visibility=View.VISIBLE
                    textInputLayout2.visibility=View.VISIBLE
                    places_autocomplete_lugar_llegada.requestFocus()

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
                    textInputLayout4.visibility=View.GONE
                    boton_agregar_parada.visibility=View.VISIBLE
                    places_autocomplete_lugar_llegada.requestFocus()
                }
            }
        )
        places_autocomplete_paradas.setOnClearListener {
            boton_agregar_parada.visibility=View.VISIBLE
            textInputLayout4.visibility=View.GONE

        }

        viewModel = ViewModelProviders.of(this).get(ModeloVistaNuevaRuta::class.java)
        // TODO: Use the ViewModel
    }




    private fun agregarParada() {
        boton_agregar_parada.visibility = View.GONE
        textInputLayout4.visibility=View.VISIBLE
        recycler_view_paradas.visibility = View.VISIBLE
        places_autocomplete_paradas.requestFocus()

    }

    fun setUpRecyclerView(){
        mRecyclerView =  recycler_view_paradas as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this.context)
        mAdapter.RecyclerAdapter(breakpointsParadas, this.requireContext())
        mRecyclerView.adapter = mAdapter
    }
    private fun setupAmigosAdapter() {
        var adaptadorVistaAmigos: AdaptadorAmigos? = null
        val observadorListaAmigos = Observer<HashMap<String,Motero>>{listaAmigos ->
            listaAmigos?.let {
                adaptadorVistaAmigos?.notifyDataSetChanged()
            }
        }
        modeloVistaAmigos = ViewModelProviders.of(requireActivity()).get(ModeloVistaAmigos::class.java)
        if (modeloVistaAmigos.listaAmigos.value == null) {
            val moteroId = FirebaseAuth.getInstance().uid ?: throw ExcepcionAutenticacion()
            val query = FirebaseDatabase.getInstance().reference.child("moteros/${moteroId}/amigos")
            modeloVistaAmigos.buscarAmigos(query)
        }
        modeloVistaAmigos.listaAmigos.observe(requireActivity(), observadorListaAmigos)
        adaptadorVistaAmigos = modeloVistaAmigos.listaAmigos.value?.let {
            AdaptadorAmigos(it,amigos, requireActivity())
        }
        adaptador= adaptadorVistaAmigos!!
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this.requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("imagen", "entra1")
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData()!!
                Picasso.get()
                    .load(filePath)
                    .centerCrop()
                    .transform(TransformacionImagen(10, 0))
                    .fit()
                    .into(boton_imagen_ruta)

        }
    }

    fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    fun agregarRutaFoto(ruta: RutaPrivada){
        var rutaPvda = ruta
        val current = System.currentTimeMillis()
        val imagenRef = storageRef.child("imagenesRuta/"+current+".jpg")
        var uploadTask = imagenRef.putFile(filePath).continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{task->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation imagenRef.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                urlFoto= task.result.toString()
                rutaPvda.urlFoto=urlFoto
                agregarRuta(rutaPvda)

            } else {
                // Handle failures
                // ...
            }
        }



        uploadTask.addOnFailureListener {
            Log.d("imagen", "no subio")
        }.addOnSuccessListener {


        }
    }

    fun agregarRuta(rutaPrivada: RutaPrivada){
        val ruta =rutaPrivada
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



}
