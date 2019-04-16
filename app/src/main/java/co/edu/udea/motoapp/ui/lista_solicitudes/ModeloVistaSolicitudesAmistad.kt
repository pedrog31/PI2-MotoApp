package co.edu.udea.motoapp.ui.lista_solicitudes

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.modelo.SolicitudAmistad
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ModeloVistaSolicitudesAmistad : ViewModel() {

    val listaSolicitudes = MutableLiveData<HashMap<String, SolicitudAmistad>>()
    val listaMoterosSolicitantes = MutableLiveData<HashMap<String, Motero>>()
    val repositorioMoteros = FirebaseDatabase.getInstance().reference
    val identificadorMotero = FirebaseAuth.getInstance().uid
    val escuchadorIdentificadorSolicitudes = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {

        }

        override fun onChildMoved(identificador: DataSnapshot, nombreHijoAnterior: String?) {

        }

        override fun onChildChanged(identificador: DataSnapshot, nombreHijoAnterior: String?) {

        }

        override fun onChildAdded(solicitudAmistadData: DataSnapshot, nombreHijoAnterior: String?) {
            val solicitudAmistad = solicitudAmistadData.getValue(SolicitudAmistad::class.java) ?: return
            listaSolicitudes.value?.put(solicitudAmistadData.key.toString(),solicitudAmistad)
            repositorioMoteros.child("moteros/${solicitudAmistad.invita}")
                .addListenerForSingleValueEvent(escuchadorSolicitudAmistad)
        }

        override fun onChildRemoved(solicitudAmistadData: DataSnapshot) {
            val solicitudAmistad = solicitudAmistadData.getValue(SolicitudAmistad::class.java) ?: return
            listaSolicitudes.value?.remove(solicitudAmistadData.key)
            listaMoterosSolicitantes.value = listaMoterosSolicitantes.value?.filterKeys {
                solicitudAmistad.invita != it
            } as HashMap<String, Motero>
        }
    }
    val escuchadorSolicitudAmistad = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(motero: DataSnapshot) {
            motero.getValue(Motero::class.java).let {
                listaMoterosSolicitantes.value?.put(motero.key.toString(), it!!)
                listaMoterosSolicitantes.value = listaMoterosSolicitantes.value
            }
        }
    }

    fun buscarSolicitudes() {
        listaSolicitudes.value = hashMapOf()
        listaMoterosSolicitantes.value = hashMapOf()
        repositorioMoteros
            .child("solicitudesAmistad")
            .orderByChild("recibe")
            .equalTo(identificadorMotero)
            .addChildEventListener(escuchadorIdentificadorSolicitudes)
    }

    fun aceptarSolicitudAmistad(identificacionAmigo: String, contexto: FragmentActivity) {
        repositorioMoteros
            .child("moteros/${identificacionAmigo}/amigos/${identificadorMotero}").setValue(true)
            .addOnSuccessListener {
                repositorioMoteros
                    .child("moteros/${identificadorMotero}/amigos/${identificacionAmigo}").setValue(true)
                    .addOnSuccessListener {
                        listaSolicitudes.value?.filterValues {
                            it.invita == identificacionAmigo
                        }?.keys?.forEach {
                            repositorioMoteros
                                .child("solicitudesAmistad/${it}")
                                .removeValue()
                                .addOnSuccessListener {
                                    mostrarMensaje("Ahora tienes un nuevo amigo", contexto)
                                }.addOnFailureListener {
                                    mostrarMensaje("Error aceptando solicitud", contexto)
                                }
                        }
                    }
            }
    }

    fun rechazarSolicitudAmistad(identificacionAmigo: String, contexto: FragmentActivity) {
        listaSolicitudes.value?.filterValues {
            it.invita == identificacionAmigo
        }?.keys?.forEach {
            repositorioMoteros
                .child("solicitudesAmistad/${it}")
                .removeValue()
                .addOnSuccessListener {
                    mostrarMensaje("Solicitud rechazara exitosamente", contexto)
                }.addOnFailureListener {
                    mostrarMensaje("Error rechazando solicitud", contexto)
                }
        }
    }

    private fun mostrarMensaje(mensaje: String, contexto: FragmentActivity) {
        Toast.makeText(contexto, mensaje, Toast.LENGTH_LONG).show()
    }
}
