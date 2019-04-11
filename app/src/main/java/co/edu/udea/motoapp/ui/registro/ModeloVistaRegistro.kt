package co.edu.udea.motoapp.ui.registro

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.edu.udea.motoapp.excepcion.ExcepcionAutenticacion
import co.edu.udea.motoapp.modelo.Motero
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ModeloVistaRegistro : ViewModel() {

    val estadoRegistro = MutableLiveData<String>()
    val moteroActual = FirebaseAuth.getInstance().currentUser ?: throw ExcepcionAutenticacion("Error cargando datos para el registro")
    val repositorioMoteros = FirebaseDatabase.getInstance().getReference("moteros")

    fun guardarInformacionMotero(nombre: String, correo: String, ciudad: String, celular: String) {
        moteroActual.updateEmail(correo).addOnSuccessListener {
            this.repositorioMoteros
                .child(moteroActual.uid)
                .setValue(
                    Motero(nombre, correo, celular,  ciudad, moteroActual.photoUrl.toString(), mutableListOf("CREADO")))
                .addOnSuccessListener {
                    estadoRegistro.value = "ok"
                }
                .addOnFailureListener {
                    estadoRegistro.value = "Error realizando el registro: ${it.message}"
                }
        }.addOnFailureListener {
            estadoRegistro.value = "Error realizando el registro: ${it.message}"
        }
    }
}
