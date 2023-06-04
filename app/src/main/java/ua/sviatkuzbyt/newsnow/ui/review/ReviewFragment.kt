package ua.sviatkuzbyt.newsnow.ui.review

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.databinding.FragmentReviewBinding
import ua.sviatkuzbyt.newsnow.ui.SharedData
import ua.sviatkuzbyt.newsnow.ui.elements.NewsListAdapter
import ua.sviatkuzbyt.newsnow.ui.elements.ProgressBarMode
import ua.sviatkuzbyt.newsnow.ui.elements.makeToast

class ReviewFragment : Fragment(R.layout.fragment_review) {

    private val viewModel: ReviewViewModel by viewModels()
    private lateinit var adapter: NewsListAdapter
    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReviewBinding.bind(view)

        setViews()
        setListeners()
        setViewModel()

        if(SharedData.isChangeRegion)
            viewModel.loadNewNews()
    }

    @SuppressLint("ResourceAsColor")
    private fun setViews(){
        binding.recycleViewReview.layoutManager = LinearLayoutManager(activity)
        adapter = NewsListAdapter(mutableListOf(), requireActivity(), true, viewModel)
        binding.recycleViewReview.adapter = adapter

        binding.progressBarLoadMore.isIndeterminate = true

        binding.refreshReview.setColorSchemeResources(R.color.blue)
        binding.refreshReview.setProgressBackgroundColorSchemeColor(
            MaterialColors.getColor(requireContext(), android.R.attr.windowBackground, Color.WHITE)
        )
    }

    private fun setListeners(){
        binding.recycleViewReview.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()

                if(
                    lastVisibleItemPosition == recyclerView.adapter?.itemCount?.minus(1)
                    && viewModel.progressBarMode.value == ProgressBarMode.Nothing
                ) viewModel.loadMoreNews()
            }
        })

        binding.refreshReview.setOnRefreshListener{
            viewModel.loadNewNews()
        }
    }

    private fun setViewModel(){
        viewModel.newsList.observe(viewLifecycleOwner) {
            if(it.isEmpty()) viewModel.loadNewNews()
            else{
                adapter.apply {
                    if (viewModel.isAllDataNew) updateData(it)
                    else{
                        addData(it)
                        viewModel.isAllDataNew = true
                    }
                }
            }
        }

        viewModel.progressBarMode.observe(viewLifecycleOwner) {
            when(it){
                ProgressBarMode.LoadMore -> binding.progressBarLoadMore.visibility = View.VISIBLE
                ProgressBarMode.LoadNew -> binding.refreshReview.isRefreshing = true
                ProgressBarMode.Nothing -> hideViews()
            }
        }

        viewModel.error.observe(viewLifecycleOwner){
            makeToast(activity, it)
        }
    }

    private fun hideViews(){
        if(binding.progressBarLoadMore.isVisible) binding.progressBarLoadMore.visibility = View.GONE
        else if(binding.refreshReview.isRefreshing) binding.refreshReview.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}