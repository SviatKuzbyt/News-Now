package ua.sviatkuzbyt.newsnow.ui.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.ui.elements.adapters.SavedAdapter
import ua.sviatkuzbyt.newsnow.changeSavedNews

class SavedFragment : Fragment() {

    lateinit var viewModel: SavedViewModel
    lateinit var adapter: SavedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Created ViewModel
        viewModel = ViewModelProvider(this)[SavedViewModel::class.java]
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /**    INITIALIZATION VIEWS    */
        val textNoSaved = view.findViewById<TextView>(R.id.textNoSaved)
        //recyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleViewSaved)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        /**   CODE ON START FRAGMENT    */
        //update saved news, when we add/delete saved news from another fragment
        if (changeSavedNews){
            viewModel.updateSavedNews()
            changeSavedNews = false
        }

        /**    OBSERVES    */
        //list
        viewModel.list.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                //set adapter and elements
                adapter = SavedAdapter(viewModel.list.value!!, requireActivity(), viewModel)
                recyclerView.adapter = adapter

                //change textNoSaved
                if (textNoSaved.isVisible) textNoSaved.visibility = View.GONE
            } else textNoSaved.visibility = View.VISIBLE
        }

        //delete
        viewModel.deleteElement.observe(viewLifecycleOwner){
            if (it != null){
                adapter.notifyItemRemoved(it)
                adapter.notifyItemRangeChanged(it, viewModel.list.value!!.size)
                viewModel.deleteElement.value = null
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}