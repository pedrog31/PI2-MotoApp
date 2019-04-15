package co.edu.udea.motoapp.ui.perfil

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.edu.udea.motoapp.modelo.Motero
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ModeloVistaMotero : ViewModel() {

    var motero = MutableLiveData<Motero>()
    lateinit var moteroFirebase: FirebaseUser

    fun buscarMotero() {
        val moteroFirebase = FirebaseAuth.getInstance().currentUser
        if (moteroFirebase == null)
            motero.value = Motero()
        else {
            this.moteroFirebase = moteroFirebase
            FirebaseDatabase
                .getInstance()
                .reference
                .child("moteros")
                .child(moteroFirebase.uid)
                .addListenerForSingleValueEvent(escuchadorMotero)
        }
    }

    val escuchadorMotero = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {

        }

        override fun onDataChange(resultado: DataSnapshot) {
            motero.value = resultado.getValue(Motero::class.java)
        }
    }
}