package ua.sviatkuzbyt.newsnow.ui.setting

import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.changeSetting
import ua.sviatkuzbyt.newsnow.ui.elements.adapters.SettingAdapter

class SettingFragment : Fragment() {

    //publish views, vars
    lateinit var viewModel: SettingViewModel
    lateinit var recycleSetting: RecyclerView
    lateinit var adapter: SettingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // create ViewModel
        viewModel = ViewModelProvider(this)[SettingViewModel::class.java]
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initializing views
        recycleSetting = view.findViewById(R.id.recycleSetting)
        recycleSetting.layoutManager = LinearLayoutManager(activity)

        val textLinks = view.findViewById<TextView>(R.id.textLinks)
        textLinks.movementMethod = LinkMovementMethod.getInstance()

        //observe settingList
        viewModel.settingList.observe(viewLifecycleOwner){
            adapter = SettingAdapter(it, requireActivity())
            recycleSetting.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        if (changeSetting != null){
            //updating settings text
            when(changeSetting){
                "language" -> viewModel.changeLanguage()
                "region" -> viewModel.changeRegion()
            }
            changeSetting = null
        }
    }
}