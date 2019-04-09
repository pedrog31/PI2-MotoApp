package co.edu.udea.motoapp

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class MotoApp: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}