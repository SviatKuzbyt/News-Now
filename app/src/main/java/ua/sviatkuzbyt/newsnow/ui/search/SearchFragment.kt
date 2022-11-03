package ua.sviatkuzbyt.newsnow.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.ui.elements.SearchEditText
import ua.sviatkuzbyt.newsnow.ui.elements.adapters.HistoryAdapter
import ua.sviatkuzbyt.newsnow.ui.elements.adapters.SearchAdapter
import ua.sviatkuzbyt.newsnow.ui.elements.hideKeyboard


class SearchFragment : Fragment(), HistoryAdapter.HistoryInterface {
    lateinit var viewModel: SearchViewModel
    lateinit var recycleHistory: RecyclerView
    lateinit var recycleSearch: RecyclerView
    lateinit var progressBarSearch: ProgressBar
    lateinit var editTextSearch: SearchEditText
    lateinit var progressBarLoadSearch: ProgressBar
    lateinit var textDescription: TextView

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
        editTextSearch = view.findViewById(R.id.editTextSearch)
        textDescription = view.findViewById(R.id.textDescription)
        recycleSearch = view.findViewById(R.id.recycleSearch)
        recycleHistory = view.findViewById(R.id.historyRecycle)
        progressBarSearch = view.findViewById(R.id.progressBarSearch)

        progressBarLoadSearch = view.findViewById(R.id.progressBarLoadSearch)
        progressBarLoadSearch.isIndeterminate = true


        //RECYCLE SEARCH
        val searchAdapter = SearchAdapter(
            viewModel.listSearch.value!!, requireActivity(), viewModel
        )
        recycleSearch.layoutManager = LinearLayoutManager(activity)
        recycleSearch.adapter = searchAdapter //адаптер

        //заповнення інформації
        viewModel.listSearch.observe(viewLifecycleOwner) {
            if (viewModel.updatingSearch) {
                //дозавантаження нових новин
                if (viewModel.loadModeSearch == 2) {
                    searchAdapter.notifyItemRangeInserted(
                        viewModel.oldSizeSearch,
                        viewModel.listSearch.value!!.size
                    )
                    progressBarLoadSearch.visibility = View.GONE

                } else { //результат пошуку
                    searchAdapter.notifyItemRangeRemoved(0, viewModel.oldSizeSearch)
                    searchAdapter.notifyItemRangeInserted(0, viewModel.listSearch.value!!.size)
                    progressBarSearch.visibility = View.GONE
                }
                //оновлення індекаторів оновлення (програмних)
                viewModel.loadModeSearch = 0
                viewModel.updatingSearch = false
            }
        }

        //загрузка нових результатів
        recycleSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (
                    linearLayoutManager != null
                    && linearLayoutManager.findLastCompletelyVisibleItemPosition() == viewModel.listSearch.value!!.size - 1
                    && viewModel.loadModeSearch == 0
                ) {
                    progressBarLoadSearch.visibility = View.VISIBLE
                    viewModel.loadMoreNews()
                }
            }
        })

        //RECYCLE HISTORY
        val historyAdapter =
            HistoryAdapter(viewModel.listHistory.value!!, requireActivity(), viewModel, this)
        recycleHistory.layoutManager = LinearLayoutManager(activity)
        recycleHistory.adapter = historyAdapter
        //Видалення елементу
        viewModel.deleteHistory.observe(viewLifecycleOwner){
            historyAdapter.notifyItemRemoved(it)
            historyAdapter.notifyItemRangeChanged(it, viewModel.listHistory.value!!.size)
        }





        //Запуск пошуку
        editTextSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.getNews(editTextSearch.text.toString())
                //зміна відображення
                progressBarSearch.visibility = View.VISIBLE
                hideHistory()

                hideKeyboard(requireActivity())

                historyAdapter.notifyItemInserted(0)
                historyAdapter.notifyItemRangeChanged(0, viewModel.listHistory.value!!.size)

                return@OnEditorActionListener true
            }
            false
        })


        //обробка помилки
        viewModel.error.observe(viewLifecycleOwner) {
            if (it != 0) {
                val text = when (it) {
                        1 -> getString(R.string.internet_error)
                        2 -> "No results"
                        else -> "Please wait for end loading"
                    }

                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                if (progressBarSearch.isVisible) {
                    progressBarSearch.visibility = View.GONE
                } else progressBarLoadSearch.visibility = View.GONE

                viewModel.error.value = 0
                if (it != 3){
                    viewModel.loadModeSearch = 0
                    viewModel.updatingSearch = false
                }
            }
        }





        editTextSearch.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (!recycleHistory.isVisible) showHistory()
            } else if (viewModel.listSearch.value!!.isNotEmpty()) {
                hideHistory()
            }
        }

        if (viewModel.listSearch.value!!.isEmpty()) showHistory()
    }

    private fun showHistory(){
        recycleHistory.visibility = View.VISIBLE
        recycleSearch.visibility = View.GONE
        textDescription.text = getString(R.string.search_history)
    }

     private fun hideHistory(){
        recycleHistory.visibility = View.GONE
        recycleSearch.visibility = View.VISIBLE
        textDescription.text = getString(R.string.search_result)
    }

    override fun searchNews(news: String) {
        progressBarSearch.visibility = View.VISIBLE
        hideHistory()
        editTextSearch.setText(news)
        viewModel.getNews(news)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (viewModel.updatingSearch){
            viewModel.getNews().cancel()
            viewModel.loadMoreNews().cancel()
            viewModel.updatingSearch = false
            viewModel.loadModeSearch = 0
        }

    }
}