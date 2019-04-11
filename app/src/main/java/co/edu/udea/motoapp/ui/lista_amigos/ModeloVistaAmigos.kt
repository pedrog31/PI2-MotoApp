package co.edu.udea.motoapp.ui.lista_amigos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.edu.udea.motoapp.modelo.Motero
import com.google.firebase.database.*

class ModeloVistaAmigos : ViewModel() {

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
            repositorioMoteros.child(identificador.value.toString()).addListenerForSingleValueEvent(escuchadorAmigo)
        }

        override fun onChildRemoved(identificador: DataSnapshot) {
            listaAmigos.value?.remove(identificador.value.toString())
            listaAmigos.value = listaAmigos.value
        }
    }
    val escuchadorAmigo = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(motero: DataSnapshot) {
            motero.getValue(Motero::class.java).let {
                listaAmigos.value?.put(motero.key.toString(), it!!)
                listaAmigos.value = listaAmigos.value
            }
        }
    }


    fun buscarAmigos(query: Query) {
        listaAmigos.value = hashMapOf()
        query.addChildEventListener(escuchadorIdentificadorAmigos)
    }
}
