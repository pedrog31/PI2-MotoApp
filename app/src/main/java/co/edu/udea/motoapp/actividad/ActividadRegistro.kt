package co.edu.udea.motoapp.actividad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.ui.registro.FragmentoRegistro

class ActividadRegistro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_registro)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FragmentoRegistro.newInstance())
                .commitNow()
        }
    }
}
