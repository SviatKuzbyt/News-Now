package ua.sviatkuzbyt.newsnow.ui.saved

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.data.database.updateDataBaseFromReview
import ua.sviatkuzbyt.newsnow.ui.elements.SavedAdapter


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
        var first = true
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleViewSaved)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarSaved)
        var adapter: SavedAdapter? = null
        recyclerView.layoutManager = LinearLayoutManager(activity)

        if (updateDataBaseFromReview){
            viewModel.updateSavedNews()
            updateDataBaseFromReview = false
        }

        viewModel.list.observe(viewLifecycleOwner){
            if (it.isNotEmpty() && first){
                adapter = SavedAdapter(viewModel.list.value!!, requireActivity(), viewModel)
                recyclerView.adapter = adapter
//                   adapter.notifyItemRangeInserted(1, it.size)
                Log.v("Observe", "додав елементи")
                first = false
            }
        }

        viewModel.deleteElement.observe(viewLifecycleOwner){
            adapter?.notifyItemRemoved(it)
            adapter?.notifyItemRangeChanged(it, viewModel.list.value!!.size)
            Log.v("Observe", it.toString())
        }

        super.onViewCreated(view, savedInstanceState)
    }

}