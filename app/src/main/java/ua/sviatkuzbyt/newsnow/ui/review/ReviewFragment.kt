package ua.sviatkuzbyt.newsnow.ui.review

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.color.MaterialColors
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.ui.elements.NewsListAdapter
import ua.sviatkuzbyt.newsnow.ui.elements.ProgressBarMode


class ReviewFragment : Fragment(R.layout.fragment_review) {

    private val viewModel by viewModels<ReviewViewModel>()
    private lateinit var recycleViewReview: RecyclerView
    private lateinit var progressBarLoadMore: ProgressBar
    private lateinit var refreshReview: SwipeRefreshLayout
    private lateinit var adapter: NewsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews(view)
        setListeners()
        setViewModel()
    }

    @SuppressLint("ResourceAsColor")
    private fun setViews(view: View){
        recycleViewReview = view.findViewById(R.id.recycleViewReview)
        recycleViewReview.layoutManager = LinearLayoutManager(activity)
        adapter = NewsListAdapter(mutableListOf(), requireActivity(), true)
        recycleViewReview.adapter = adapter

        progressBarLoadMore = view.findViewById(R.id.progressBarLoadMore)
        progressBarLoadMore.isIndeterminate = true

        refreshReview = view.findViewById(R.id.refreshReview)
        refreshReview.setColorSchemeResources(R.color.blue)
        val color = MaterialColors.getColor(requireContext(), android.R.attr.windowBackground, Color.WHITE)
        refreshReview.setProgressBackgroundColorSchemeColor(color)

    }

    private fun setListeners(){
        recycleViewReview.addOnScrollListener(object: RecyclerView.OnScrollListener() {
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

        refreshReview.setOnRefreshListener{
            viewModel.loadNewNews()
        }
    }

    private fun setViewModel(){
        viewModel.newsList.observe(viewLifecycleOwner) {
            adapter.apply {
                if (viewModel.isAllDataNew) updateData(it)
                else{
                    addData(it)
                    viewModel.isAllDataNew = true
                }
            }
        }

        viewModel.progressBarMode.observe(viewLifecycleOwner) {
            when(it){
                ProgressBarMode.LoadMore -> progressBarLoadMore.visibility = View.VISIBLE
                ProgressBarMode.LoadNew -> refreshReview.isRefreshing = true
                ProgressBarMode.Nothing -> hideViews()
            }
        }

        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun hideViews(){
        if(progressBarLoadMore.isVisible) progressBarLoadMore.visibility = View.GONE
        else if(refreshReview.isRefreshing) refreshReview.isRefreshing = false
    }
}