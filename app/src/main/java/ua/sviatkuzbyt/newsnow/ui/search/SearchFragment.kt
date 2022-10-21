package ua.sviatkuzbyt.newsnow.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.ui.elements.SearchAdapter
import ua.sviatkuzbyt.newsnow.ui.elements.hideKeyboard


class SearchFragment : Fragment() {
    lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextSearch = view.findViewById<EditText>(R.id.editTextSearch)
        val recycleSearch = view.findViewById<RecyclerView>(R.id.recycleSearch)
        val progressBarSearch = view.findViewById<ProgressBar>(R.id.progressBarSearch)
        val textSearching = view.findViewById<TextView>(R.id.textSearching)

        val adapter = SearchAdapter(viewModel.list.value!!, requireActivity(), viewModel)
        recycleSearch.layoutManager = LinearLayoutManager(activity)
        recycleSearch.adapter = adapter

        editTextSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.getNews(editTextSearch.text.toString())
                progressBarSearch.visibility = View.VISIBLE
                textSearching.visibility = View.VISIBLE
                hideKeyboard(requireActivity())
                return@OnEditorActionListener true
            }
            false
        })

        viewModel.list.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                adapter.notifyItemRangeRemoved(0, viewModel.oldSize)
                adapter.notifyItemRangeInserted(0, viewModel.list.value!!.size)

                progressBarSearch.visibility = View.GONE
                textSearching.visibility = View.GONE
            }
        }

        viewModel.error.observe(viewLifecycleOwner){
            if (it == true){
                Toast.makeText(context, getText(R.string.internet_error), Toast.LENGTH_SHORT).show()
                if (progressBarSearch.isVisible){
                    textSearching.visibility = View.GONE
                    progressBarSearch.visibility = View.GONE
                }
                viewModel.error.value = false
            }
        }
    }

}