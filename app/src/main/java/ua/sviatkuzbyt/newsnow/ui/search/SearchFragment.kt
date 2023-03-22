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
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.changeSavedNewsForSearch
import ua.sviatkuzbyt.newsnow.ui.elements.SearchEditText
//import ua.sviatkuzbyt.newsnow.ui.elements.adapters.HistoryAdapter
//import ua.sviatkuzbyt.newsnow.ui.elements.adapters.SearchAdapter
//import ua.sviatkuzbyt.newsnow.ui.elements.hideKeyboardFrom

class SearchFragment : Fragment(){

    //publish views, vars
//    lateinit var viewModel: SearchViewModel
//    lateinit var recycleHistory: RecyclerView
//    lateinit var recycleSearch: RecyclerView
//    lateinit var progressBarSearch: ProgressBar
//    lateinit var editTextSearch: SearchEditText
//    lateinit var progressBarLoadMore: ProgressBar
//    lateinit var textDescription: TextView
//    lateinit var clearButton: Button
//    lateinit var searchAdapter: SearchAdapter
//    lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Create ViewModel
//        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        /**    INITIALIZATION VIEWS    */
//
//        //editText
//        editTextSearch = view.findViewById(R.id.editTextSearch)
//        clearButton = view.findViewById(R.id.clearButton)
//
//        //ProgressBars
//        textDescription = view.findViewById(R.id.textDescription)
//        progressBarSearch = view.findViewById(R.id.progressBarSearch)
//        progressBarLoadMore = view.findViewById(R.id.progressBarLoadSearch)
//        progressBarLoadMore.isIndeterminate = true
//
//        //recycleSearch
//
//        if (changeSavedNewsForSearch){
//            if (viewModel.listSearch.value!!.isNotEmpty()) viewModel.updateChanges()
//            changeSavedNewsForSearch = false
//        }
//
//        recycleSearch = view.findViewById(R.id.recycleSearch)
//        searchAdapter = SearchAdapter(
//            viewModel.listSearch.value!!,
//            requireActivity(),
//            viewModel
//        )
//        recycleSearch.layoutManager = LinearLayoutManager(activity)
//        recycleSearch.adapter = searchAdapter
//
//        //recycleHistory
//        recycleHistory = view.findViewById(R.id.historyRecycle)
//        historyAdapter = HistoryAdapter(
//            viewModel.listHistory.value!!,
//            viewModel,
//            this
//        )
//        recycleHistory.layoutManager = LinearLayoutManager(activity)
//        recycleHistory.adapter = historyAdapter
//
//        /**   CODE ON START FRAGMENT    */
//
//        if (viewModel.updatingSearch) editTextToSearch()
//        else if (viewModel.listSearch.value!!.isNotEmpty()){
//            showResults()
//            if(viewModel.loadMore) progressBarLoadMore.visibility = View.VISIBLE
//        }
//
//        /**    WORK EDITTEXT    */
//
//        //call search, when we touch a button in keyboard
//        editTextSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                search(editTextSearch.text.toString())
//                return@OnEditorActionListener true
//            }
//            false
//        })
//
//        //clear text, when we touch a button on EditText
//        clearButton.setOnClickListener { editTextSearch.setText("") }
//
//        //hide/show button if is/isn't focus
//        editTextSearch.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
//            if (hasFocus){
//                clearButton.visibility = View.VISIBLE
//                //show recycleHistory
//                if(recycleHistory.isGone){
//                    recycleHistory.visibility = View.VISIBLE
//                    recycleSearch.visibility = View.GONE
//                }
//            }
//            else{
//                clearButton.visibility = View.GONE
//                //hide recycleHistory
//                if (viewModel.updatingSearch || viewModel.listSearch.value!!.isNotEmpty()) hideHistory()
//            }
//        }
//
//        /**    LOAD NEWS RESULTS    */
//
//        recycleSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
//
//                if (
//                    linearLayoutManager != null
//                    && linearLayoutManager.findLastCompletelyVisibleItemPosition() == viewModel.listSearch.value!!.size - 1
//                    && !viewModel.updatingSearch
//                ) {
//                    progressBarLoadMore.visibility = View.VISIBLE //set progressbar
//                    viewModel.loadMore = true
//
//                    viewModel.loadMoreNews() //load news
//                }
//            }
//        })
//
//        /**    OBSERVES    */
//
//        //ListSearch
//        viewModel.listSearch.observe(viewLifecycleOwner) {
//
//            //Load more news
//            if (viewModel.loadModeSearch == 2) {
//                putNews(viewModel.oldSizeSearch)
//                progressBarLoadMore.visibility = View.GONE
//
//                //change operators values
//                viewModel.loadModeSearch = 0
//                viewModel.updatingSearch = false
//                viewModel.loadMore = false
//            }
//            //load search result
//            else if(viewModel.loadModeSearch == 1) {
//                searchAdapter.notifyItemRangeRemoved(
//                    0,
//                    viewModel.oldSizeSearch
//                )
//                putNews(0)
//                showResults()
//
//                //change operators values
//                viewModel.loadModeSearch = 0
//                viewModel.updatingSearch = false
//            }
//        }
//
//        //ListHistory
//        viewModel.listHistory.observe(viewLifecycleOwner){
//            val changeHistoryMode = viewModel.changeHistoryMode
//            if (changeHistoryMode != 0){
//
//                //add history
//                if (changeHistoryMode == 1){
//                    historyAdapter.notifyItemInserted(0)
//                    changeHistoryList(0)
//                }
//                //delete history
//                else {
//                    historyAdapter.notifyItemRemoved(viewModel.deleteHistory)
//                    changeHistoryList(viewModel.deleteHistory)
//                }
//                // set operator value
//                viewModel.changeHistoryMode = 0
//            }
//        }
//
//        //Error
//        viewModel.error.observe(viewLifecycleOwner) {
//            if (it != 0) {
//
//                val text = when (it) { //set error text
//                    1 -> getString(R.string.internet_error)
//                    2 -> getString(R.string.no_results)
//                    3 -> getString(R.string.no_more_result)
//                    else -> getString(R.string.wait)
//                }
//                //show error
//                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
//                showResults()
//
//                // set operators values and view
//                viewModel.error.value = 0
//                viewModel.loadModeSearch = 0
//                viewModel.updatingSearch = false
//
//                if (it == 3) {
//                    progressBarLoadMore.visibility = View.GONE
//                    viewModel.loadMore = false
//                }
//            }
//        }
//    }
//
//    //search news
//    private fun search(q: String){
//        viewModel.getNews(q)
//        editTextToSearch()
//
//        if (editTextSearch.isFocused){
//            hideKeyboardFrom(requireContext(), editTextSearch)
//            editTextSearch.clearFocus()
//        }
//    }
//
//    //change description to "updating" and views
//    private fun editTextToSearch(){
//        textDescription.text = getString(R.string.searching)
//        progressBarSearch.visibility = View.VISIBLE
//
//        if (recycleHistory.isVisible) hideHistory()
//    }
//
//    //change description to "results" and views
//    private fun showResults(){
//        textDescription.text = getString(R.string.search_result)
//        progressBarSearch.visibility = View.GONE
//
//        if (recycleHistory.isVisible) hideHistory()
//    }
//
//    //from interface, search from historyRecycle element
//    override fun searchNewsFromHistory(news: String) {
//        search(news)
//        editTextSearch.setText(news)
//    }
//
//    //add results
//    private fun putNews(startElement: Int){
//        searchAdapter.notifyItemRangeInserted(
//            startElement,
//            viewModel.listSearch.value!!.size
//        )
//    }
//
//    private fun changeHistoryList(id: Int){
//        historyAdapter.notifyItemRangeChanged(
//            id,
//            viewModel.listHistory.value!!.size
//        )
//    }
//
//    private fun hideHistory(){
//        recycleHistory.visibility = View.GONE
//        recycleSearch.visibility = View.VISIBLE
//    }
}