package ua.sviatkuzbyt.newsnow.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.ui.elements.ChooseSettingAdapter

class ChooseSettingActivity : AppCompatActivity() {
    private val viewModel by viewModels<ChooseSettingViewModel>()
    private val recycler: RecyclerView by lazy { findViewById(R.id.recycleChooseSetting) }
    private val btnBack: Button by lazy { findViewById(R.id.btnBack) }

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
    }
}