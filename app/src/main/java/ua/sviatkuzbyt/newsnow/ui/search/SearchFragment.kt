package ua.sviatkuzbyt.newsnow.ui.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.databinding.FragmentSearchBinding
import ua.sviatkuzbyt.newsnow.ui.elements.NewsListAdapter
import ua.sviatkuzbyt.newsnow.ui.elements.ProgressBarMode
import ua.sviatkuzbyt.newsnow.ui.elements.hideKeyboardFrom

class SearchFragment : Fragment(R.layout.fragment_search){
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: NewsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        adapter = NewsListAdapter(mutableListOf(), requireContext(), false)
        binding.recycleSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleSearch.adapter = adapter

        binding.editTextSearch.setOnEditorActionListener { textView, _, _ ->
            viewModel.loadNewNews(textView.text.toString())
            hideKeyboardFrom(activity, textView)
            textView.clearFocus()
            true
        }

        binding.clearButton.setOnClickListener {
            binding.editTextSearch.setText("")
        }

        viewModel.newsList.observe(viewLifecycleOwner) { list ->
            adapter.apply {
                if (viewModel.isAllDataNew) updateData(list)
                else {
                    addData(list)
                    viewModel.isAllDataNew = true
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }

        viewModel.progressBarMode.observe(viewLifecycleOwner) { mode ->
            when(mode) {
                ProgressBarMode.Nothing -> {
                    binding.progressBarSearch.visibility = View.INVISIBLE
                    binding.textDescription.text = getString(R.string.search_result)
                }
                else -> {
                    binding.progressBarSearch.visibility = View.VISIBLE
                    binding.textDescription.text = getString(R.string.searching)
                }
            }
        }

        binding.editTextSearch.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            binding.clearButton.visibility = if (hasFocus) View.VISIBLE else View.GONE
        }

        binding.recycleSearch.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()

                if (
                    lastVisibleItemPosition == recyclerView.adapter?.itemCount?.minus(1)
                    && viewModel.progressBarMode.value == ProgressBarMode.Nothing
                ) {
                    viewModel.loadMoreNews()
                }
            }
        })
    }
}