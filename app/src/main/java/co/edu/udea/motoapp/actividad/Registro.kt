package co.edu.udea.motoapp.actividad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.ui.registro.Registro

class Registro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_registro)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, Registro.newInstance())
                .commitNow()
        }
    }
}
