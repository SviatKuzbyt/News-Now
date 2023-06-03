package ua.sviatkuzbyt.newsnow.ui.setting

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.databinding.FragmentSettingBinding

class SettingFragment : Fragment(R.layout.fragment_setting) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel by viewModels<SettingViewModel>()
        val binding = FragmentSettingBinding.bind(view)

        viewModel.searchInAll.observe(viewLifecycleOwner){
            binding.checkBox.isChecked = it
        }

        binding.checkBox.setOnClickListener {
            viewModel.setSearchInAll(binding.checkBox.isChecked)
        }

        binding.textRegion.setOnClickListener {
            startActivity(Intent(activity,ChooseSettingActivity::class.java))
        }

        binding.textAbout.movementMethod = LinkMovementMethod.getInstance()
    }
}