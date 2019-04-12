package co.edu.udea.motoapp.ui.nuevoamigo

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.modelo.SolicitudAmistad
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ModeloVistaNuevoAmigo : ViewModel() {

    val palabraClave = MutableLiveData<String>()
    val identificacion = FirebaseAuth.getInstance().uid
    val listaMoteros = MutableLiveData<HashMap<String, Motero>>()
    val repositorioMoteros = FirebaseDatabase.getInstance().reference
    val listaAmigos = mutableListOf<String>()
    val escuchadorBusquedaMoteros = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {

        }

        override fun onChildMoved(identificador: DataSnapshot, nombreHijoAnterior: String?) {

        }

        override fun onChildChanged(identificador: DataSnapshot, nombreHijoAnterior: String?) {

        }

        override fun onChildAdded(identificador: DataSnapshot, nombreHijoAnterior: String?) {
            identificador.key?.let {
                if (it != identificacion && !listaAmigos.contains(it)) {
                    listaMoteros.value?.put(it, identificador.getValue(Motero::class.java)!!)
                    listaMoteros.value = listaMoteros.value
                }
            }
        }

        override fun onChildRemoved(identificador: DataSnapshot) {
            listaMoteros.value?.remove(identificador.value.toString())
            listaMoteros.value = listaMoteros.value
        }
    }

    val escuchadorIdentificacionAmigos = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {

        }

        override fun onDataChange(dato: DataSnapshot) {
            if (dato.exists())
                dato.children.forEach {
                    listaAmigos.add(it.value.toString())
                }
            repositorioMoteros
                .child("moteros")
                .orderByChild("nombre")
                .startAt(palabraClave.value?.toUpperCase())
                .endAt("${palabraClave.value?.toUpperCase()}\uf8ff")
                .addChildEventListener(escuchadorBusquedaMoteros)
        }

    }


    fun buscarAmigos() {
        listaMoteros.value = hashMapOf()
        repositorioMoteros.child("moteros").removeEventListener(escuchadorBusquedaMoteros)
        repositorioMoteros.child("moteros/${identificacion}/amigos").addListenerForSingleValueEvent(escuchadorIdentificacionAmigos)
    }

    fun agregarAmigo(identificacionAmigo: String, contexto: FragmentActivity) {
        val solicitudAmistad = identificacion?.let { SolicitudAmistad(it, identificacionAmigo) }
        repositorioMoteros.child("solicitudesAmistad").push().setValue(solicitudAmistad).addOnSuccessListener {
            mostrarMensaje("Solicitud de amistad enviada", contexto)
        }.addOnFailureListener {
            mostrarMensaje("Error enviando solicitud de amistad", contexto)
        }
        contexto.finish()
    }

    private fun mostrarMensaje(mensaje: String, contexto: FragmentActivity) {
        Toast.makeText(contexto, mensaje, Toast.LENGTH_LONG).show()
    }
}
