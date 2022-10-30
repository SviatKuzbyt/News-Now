package ua.sviatkuzbyt.newsnow.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.ui.elements.adapters.HistoryAdapter
import ua.sviatkuzbyt.newsnow.ui.elements.adapters.SearchAdapter
import ua.sviatkuzbyt.newsnow.ui.elements.hideKeyboard


class SearchFragment : Fragment() {
    lateinit var viewModel: SearchViewModel
    lateinit var recycleHistory: RecyclerView
    lateinit var recycleSearch: RecyclerView
    lateinit var progressBarSearch: ProgressBar
    lateinit var textSearching: TextView
    var isKeyboard = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if(isKeyboard && viewModel.list.value!!.isNotEmpty()){
                isKeyboard = false
                recycleHistory.visibility = View.GONE
                recycleSearch.visibility = View.VISIBLE
            }
        }
        callback.handleOnBackPressed()

    }

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

        //created views
        val editTextSearch = view.findViewById<EditText>(R.id.editTextSearch)
        progressBarSearch = view.findViewById<ProgressBar>(R.id.progressBarSearch)
        textSearching = view.findViewById<TextView>(R.id.textSearching)

        val progressBarLoadSearch = view.findViewById<ProgressBar>(R.id.progressBarLoadSearch)
        progressBarLoadSearch.isIndeterminate = true

        //Створення списку новин
        recycleSearch = view.findViewById<RecyclerView>(R.id.recycleSearch)
        val searchAdapter = SearchAdapter(viewModel.list.value!!, requireActivity(), viewModel)
        recycleSearch.layoutManager = LinearLayoutManager(activity)
        recycleSearch.adapter = searchAdapter

        //Створення списку історії
        recycleHistory = view.findViewById<RecyclerView>(R.id.historyRecycle)
        val historyAdapter = HistoryAdapter(viewModel.listHistory.value!!, requireActivity(), viewModel)
        recycleHistory.layoutManager = LinearLayoutManager(activity)
        recycleHistory.adapter = historyAdapter

        if(viewModel.list.value!!.isEmpty()){
            recycleHistory.visibility = View.VISIBLE
            recycleSearch.visibility = View.GONE
        }

        //Запуск пошуку
        editTextSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.getNews(editTextSearch.text.toString())
                //зміна відображення
                progressBarSearch.visibility = View.VISIBLE
                textSearching.visibility = View.VISIBLE

                recycleHistory.visibility = View.GONE
                recycleSearch.visibility = View.VISIBLE

                hideKeyboard(requireActivity())
                isKeyboard = false

                return@OnEditorActionListener true
            }
            false
        })
        //Відображення історії при пошуку
        editTextSearch.setOnClickListener {

            if (!recycleHistory.isVisible){
                recycleHistory.visibility = View.VISIBLE
                recycleSearch.visibility = View.GONE
                isKeyboard = true
            }
        }


        //оновлення списку
        viewModel.list.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                if (viewModel.loadmore) {
                    searchAdapter.notifyItemRangeInserted(viewModel.oldSizeSearch, viewModel.list.value!!.size)
                    progressBarLoadSearch.visibility = View.GONE
                    viewModel.loadmore = false
                }
                else{
                    searchAdapter.notifyItemRangeRemoved(0, viewModel.oldSizeSearch)
                    searchAdapter.notifyItemRangeInserted(0, viewModel.list.value!!.size)
                }

                //зміна відображення
                hideLoad()
                viewModel.updating = false
            }
        }
        //обробка помилки
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

        recycleSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (
                    linearLayoutManager != null
                    && linearLayoutManager.findLastCompletelyVisibleItemPosition() == viewModel.list.value!!.size - 1
                    && !viewModel.updating
                ) {
                    //on progressbar and call function in VM
                    progressBarLoadSearch.visibility = View.VISIBLE
                    viewModel.loadmore = true
                    viewModel.getNews()
                    viewModel.updating = true
                }
            }
        })

        //видалення елементу
        viewModel.deleteHistory.observe(viewLifecycleOwner){
            historyAdapter.notifyItemRemoved(it)
            historyAdapter.notifyItemRangeChanged(it, viewModel.listHistory.value!!.size)
        }

        viewModel.emptySearch.observe(viewLifecycleOwner){
            if (viewModel.loadmore){
                progressBarLoadSearch.visibility = View.GONE

            }
            else{
                hideLoad()
            }
            Toast.makeText(context, "No results", Toast.LENGTH_SHORT).show()


        }
    }
    private fun hideLoad(){
        progressBarSearch.visibility = View.GONE
        textSearching.visibility = View.GONE
    }

}