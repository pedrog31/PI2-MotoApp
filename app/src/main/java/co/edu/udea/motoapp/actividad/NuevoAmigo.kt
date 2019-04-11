package co.edu.udea.motoapp.actividad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.ui.nuevoamigo.NuevoAmigo

class NuevoAmigo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_nuevo_amigo)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NuevoAmigo.newInstance())
                .commitNow()
        }
    }

}
