package ua.sviatkuzbyt.newsnow.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.ui.elements.adapters.SettingValuesAdapter

class SettingValuesActivity : AppCompatActivity() {
    var mode = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_value)
        val viewModel = ViewModelProvider(this)[SettingValuesViewModel::class.java]

        val recycleSettingValues = findViewById<RecyclerView>(R.id.recycleSettingValues)

        val toolbarSettingValues = findViewById<Toolbar>(R.id.toolbarSettingValues)
        setSupportActionBar(toolbarSettingValues)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        recycleSettingValues.layoutManager = LinearLayoutManager(this)
        var adapter: SettingValuesAdapter

        val labelText = findViewById<TextView>(R.id.labelText)
        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        val settingIntent = intent
        mode = settingIntent.getIntExtra("key", 3)
        var modeString = ""

        when(mode){
            0 ->{
                viewModel.loadLanguagesList()
                labelText.text = getString(R.string.language_select)
                modeString = "language"
            }
            1 ->{
                viewModel.loadRegionsList()
                labelText.text = getString(R.string.region_select)
                modeString = "region"
            }
            else -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }

        viewModel.valuesList.observe(this){
            adapter = SettingValuesAdapter(
                viewModel.valuesList.value!!,
                viewModel,
                settingIntent.getStringExtra("selected"),
                modeString)
            recycleSettingValues.adapter = adapter
        }

        viewModel.finish.observe(this){
            if (it){
                viewModel.finish.value = false
                finish()
            }
        }
    }
}