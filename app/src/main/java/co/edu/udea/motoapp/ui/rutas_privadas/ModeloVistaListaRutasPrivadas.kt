package co.edu.udea.motoapp.ui.rutas_privadas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.edu.udea.motoapp.modelo.RutaPrivada
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ModeloVistaListaRutasPrivadas : ViewModel() {

    val identificadorRuta = FirebaseAuth.getInstance().uid
    val listaRuta = MutableLiveData<HashMap<String, RutaPrivada>>()
    val listaInvitacionRutas = MutableLiveData<HashMap<String, RutaPrivada>>()
    val repositorioRutas = FirebaseDatabase.getInstance().reference
    val repositorioInvitacionRutas = FirebaseDatabase.getInstance().reference

    val escuchadorIdentificadorRutas = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {

        }

        override fun onChildMoved(identificador: DataSnapshot, nombreHijoAnterior: String?) {

        }

        override fun onChildChanged(identificador: DataSnapshot, nombreHijoAnterior: String?) {

        }

        override fun onChildAdded(identificador: DataSnapshot, nombreHijoAnterior: String?) {
            identificador.key?.let {
                repositorioRutas.child("rutasPrivadas/$it").addListenerForSingleValueEvent(escuchadorRuta)
            }
        }

        override fun onChildRemoved(identificador: DataSnapshot) {
            listaRuta.value?.remove(identificador.key)
            listaRuta.value = listaRuta.value

        }
    }

    val escuchadorIdentificadorInvitacionRutas = object : ChildEventListener{
        override fun onCancelled(p0: DatabaseError) {
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            p0.key?.let {
                repositorioInvitacionRutas.child("rutasPrivadas/$it").addListenerForSingleValueEvent(escuchadorInvitacionRuta)
            }
        }

        override fun onChildRemoved(p0: DataSnapshot) {
            listaRuta.value?.remove(p0.value.toString())
            listaRuta.value = listaRuta.value
        }

    }

    val escuchadorRuta = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(datoRuta: DataSnapshot) {
            datoRuta.getValue(RutaPrivada::class.java)?.let {
                listaRuta.value?.put(datoRuta.key.toString(), it)
                listaRuta.value = listaRuta.value
            }
        }
    }
    val escuchadorInvitacionRuta = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(datoRuta: DataSnapshot) {
            datoRuta.getValue(RutaPrivada::class.java)?.let {
                listaInvitacionRutas.value?.put(datoRuta.key.toString(), it)
                listaInvitacionRutas.value = listaInvitacionRutas.value
            }
        }
    }

    fun buscarRutas() {
        listaRuta.value = hashMapOf()
        repositorioRutas
            .child("moteros/$identificadorRuta/rutas")
            .addChildEventListener(this.escuchadorIdentificadorRutas)
    }

    fun buscarInvitacionRutas() {
        listaInvitacionRutas.value = hashMapOf()
        repositorioInvitacionRutas
            .child("moteros/$identificadorRuta/invitacionRuta")
            .addChildEventListener(this.escuchadorIdentificadorInvitacionRutas)
    }
}
