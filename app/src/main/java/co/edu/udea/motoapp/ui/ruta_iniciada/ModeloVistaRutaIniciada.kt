package co.edu.udea.motoapp.ui.ruta_iniciada

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.edu.udea.motoapp.modelo.RutaPrivada

class ModeloVistaRutaIniciada : ViewModel() {
    val rutaActual = MutableLiveData<RutaPrivada>()
}
