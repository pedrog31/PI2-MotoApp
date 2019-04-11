package co.edu.udea.motoapp.ui.nuevoamigo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.edu.udea.motoapp.modelo.Motero
import com.google.firebase.database.*

class ModeloVistaNuevoAmigo : ViewModel() {

    val palabraClave = MutableLiveData<String>()

    val listaAmigos = MutableLiveData<HashMap<String, Motero>>()
    val repositorioMoteros = FirebaseDatabase.getInstance().getReference("moteros")
    val escuchadorIdentificadorAmigos = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {

        }

        override fun onChildMoved(identificador: DataSnapshot, nombreHijoAnterior: String?) {

        }

        override fun onChildChanged(identificador: DataSnapshot, nombreHijoAnterior: String?) {

        }

        override fun onChildAdded(identificador: DataSnapshot, nombreHijoAnterior: String?) {
            identificador.key?.let { listaAmigos.value?.put(it, identificador.getValue(Motero::class.java)!!) }
            listaAmigos.value = listaAmigos.value
        }

        override fun onChildRemoved(identificador: DataSnapshot) {
            listaAmigos.value?.remove(identificador.value.toString())
            listaAmigos.value = listaAmigos.value
        }
    }


    fun buscarAmigos() {
        listaAmigos.value = hashMapOf()
        repositorioMoteros
            .orderByChild("nombre")
            .startAt(palabraClave.value)
            .endAt("${palabraClave.value}\uf8ff")
            .addChildEventListener(escuchadorIdentificadorAmigos)
    }
}
