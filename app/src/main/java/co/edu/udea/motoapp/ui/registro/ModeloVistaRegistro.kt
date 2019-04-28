package co.edu.udea.motoapp.ui.registro

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.edu.udea.motoapp.excepcion.ExcepcionAutenticacion
import co.edu.udea.motoapp.modelo.Motero
import co.edu.udea.motoapp.servicios.MyFirebaseMessagingService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ModeloVistaRegistro : ViewModel() {

    val estadoRegistro = MutableLiveData<String>()
    val moteroActual = FirebaseAuth.getInstance().currentUser ?: throw ExcepcionAutenticacion("Error cargando datos para el registro")
    val repositorioMoteros = FirebaseDatabase.getInstance().getReference("moteros")
    val messagingService = MyFirebaseMessagingService()


    fun guardarInformacionMotero(nombre: String, correo: String, ciudad: String, celular: String) {
        moteroActual.updateEmail(correo).addOnSuccessListener {
            this.repositorioMoteros
                .child(moteroActual.uid)
                .setValue(
                    Motero(nombre, correo, celular,  ciudad, moteroActual.photoUrl.toString(), hashMapOf(), mutableListOf(), ""))
                .addOnSuccessListener {
                    estadoRegistro.value = "ok"
                    messagingService.grabFcmToken()
                }
                .addOnFailureListener {
                    estadoRegistro.value = "Error realizando el registro: ${it.message}"
                }
        }.addOnFailureListener {
            estadoRegistro.value = "Error realizando el registro: ${it.message}"
        }
    }
}
