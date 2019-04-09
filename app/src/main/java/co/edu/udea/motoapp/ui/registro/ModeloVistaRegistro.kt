package co.edu.udea.motoapp.ui.registro

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.edu.udea.motoapp.excepcion.ExcepcionAutenticacion
import co.edu.udea.motoapp.excepcion.ExcepcionRegistro
import co.edu.udea.motoapp.modelo.Motero
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ModeloVistaRegistro : ViewModel() {

    val estadoRegistro = MutableLiveData<Int>()
    val moteroActual = FirebaseAuth.getInstance().currentUser ?: throw ExcepcionAutenticacion("Error cargando datos para el registro")
    val repositorioMoteros = FirebaseDatabase.getInstance().getReference("moteros")

    fun guardarInformacionMotero(nombre: String, correo: String, celular: String, ciudad: String) {
        moteroActual.updateEmail(correo).addOnSuccessListener {
            this.repositorioMoteros
                .child(moteroActual.uid)
                .setValue(Motero(nombre, correo,  ciudad, celular, mutableListOf("CREADO")))
                .addOnSuccessListener {
                    estadoRegistro.value = 0
                }
                .addOnFailureListener {
                    estadoRegistro.value = -1
                    throw ExcepcionRegistro("Error realizando el registro", it)
                }
        }.addOnFailureListener {
            estadoRegistro.value = -1
        }
    }
}
