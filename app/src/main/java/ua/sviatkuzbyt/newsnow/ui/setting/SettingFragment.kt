package ua.sviatkuzbyt.newsnow.ui.setting

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.databinding.FragmentSettingBinding
import ua.sviatkuzbyt.newsnow.ui.SharedData

class SettingFragment : Fragment(R.layout.fragment_setting) {

    private val viewModel by viewModels<SettingViewModel>()
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingBinding.bind(view)

        viewModel.searchInAll.observe(viewLifecycleOwner){
            binding.checkBox.isChecked = it
            SharedData.isChangeSearchConfiguration = true
        }

        binding.checkBox.setOnClickListener {
            viewModel.setSearchInAll(binding.checkBox.isChecked)
        }

        binding.textRegion.setOnClickListener {
            startActivity(Intent(activity,ChooseSettingActivity::class.java))
        }

        binding.textAbout.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}