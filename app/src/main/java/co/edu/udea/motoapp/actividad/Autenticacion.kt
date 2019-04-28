package co.edu.udea.motoapp.actividad

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.excepcion.ExcepcionAutenticacion
import co.edu.udea.motoapp.servicios.MyFirebaseMessagingService
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class Autenticacion : Activity() {
    private val CODIGO_VALIDACION = 0
    val messagingService = MyFirebaseMessagingService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manejarAutenticacion()
    }

    private fun manejarAutenticacion() {
        if (FirebaseAuth.getInstance().currentUser == null)
            this.iniciarAutenticacion()
        else
            iniciarActividad(Principal::class.java)
    }

    private fun iniciarAutenticacion() {
        val proveedoresAutenticacion = arrayListOf(
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(proveedoresAutenticacion)
                .setLogo(R.drawable.inicio)
                .build(), CODIGO_VALIDACION)
    }

    override fun onActivityResult(codigoRespuestaValidacion: Int, codigoResultado: Int, datos: Intent?) {
        super.onActivityResult(codigoRespuestaValidacion, codigoResultado, datos)
        if (codigoRespuestaValidacion == CODIGO_VALIDACION)
            if (codigoResultado == Activity.RESULT_OK)
                finalizarAutenticacion()
            else
                throw ExcepcionAutenticacion("Error iniciando sesion")
    }

    private fun finalizarAutenticacion() {
        val motero = FirebaseAuth.getInstance().currentUser
        val metadata = motero?.metadata ?: throw ExcepcionAutenticacion("Error cargarndo datos del usuario")
        val esNuevoMotero = metadata.creationTimestamp - metadata.lastSignInTimestamp
        if (esNuevoMotero > -10 && esNuevoMotero < 10)
            iniciarActividad(Registro::class.java)
        else
            messagingService.grabFcmToken()
            iniciarActividad(Principal::class.java)
    }

    private fun iniciarActividad(actividad: Class<*>) {
        startActivity(Intent(this@Autenticacion, actividad))
        finish()
    }
}
