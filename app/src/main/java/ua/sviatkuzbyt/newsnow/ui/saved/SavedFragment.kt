package ua.sviatkuzbyt.newsnow.ui.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.databinding.FragmentSavedBinding
import ua.sviatkuzbyt.newsnow.ui.SharedData
import ua.sviatkuzbyt.newsnow.ui.elements.NewsListAdapter

class SavedFragment : Fragment(R.layout.fragment_saved) {

    private lateinit var binding: FragmentSavedBinding
    private val viewModel: SavedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedBinding.bind(view)

        val recyclerAdapter = NewsListAdapter(mutableListOf(), requireContext(), false, viewModel)
        binding.recycleViewSaved.layoutManager = LinearLayoutManager(activity)
        binding.recycleViewSaved.adapter = recyclerAdapter

        viewModel.savedList.observe(viewLifecycleOwner){
            val deleteItem = viewModel.deleteItem
            if(deleteItem == -1){
                recyclerAdapter.updateData(it)
                binding.textNoSaved.visibility = View.GONE
            }

            else {
                recyclerAdapter.notifyItemRemoved(deleteItem)
                recyclerAdapter.notifyItemRangeChanged(deleteItem,
                    recyclerAdapter.itemCount-deleteItem)
            }
            if(it.isEmpty())
                binding.textNoSaved.visibility = View.VISIBLE

            viewModel.deleteItem = -1
        }
        if(SharedData.isChangeSaved)
            viewModel.loadSavedList()
    }
}