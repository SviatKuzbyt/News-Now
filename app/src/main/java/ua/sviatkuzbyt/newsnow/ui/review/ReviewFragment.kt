package ua.sviatkuzbyt.newsnow.ui.review

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ua.sviatkuzbyt.newsnow.R
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

        //Created progressBars
        val progressBarLoadMore = view.findViewById<ProgressBar>(R.id.progressBarLoadMore)
        progressBarLoadMore.isIndeterminate = true

        val progressBarReview = view.findViewById<ProgressBar>(R.id.progressBarReview)
        //set visible for progress bar
        if (!viewModel.isUpdated) progressBarReview.visibility = View.VISIBLE

        //Set SwipeRefresh, options and work
        val refreshReview = view.findViewById<SwipeRefreshLayout>(R.id.refreshReview)
        refreshReview.setColorSchemeResources(R.color.blue)

        refreshReview.setOnRefreshListener {
            if (progressBarReview.isVisible || progressBarLoadMore.isVisible)
                refreshReview.isRefreshing = false
            else viewModel.firstUpdate()
        }

        //Set RecycleView
        val recycleViewReview = view.findViewById<RecyclerView>(R.id.recycleViewReview)
        recycleViewReview.layoutManager = LinearLayoutManager(activity)
        //Set adapter
        val adapter = NewsRecycleViewAdapter(viewModel.list.value!!, requireActivity())
        recycleViewReview.adapter = adapter

        //set loading new news, when list is scrolled down
        recycleViewReview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (
                    linearLayoutManager != null
                    && linearLayoutManager.findLastCompletelyVisibleItemPosition() == viewModel.list.value!!.size - 1
                    && !refreshReview.isRefreshing
                ) {
                    //on progressbar and call function in VM
                    progressBarLoadMore.visibility = View.VISIBLE
                    viewModel.update()
                }
            }
        })

        //update information, list,
        viewModel.list.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                //when we call update info
                if(refreshReview.isRefreshing){
                    //update view list
                    adapter.notifyItemRangeRemoved(0, viewModel.oldSize)
                    adapter.notifyItemRangeInserted(0, viewModel.newElements)
                    refreshReview.isRefreshing = false
                }
                else{
                    //update view list
                    adapter.notifyItemRangeInserted(it.size - viewModel.newElements, viewModel.newElements)

                    //hide progress more
                    if(progressBarLoadMore.isVisible) progressBarLoadMore.visibility = View.GONE
                    //hide progress review
                    else if (!viewModel.isUpdated){
                        progressBarReview.visibility = View.GONE
                        //animation
                        ObjectAnimator.ofFloat(recycleViewReview, View.ALPHA, 0f, 1f).apply {
                            duration = 1000
                            start()
                        }
                        viewModel.isUpdated = true //hide progress
                    }
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner){
            if (it == true){
                Toast.makeText(context, getText(R.string.internet_error), Toast.LENGTH_SHORT).show()

                if (progressBarReview.isVisible)
                    progressBarReview.visibility = View.GONE
                else if (progressBarLoadMore.isVisible)
                    progressBarLoadMore.visibility = View.GONE
                else
                    refreshReview.isRefreshing = false
                viewModel.error.value = false
            }
        }
    }
}