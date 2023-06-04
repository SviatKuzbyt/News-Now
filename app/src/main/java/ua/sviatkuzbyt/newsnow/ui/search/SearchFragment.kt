package ua.sviatkuzbyt.newsnow.ui.search

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.databinding.FragmentSearchBinding
import ua.sviatkuzbyt.newsnow.ui.SharedData
import ua.sviatkuzbyt.newsnow.ui.elements.NewsListAdapter
import ua.sviatkuzbyt.newsnow.ui.elements.ProgressBarMode
import ua.sviatkuzbyt.newsnow.ui.elements.makeToast

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModels()
    private var binding: FragmentSearchBinding? = null
    private lateinit var adapter: NewsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        setupViews()
        observeViewModel()
        handleSearchConfigurationChange()
    }

    private fun setupViews() {
        binding?.apply {
            recycleSearch.layoutManager = LinearLayoutManager(requireContext())
            adapter = NewsListAdapter(mutableListOf(), requireContext(), false, viewModel)
            recycleSearch.adapter = adapter

            recycleSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()

                    if (lastVisibleItemPosition == recyclerView.adapter?.itemCount?.minus(1)
                        && viewModel.progressBarMode.value == ProgressBarMode.Nothing
                    ) {
                        viewModel.loadMoreNews()
                    }
                }
            })

            editTextSearch.setOnEditorActionListener { textView, _, _ ->
                viewModel.loadNewNews(textView.text.toString())
                hideKeyboardFrom(textView)
                textView.clearFocus()
                true
            }

            clearButton.setOnClickListener {
                editTextSearch.setText("")
            }

            editTextSearch.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                clearButton.visibility = if (hasFocus) View.VISIBLE else View.GONE
            }
        }
    }

    private fun observeViewModel() {
        viewModel.newsList.observe(viewLifecycleOwner) { list ->
            adapter.apply {
                if (viewModel.isAllDataNew) updateData(list)
                else {
                    addData(list)
                    viewModel.isAllDataNew = true
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            makeToast(activity, it)
        }

        viewModel.progressBarMode.observe(viewLifecycleOwner) { mode ->
            binding?.progressBarSearch?.visibility =
                if (mode == ProgressBarMode.Nothing) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun handleSearchConfigurationChange() {
        if (SharedData.isChangeSearchConfiguration) {
            viewModel.setRegion()
            SharedData.isChangeSearchConfiguration = false
        }
    }

    private fun hideKeyboardFrom(view: View) {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}