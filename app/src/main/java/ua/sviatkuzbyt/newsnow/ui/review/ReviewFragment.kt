package ua.sviatkuzbyt.newsnow.ui.review

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.data.NewsContainer
import ua.sviatkuzbyt.newsnow.ui.elements.NewsRecycleViewAdapter


class ReviewFragment : Fragment() {

    lateinit var viewModel: ReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Created VM
        viewModel = ViewModelProvider(this)[ReviewViewModel::class.java]
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBarLoadMore = view.findViewById<ProgressBar>(R.id.progressBarLoadMore)
        progressBarLoadMore.isIndeterminate = true

        //Set SwipeRefresh, options and work
        val refreshReview = view.findViewById<SwipeRefreshLayout>(R.id.refreshReview)
        val progressBarReview = view.findViewById<ProgressBar>(R.id.progressBarReview)
        if (!viewModel.isUpdated) progressBarReview.visibility = View.VISIBLE

        refreshReview.setColorSchemeResources(R.color.blue)
        refreshReview.setOnRefreshListener {
            if (!progressBarLoadMore.isVisible && !progressBarReview.isVisible) viewModel.firstUpdate()
            else{
                refreshReview.isRefreshing = false
                Log.v("refresh", "Все ахуєнно")
            }
        }

        //Set progress bar and show it on start fragment


        //Set recycleView
        val recycleViewReview = view.findViewById<RecyclerView>(R.id.recycleViewReview)
        recycleViewReview.layoutManager = LinearLayoutManager(activity)
        val adapter = NewsRecycleViewAdapter(viewModel.list.value!!, requireActivity())
        recycleViewReview.adapter = adapter



        //VM subscription for RecycleView

        viewModel.list.observe(viewLifecycleOwner){
            if(refreshReview.isRefreshing) adapter.notifyDataSetChanged()
            else adapter.notifyItemRangeInserted(it.size - viewModel.newElements, viewModel.newElements)

            Log.v("1123212", "updated")
            if (!viewModel.isUpdated && it.isNotEmpty()){

                //animation hiding progress bar and showing recycleView
                ObjectAnimator.ofFloat(progressBarReview, View.ALPHA, 1f, 0f).apply {
                    duration = 1000
                    start()
                }
                ObjectAnimator.ofFloat(recycleViewReview, View.ALPHA, 0f, 1f).apply {
                    duration = 1000
                    start()
                }

                viewModel.isUpdated = true //hide progress bar
            }

            else if (progressBarLoadMore.isVisible) progressBarLoadMore.visibility = View.GONE
            else refreshReview.isRefreshing = false //hide SwipeRefresh
            Log.v("review", it.toString())


        }

        recycleViewReview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    if (
                        linearLayoutManager != null
                        && linearLayoutManager.findLastCompletelyVisibleItemPosition() == viewModel.list.value!!.size - 1
                        && !refreshReview.isRefreshing
                    ) {
                        progressBarLoadMore.visibility = View.VISIBLE
                        viewModel.update()
                    }
                }

        })
    }
}