package co.edu.udea.motoapp.ui.ruta_iniciada

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.edu.udea.motoapp.modelo.IntegranteRuta
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.modelo.RutaPrivada
import co.edu.udea.motoapp.servicios.UbicacionActualRuta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ModeloVistaRutaIniciada : ViewModel() {

    val listaIntegrantesRuta = MutableLiveData<LinkedHashMap<String, IntegranteRuta>>()
    val listaMoterosIntegrantesRuta = MutableLiveData<LinkedHashMap<String, Motero>>()
    val estadoRuta = MutableLiveData<String>()
    var keyRutaActual: String? = null
    var rutaActual: RutaPrivada? = null
    val repositorioRutaPrivada = FirebaseDatabase.getInstance().reference

    val escuchadorEstadoRuta = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {

        }

        override fun onDataChange(datoEstado: DataSnapshot) {
            rutaActual?.estado = datoEstado.value.toString()
            estadoRuta.value = rutaActual?.estado
            when (rutaActual?.estado) {
                "Iniciada" -> {

                }

                "Creada" -> {

                }

                "Finalizada" -> {

                }
            }
        }
    }

    fun eliminarRuta(contexto: FragmentActivity) {
        keyRutaActual?.let {
            repositorioRutaPrivada.child(
                "moteros/${FirebaseAuth.getInstance().uid}/rutas/$it"
            ).removeValue()
                .addOnSuccessListener {
                    repositorioRutaPrivada
                        .child("rutasPrivadas/$it/estado").removeValue()
                        .addOnSuccessListener {
                            mostrarMensaje(contexto, "Ruta eliminada correctamente")
                            contexto.finish()
                        }
                        .addOnFailureListener {
                            mostrarMensaje(contexto, "Error eliminando ruta")
                        }
                }
                .addOnFailureListener {
                    mostrarMensaje(contexto, "Error eliminando ruta")
                }
        }
    }

    fun iniciarRuta(contexto: FragmentActivity) {
        repositorioRutaPrivada
            .child("rutasPrivadas/$keyRutaActual/estado")
            .setValue("Iniciada")
            .addOnSuccessListener {
                iniciarRutaIntegrante(contexto)
            }
            .addOnFailureListener {
                mostrarMensaje(contexto, "Error iniciando ruta")
            }
    }

    fun iniciarRutaIntegrante(contexto: FragmentActivity) {
        repositorioRutaPrivada
            .child(
                "rutasPrivadas/${keyRutaActual}/integrantes/${FirebaseAuth.getInstance().uid}/conectado"
            ).setValue(true)
            .addOnSuccessListener {
                val locationServiceIntent = Intent(contexto, UbicacionActualRuta::class.java)
                locationServiceIntent.putExtra("idRuta", keyRutaActual)
                contexto.startService(locationServiceIntent)
            }
            .addOnFailureListener {
                mostrarMensaje(contexto, "Error iniciando ruta")
            }
    }

    fun finalizarRuta(contexto: FragmentActivity) {
        repositorioRutaPrivada
            .child("rutasPrivadas/$keyRutaActual/estado")
            .setValue("Finalizada")
            .addOnSuccessListener {
                mostrarMensaje(contexto, "Ruta finalizada correctamente")
                contexto.finish()
            }
            .addOnFailureListener {
                mostrarMensaje(contexto, "Error finalizando ruta")
            }
    }

    fun eliminarRutaInvitada(contexto: FragmentActivity) {
        val moteroId = FirebaseAuth.getInstance().uid
        repositorioRutaPrivada
            .child("moteros/$moteroId/invitacionRuta/$keyRutaActual")
            .removeValue()
            .addOnSuccessListener {
                repositorioRutaPrivada
                    .child("rutasPrivadas/$keyRutaActual/integrantes/$moteroId")
                    .removeValue()
                    .addOnSuccessListener {
                        mostrarMensaje(contexto, "Ruta eliminada correctamente")
                        contexto.finish()
                    }
                    .addOnFailureListener {
                        mostrarMensaje(contexto, "Error eliminando ruta")
                    }
            }
            .addOnFailureListener {
                mostrarMensaje(contexto, "Error eliminando ruta")
            }
    }

    fun eliminarIntegranteRuta(contexto: FragmentActivity, integranteRutaKey: String) {
        repositorioRutaPrivada
            .child("rutasPrivadas/${keyRutaActual}/integrantes/$integranteRutaKey")
            .removeValue()
            .addOnSuccessListener {
                listaMoterosIntegrantesRuta.value?.remove(integranteRutaKey)
                listaIntegrantesRuta.value?.remove(integranteRutaKey)
                listaIntegrantesRuta.value = listaIntegrantesRuta.value
                mostrarMensaje(contexto, "Integrante eliminado correctamente")
            }.addOnFailureListener {
                mostrarMensaje(contexto, "Error eliminando integrante :(")
            }
    }

    private fun mostrarMensaje(contexto: Context, mensaje: String) {
        Toast.makeText(contexto, mensaje, Toast.LENGTH_LONG).show()
    }

    fun iniciarEscuchaEstadoRuta() {
        repositorioRutaPrivada
            .child("rutasPrivadas/${keyRutaActual}/estado")
            .addValueEventListener(escuchadorEstadoRuta)

    }
}
