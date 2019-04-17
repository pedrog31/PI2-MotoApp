package co.edu.udea.motoapp.ui.rutas_publicas

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.edu.udea.motoapp.modelo.Ruta
import co.edu.udea.motoapp.modelo.RutaPrivada
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ModeloVistaRutasPublicas : ViewModel() {

    val listaRutasPublicas = MutableLiveData<MutableList<Ruta>>()
    val repositorioRutasPublicas = FirebaseDatabase.getInstance().reference

    val escuchadorIdentificadorAmigos = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(datoListaRutasPublicas: DataSnapshot) {
            if (datoListaRutasPublicas.exists()) {
                datoListaRutasPublicas.children.forEach {
                    val ruta = it.getValue(Ruta::class.java) ?: return
                    listaRutasPublicas.value?.add(ruta)
                }
                listaRutasPublicas.value = listaRutasPublicas.value
            }
        }
    }

    fun buscarRutas() {
        listaRutasPublicas.value = mutableListOf()
        val referenciaRutasPublicas = repositorioRutasPublicas.child("rutasPublicas")
        referenciaRutasPublicas.keepSynced(true)
        referenciaRutasPublicas.addListenerForSingleValueEvent(escuchadorIdentificadorAmigos)
    }

    fun agregarRutaPublicaMotero(rutaSeleccionada: Ruta, contexto: Context) {
        val identificacionMotero = FirebaseAuth.getInstance().uid!!
        val rutaPrivada = RutaPrivada(rutaSeleccionada, identificacionMotero)
        val identificacionNuevaRuta = repositorioRutasPublicas.child("rutasPrivadas").push().key
        repositorioRutasPublicas
            .child("rutasPrivadas/$identificacionNuevaRuta")
            .setValue(rutaPrivada)
            .addOnSuccessListener {
                repositorioRutasPublicas
                    .child("moteros/$identificacionMotero/rutas/$identificacionNuevaRuta")
                    .setValue(true).addOnSuccessListener {
                        Toast.makeText(contexto, "Ruta agregada correctamente", Toast.LENGTH_LONG).show()
                    }
            }
    }
}
