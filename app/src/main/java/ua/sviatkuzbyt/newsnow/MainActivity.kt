package ua.sviatkuzbyt.newsnow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigator = findViewById<BottomNavigationView>(R.id.bottom_navigator)
        val mainToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.mainToolbar)
        setSupportActionBar(mainToolbar)

        val navController = findNavController(R.id.nav_fragments)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_review,
            R.id.navigation_search,
            R.id.navigation_saved,
            R.id.navigation_setting
        ))

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigator.setupWithNavController(navController)

    }
    fun makeToast(text: String, long: Boolean){
        val toastLong = if(long) Toast.LENGTH_LONG
                        else Toast.LENGTH_SHORT
        Toast.makeText(this, text, toastLong).show()
    }
}