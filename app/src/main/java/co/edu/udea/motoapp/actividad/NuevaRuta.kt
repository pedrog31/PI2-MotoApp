package co.edu.udea.motoapp.actividad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.ui.nuevaruta.NuevaRuta
import com.google.android.gms.common.api.Status

import java.util.*

class NuevaRuta : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_nueva_ruta)



        // Create a new Places client instance.
        //val placesClient = Places.createClient(this)
        //val autocompleteFragment1 =
        //    supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        //val autocompleteFragment = this.supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        //autocompleteFragment1?.setOnPlaceSelectedListener(this)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NuevaRuta.newInstance())
                .commitNow()
        }
    }

}
