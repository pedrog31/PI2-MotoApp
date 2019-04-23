package co.edu.udea.motoapp.ui.ruta_iniciada

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.edu.udea.motoapp.modelo.IntegranteRuta
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.modelo.RutaPrivada
import com.google.firebase.database.*

class ModeloVistaRutaIniciada : ViewModel() {

    val listaIntegrantesRuta = MutableLiveData<HashMap<String, IntegranteRuta>>()
    val listaMoterosIntegrantesRuta = MutableLiveData<HashMap<String, Motero>>()
    val keyRutaActual = MutableLiveData<String>()
    val rutaActual = MutableLiveData<RutaPrivada>()
    val repositorioRutaPrivada = FirebaseDatabase.getInstance().reference

    val escuchadorIntegrantesRutas = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {

        }

        override fun onChildMoved(datoIntegrante: DataSnapshot, nombreHijoAnterior: String?) {

        }

        override fun onChildChanged(datoIntegrante: DataSnapshot, nombreHijoAnterior: String?) {
            datoIntegrante.getValue(IntegranteRuta::class.java)?.let {
                listaIntegrantesRuta.value?.put(datoIntegrante.key!!, it)
                listaIntegrantesRuta.value = listaIntegrantesRuta.value
            }
        }

        override fun onChildAdded(datoIntegrante: DataSnapshot, nombreHijoAnterior: String?) {
            datoIntegrante.getValue(IntegranteRuta::class.java)?.let {
                listaIntegrantesRuta.value?.put(datoIntegrante.key!!,it)
                listaIntegrantesRuta.value = listaIntegrantesRuta.value
                repositorioRutaPrivada.child("moteros/${datoIntegrante.key}")
                    .addListenerForSingleValueEvent(escuchadorMoteroIntegranteRuta)
            }
        }

        override fun onChildRemoved(datoIntegrante: DataSnapshot) {
            listaIntegrantesRuta.value?.remove(datoIntegrante.key)
            listaMoterosIntegrantesRuta.value?.remove(datoIntegrante.key)
        }
    }
    val escuchadorMoteroIntegranteRuta = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(motero: DataSnapshot) {
            motero.getValue(Motero::class.java)?.let {
                listaMoterosIntegrantesRuta.value?.put(motero.key.toString(), it)
                listaMoterosIntegrantesRuta.value = listaMoterosIntegrantesRuta.value
            }
        }
    }

    fun eliminarRuta(context: Context) {
        keyRutaActual.value?.let {
            repositorioRutaPrivada.child("rutasPrivadas/$it").removeValue()
                .addOnSuccessListener {
                    mostrarMensaje(context, "Ruta eliminada correctamente")
                }
                .addOnFailureListener {
                    mostrarMensaje(context, "Error eliminando ruta")
                }
        }
    }

    fun buscarIntegrantesRuta() {
        listaIntegrantesRuta.value = hashMapOf()
        keyRutaActual.value?.let {
            repositorioRutaPrivada
                .child("rutasPrivadas/$it/integrantes")
                .addChildEventListener(escuchadorIntegrantesRutas)
        }
    }

    private fun mostrarMensaje(context: Context, mensaje: String) {
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
    }
}
