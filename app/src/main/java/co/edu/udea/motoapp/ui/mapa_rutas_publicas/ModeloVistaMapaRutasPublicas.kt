package co.edu.udea.motoapp.ui.mapa_rutas_publicas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.edu.udea.motoapp.modelo.Ruta
import com.google.firebase.database.*

class ModeloVistaMapaRutasPublicas : ViewModel() {

    val listaRutasPublicas = MutableLiveData<MutableList<Ruta>>()
    val repositorioRutasPublicas = FirebaseDatabase.getInstance().getReference("rutasPublicas")

    val escuchadorIdentificadorAmigos = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(datoListaRutasPublicas: DataSnapshot) {
            if (datoListaRutasPublicas.exists()) {
                datoListaRutasPublicas.children.forEach {
                    val ruta = it.getValue(Ruta::class.java) ?: return
                    listaRutasPublicas.value?.add(ruta)
                }
                listaRutasPublicas.value = listaRutasPublicas.value
            }
        }
    }

    fun buscarRutas() {
        listaRutasPublicas.value = mutableListOf()
        repositorioRutasPublicas.addListenerForSingleValueEvent(escuchadorIdentificadorAmigos)
    }
}
