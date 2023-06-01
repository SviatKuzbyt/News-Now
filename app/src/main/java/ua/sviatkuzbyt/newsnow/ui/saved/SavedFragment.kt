package ua.sviatkuzbyt.newsnow.ui.saved

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.changeSavedNews
import ua.sviatkuzbyt.newsnow.databinding.FragmentSavedBinding
import ua.sviatkuzbyt.newsnow.databinding.FragmentSearchBinding
import ua.sviatkuzbyt.newsnow.ui.elements.NewsListAdapter

class SavedFragment : Fragment(R.layout.fragment_saved) {

    private lateinit var binding: FragmentSavedBinding
    private val viewModel by viewModels<SavedViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedBinding.bind(view)

        binding.savedRefresh.setColorSchemeResources(R.color.blue)
        val color = MaterialColors.getColor(requireContext(), android.R.attr.windowBackground, Color.WHITE)
        binding.savedRefresh.setProgressBackgroundColorSchemeColor(color)
        binding.savedRefresh.setOnRefreshListener {
            viewModel.loadSavedList()
            binding.savedRefresh.isRefreshing = false
        }

        val recyclerAdapter = NewsListAdapter(mutableListOf(), requireContext(), false, viewModel)
        binding.recycleViewSaved.layoutManager = LinearLayoutManager(activity)
        binding.recycleViewSaved.adapter = recyclerAdapter
        viewModel.savedList.observe(viewLifecycleOwner){
            recyclerAdapter.updateData(it)
        }
    }

}