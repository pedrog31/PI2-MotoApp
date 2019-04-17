package co.edu.udea.motoapp.actividad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import co.edu.udea.motoapp.R
import co.edu.udea.motoapp.ui.inicio.Inicio
import co.edu.udea.motoapp.ui.lista_amigos.ListaAmigos
import co.edu.udea.motoapp.ui.rutas_privadas.ListaRutasPrivadas
import com.firebase.ui.auth.AuthUI

import kotlinx.android.synthetic.main.actividad_principal.*

class Principal : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_principal)
        setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        tabs_principal.setupWithViewPager(container)
        container.addOnPageChangeListener(this@Principal)
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
                    startActivity(Intent(this@Principal, Autenticacion::class.java))
                }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(posicion: Int, positionOffset: Float, positionOffsetPixels: Int) {
        this@Principal.actualizarBotonFlotante(posicion)
    }

    override fun onPageSelected(posicion: Int) {
        actualizarBotonFlotante(posicion)
    }

    private fun actualizarBotonFlotante(posicion: Int) {
        when (posicion) {
            1 -> {
                principalBotonFlotante.setImageDrawable(this@Principal.getDrawable(R.drawable.ic_explorar_rutas))
                principalBotonFlotante.setOnClickListener {
                    startActivity(Intent(this@Principal, MapaRutasPublicas::class.java))
                }
            }
            0 -> {
                principalBotonFlotante.setImageDrawable(this@Principal.getDrawable(R.drawable.ic_anadir_amigo))
                principalBotonFlotante.show()
                principalBotonFlotante.setOnClickListener {
                    startActivity(Intent(this@Principal, NuevaRuta::class.java))
                }
            }

            2 -> {
                principalBotonFlotante.setImageDrawable(this@Principal.getDrawable(R.drawable.ic_anadir_amigo))
                principalBotonFlotante.setOnClickListener {
                    startActivity(Intent(this@Principal, NuevoAmigo::class.java))
                }
            }

            else -> {
                principalBotonFlotante.setOnClickListener {  }
            }
        }
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> ListaRutasPrivadas.nuevaInstancia()
                1 -> Inicio.nuevaInstancia()
                2 -> ListaAmigos.nuevaInstancia()
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
