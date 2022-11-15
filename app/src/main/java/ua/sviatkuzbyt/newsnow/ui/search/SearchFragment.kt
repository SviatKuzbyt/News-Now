package ua.sviatkuzbyt.newsnow.ui.search


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
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
import ua.sviatkuzbyt.newsnow.ui.elements.hideKeyboardFrom


class SearchFragment : Fragment(), HistoryAdapter.HistoryInterface {

    lateinit var viewModel: SearchViewModel
    lateinit var recycleHistory: RecyclerView
    lateinit var recycleSearch: RecyclerView
    lateinit var progressBarSearch: ProgressBar
    lateinit var editTextSearch: SearchEditText
    lateinit var progressBarLoadSearch: ProgressBar
    lateinit var textDescription: TextView
    lateinit var clearButton: Button
    lateinit var searchAdapter: SearchAdapter

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
        clearButton = view.findViewById(R.id.clearButton)

        recycleSearch = view.findViewById(R.id.recycleSearch)
        recycleHistory = view.findViewById(R.id.historyRecycle)

        progressBarSearch = view.findViewById(R.id.progressBarSearch)
        progressBarLoadSearch = view.findViewById(R.id.progressBarLoadSearch)
        progressBarLoadSearch.isIndeterminate = true

        /*      RECYCLE SEARCH    */
        //адаптер
        searchAdapter = SearchAdapter(
            viewModel.listSearch.value!!,
            requireActivity(),
            viewModel
        )
        recycleSearch.layoutManager = LinearLayoutManager(activity)
        recycleSearch.adapter = searchAdapter

        //заповнення інформації
        viewModel.listSearch.observe(viewLifecycleOwner) {
            if (viewModel.updatingSearch) {

                //дозавантаження нових новин
                if (viewModel.loadModeSearch == 2) {
                    putNews(viewModel.oldSizeSearch)
                    progressBarLoadSearch.visibility = View.GONE

                } else { //результат пошуку
                    searchAdapter.notifyItemRangeRemoved( //delete all
                        0,
                        viewModel.oldSizeSearch
                    )
                    putNews(0)
                    progressBarSearch.visibility = View.GONE
                    textDescription.text = getString(R.string.search_result)
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
                    && linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    == viewModel.listSearch.value!!.size - 1
                    && viewModel.loadModeSearch == 0
                ) {
                    progressBarLoadSearch.visibility = View.VISIBLE //set progressbar
                    viewModel.loadMoreNews() //load news
                }
            }
        })

        /*      RECYCLE HISTORY     */
        //adapter
        val historyAdapter = HistoryAdapter(
            viewModel.listHistory.value!!,
            viewModel,
            this
        )
        recycleHistory.layoutManager = LinearLayoutManager(activity)
        recycleHistory.adapter = historyAdapter

        //загрузка історії
        viewModel.listHistory.observe(viewLifecycleOwner){
            val changeHistoryMode = viewModel.changeHistoryMode
            if (changeHistoryMode != 0){
                //додання історії
                if (changeHistoryMode == 1){
                    historyAdapter.notifyItemInserted(0)
                    historyAdapter.notifyItemRangeChanged(
                        0,
                        viewModel.listHistory.value!!.size
                    )

                } //видалення історії
                else {
                    val deleteHistory = viewModel.deleteHistory
                    historyAdapter.notifyItemRemoved(deleteHistory)

                    historyAdapter.notifyItemRangeChanged(
                        deleteHistory,
                        viewModel.listHistory.value!!.size
                    )
                }
                viewModel.changeHistoryMode = 0 //встановлення режиму
            }
        }

        /*      EDIT TEXT SEARCH      */
        editTextSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                viewModel.getNews(editTextSearch.text.toString())
                //зміна відображення
                progressBarSearch.visibility = View.VISIBLE
                editTextSearch.clearFocus()
                hideKeyboardFrom(requireContext(), editTextSearch)

                return@OnEditorActionListener true
            }
            false
        })
        //delete text in edit text
        clearButton.setOnClickListener { editTextSearch.setText("") }

        //закритя/відкриття історії відповідно до фокусу
        editTextSearch.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                clearButton.visibility = View.VISIBLE
                if (!recycleHistory.isVisible) showHistory()
            }
            else {
                clearButton.visibility = View.GONE
                if (viewModel.updatingSearch || viewModel.listSearch.value!!.isNotEmpty()){
                    hideHistory()
                    Toast.makeText(context, "text", Toast.LENGTH_SHORT).show()
                }

            }
        }

        //ERROR
        viewModel.error.observe(viewLifecycleOwner) {
            //show message
            if (it != 0) {
                val text = when (it) {
                        1 -> getString(R.string.internet_error)
                        2 -> getString(R.string.no_results)
                        3 -> getString(R.string.no_more_result)
                        else -> getString(R.string.wait)
                    }
                textDescription.text = getString(R.string.search_result)
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

                //hide views
                if (progressBarSearch.isVisible) {
                    progressBarSearch.visibility = View.GONE
                } else progressBarLoadSearch.visibility = View.GONE

                //set modes in vm
                viewModel.error.value = 0
                if (it != 3){
                    viewModel.loadModeSearch = 0
                    viewModel.updatingSearch = false
                }
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
         if (viewModel.updatingSearch) textDescription.text = getString(R.string.searching)
         else textDescription.text = getString(R.string.search_result)
    }

    override fun searchNews(news: String) {
        if (editTextSearch.isFocused){
            hideKeyboardFrom(requireContext(), editTextSearch)
            editTextSearch.clearFocus()
        }
        hideHistory()

        progressBarSearch.visibility = View.VISIBLE
        editTextSearch.setText(news)
        viewModel.getNews(news)

    }

    private fun putNews(startElement: Int){
        searchAdapter.notifyItemRangeInserted( //put all
            startElement,
            viewModel.listSearch.value!!.size
        )
    }
}