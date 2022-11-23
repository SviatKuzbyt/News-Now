package ua.sviatkuzbyt.newsnow

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


val Context.dataStore by preferencesDataStore(name = "settings")
var changeSavedNews = false

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
}