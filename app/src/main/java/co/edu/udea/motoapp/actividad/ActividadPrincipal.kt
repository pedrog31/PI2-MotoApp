package co.edu.udea.motoapp.actividad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.ui.inicio.FragmentInicio
import co.edu.udea.motoapp.ui.lista_amigos.FragmentoListaAmigos
import com.firebase.ui.auth.AuthUI

import kotlinx.android.synthetic.main.actividad_principal.*

class ActividadPrincipal : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_principal)
        setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        tabs_principal.setupWithViewPager(container)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.accion_cerrar_sesion) {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    startActivity(Intent(this@ActividadPrincipal, ActividadAutenticacion::class.java))
                }
        }

        return super.onOptionsItemSelected(item)
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                1 -> FragmentInicio.nuevaInstancia()
                2 -> FragmentoListaAmigos ()
                else -> Fragment()
            }
        }

        override fun getCount(): Int {
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> getString(R.string.tab_principal_rutas)
                1 -> getString(R.string.tab_principal_inicio)
                2 -> getString(R.string.tab_principal_amigos)
                else -> ""
            }
        }
    }
}
