package ua.sviatkuzbyt.newsnow.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.ui.elements.adapters.SettingAdapter

var change: String? = null
class SettingFragment : Fragment() {

    lateinit var viewModel: SettingViewModel
    lateinit var recycleSetting: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this)[SettingViewModel::class.java]
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycleSetting = view.findViewById(R.id.recycleSetting)
        var adapter: SettingAdapter
        recycleSetting.layoutManager = LinearLayoutManager(activity)

        viewModel.settingList.observe(viewLifecycleOwner){
            adapter = SettingAdapter(it, requireActivity())
            recycleSetting.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        if (change != null){
            when(change){
                "language" -> viewModel.changeLanguage()
                "region" -> viewModel.changeRegion()
            }
            change = null
        }
    }
}