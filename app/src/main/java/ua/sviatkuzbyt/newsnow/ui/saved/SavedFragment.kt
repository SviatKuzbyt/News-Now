package ua.sviatkuzbyt.newsnow.ui.saved

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.data.database.updateDataBaseFromReview
import ua.sviatkuzbyt.newsnow.ui.elements.adapters.SavedAdapter

class SavedFragment : Fragment() {

    lateinit var viewModel: SavedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[SavedViewModel::class.java]
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleViewSaved)
        var adapter: SavedAdapter? = null
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val textNoSaved = view.findViewById<TextView>(R.id.textNoSaved)

        if (updateDataBaseFromReview){
            viewModel.updateSavedNews()
            updateDataBaseFromReview = false
        }


        viewModel.list.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                adapter = SavedAdapter(viewModel.list.value!!, requireActivity(), viewModel)
                recyclerView.adapter = adapter
                if (textNoSaved.isVisible) textNoSaved.visibility = View.GONE
            } else textNoSaved.visibility = View.VISIBLE
        }

        viewModel.deleteElement.observe(viewLifecycleOwner){
            adapter?.notifyItemRemoved(it)
            adapter?.notifyItemRangeChanged(it, viewModel.list.value!!.size)
        }

        super.onViewCreated(view, savedInstanceState)
    }
}