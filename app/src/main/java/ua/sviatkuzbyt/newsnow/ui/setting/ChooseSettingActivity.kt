package ua.sviatkuzbyt.newsnow.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.ui.elements.ChooseSettingAdapter

//import ua.sviatkuzbyt.newsnow.ui.elements.adapters.SettingValuesAdapter
//import ua.sviatkuzbyt.newsnow.ui.elements.isDarkThemeOn

class ChooseSettingActivity : AppCompatActivity() {

    val viewModel by viewModels<ChooseSettingViewModel>()
    val recycler: RecyclerView by lazy { findViewById(R.id.recycleChooseSetting) }
    val btnBack: Button by lazy { findViewById(R.id.btnBack) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_setting)

        btnBack.setOnClickListener { finish() }

        recycler.layoutManager = LinearLayoutManager(this)

        viewModel.currentRegion.observe(this){
            if(it == "finish")
                finish()
            else
                recycler.adapter = ChooseSettingAdapter(viewModel.listRegion, viewModel, it)
        }



//        /**    INITIALIZATION VIEWS AND VARS    */
//
//        //ViewModel
//        val viewModel = ViewModelProvider(this)[SettingValuesViewModel::class.java]
//
//        //List
//        val recycleSettingValues = findViewById<RecyclerView>(R.id.recycleSettingValues)
//        recycleSettingValues.layoutManager = LinearLayoutManager(this)
//        var adapter: SettingValuesAdapter
//        val color = if(isDarkThemeOn()) "#FFFFFFFF" else "#FF000000"
//
//        //Toolbar
//        val toolbarSettingValues = findViewById<Toolbar>(R.id.toolbarSettingValues)
//        setSupportActionBar(toolbarSettingValues)
//        supportActionBar?.setDisplayShowTitleEnabled(false) //delete standard view
//        val labelText = findViewById<TextView>(R.id.labelText) // text on toolbar
//        //button to back
//        val btnBack = findViewById<Button>(R.id.btnBack)
//        btnBack.setOnClickListener { finish() }
//
//
//        /**    INITIALIZATION CONTENT    */
//        //create and get values with content to fill
//        val settingIntent = intent
//        val mode = settingIntent.getIntExtra("key", 3)
//        var modeString = ""
//
//        //apply content: load list and text on toolbar
//        when(mode){
//            0 ->{
//                viewModel.loadLanguagesList()
//                labelText.text = getString(R.string.language_select)
//                modeString = "language"
//            }
//            1 ->{
//                viewModel.loadRegionsList()
//                labelText.text = getString(R.string.region_select)
//                modeString = "region"
//            }
//            else -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
//        }
//
//        /**    OBSERVES    */
//        //list, apply adapter
//        viewModel.valuesList.observe(this){
//            adapter = SettingValuesAdapter(
//                viewModel.valuesList.value!!,
//                viewModel,
//                settingIntent.getStringExtra("selected"),
//                modeString,
//                color)
//            recycleSettingValues.adapter = adapter
//        }
//
//        //finish, close activity when element is selected
//        viewModel.finish.observe(this){
//            if (it){
//                viewModel.finish.value = false
//                finish()
//            }
//        }
    }
}