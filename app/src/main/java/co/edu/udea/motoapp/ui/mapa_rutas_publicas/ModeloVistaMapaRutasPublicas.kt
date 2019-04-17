package co.edu.udea.motoapp.ui.mapa_rutas_publicas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.edu.udea.motoapp.modelo.Ruta
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ModeloVistaMapaRutasPublicas : ViewModel() {

    val listaRutasPublicas = MutableLiveData<MutableList<Ruta>>()
    val repositorioRutasPublicas = FirebaseDatabase.getInstance().getReference("rutasPublicas")

    val escuchadorIdentificadorAmigos = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {

        }

        override fun onChildMoved(datoRuta: DataSnapshot, nombreHijoAnterior: String?) {

        }

        override fun onChildChanged(datoRuta: DataSnapshot, nombreHijoAnterior: String?) {

        }

        override fun onChildAdded(datoRuta: DataSnapshot, nombreHijoAnterior: String?) {
            val ruta = datoRuta.getValue(Ruta::class.java) ?: return
            listaRutasPublicas.value?.add(ruta)
            listaRutasPublicas.value = listaRutasPublicas.value
        }

        override fun onChildRemoved(datoRuta: DataSnapshot) {
            val ruta = datoRuta.getValue(Ruta::class.java) ?: return
            listaRutasPublicas.value?.remove(ruta)
            listaRutasPublicas.value = listaRutasPublicas.value
        }
    }

    fun buscarRutas() {
        listaRutasPublicas.value = mutableListOf()
        repositorioRutasPublicas.addChildEventListener(escuchadorIdentificadorAmigos)
    }
}
