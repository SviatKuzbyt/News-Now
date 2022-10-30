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
import ua.sviatkuzbyt.newsnow.ui.elements.adapters.ReviewAdapter

class ReviewFragment : Fragment() {

    lateinit var viewModel: ReviewViewModel
    lateinit var progressBarLoadMore: ProgressBar
    lateinit var progressBarReview: ProgressBar
    lateinit var refreshReview: SwipeRefreshLayout
    lateinit var recycleViewReview: RecyclerView

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

        //Встановлення progressBarLoadMore
        progressBarLoadMore = view.findViewById(R.id.progressBarLoadMore)
        progressBarLoadMore.isIndeterminate = true
        //показати, якщо йде оновлення
        if (viewModel.loadMode == 2) progressBarLoadMore.visibility = View.VISIBLE

        //Встановлення progressBarReview
        progressBarReview = view.findViewById(R.id.progressBarReview)
        if (viewModel.loadMode == 1) progressBarReview.visibility = View.VISIBLE

        //Set SwipeRefresh, options and work
        refreshReview = view.findViewById(R.id.refreshReview)
        refreshReview.setColorSchemeResources(R.color.blue)

        refreshReview.setOnRefreshListener {
            if (viewModel.loadMode == 0){
                viewModel.firstUpdate()
                viewModel.loadMode = 3
            }
            else refreshReview.isRefreshing = false
        }

        //Set RecycleView
        recycleViewReview = view.findViewById(R.id.recycleViewReview)
        recycleViewReview.layoutManager = LinearLayoutManager(activity)

        val adapter = ReviewAdapter( //Set adapter
            viewModel.list.value!!, requireActivity(), viewModel
        )
        recycleViewReview.adapter = adapter

        //set loading new news, when list is scrolled down
        recycleViewReview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (
                    linearLayoutManager != null
                    && linearLayoutManager.findLastCompletelyVisibleItemPosition() == viewModel.list.value!!.size - 1
                    && viewModel.loadMode == 0
                ) {
                    progressBarLoadMore.visibility = View.VISIBLE //on progressbar
                    viewModel.update() //call function in VM
                    viewModel.loadMode = 2 //set loadMode
                }
            }
        })

        //update information, list when we load information
        viewModel.list.observe(viewLifecycleOwner){
            if (viewModel.changed){

                if(viewModel.loadMode == 3){ //when we call update info
                    adapter.notifyItemRangeRemoved(0, viewModel.oldSize) //delete all
                    adapter.notifyItemRangeInserted(0, viewModel.newElements) //and add all
                    refreshReview.isRefreshing = false
                }

                else{ //update view list
                    adapter.notifyItemRangeInserted(
                        it.size - viewModel.newElements,
                        viewModel.newElements
                    )

                    if(viewModel.loadMode == 2) progressBarLoadMore.visibility = View.GONE //hide progress more

                    else if(viewModel.loadMode == 1){ //or hide progress review
                        progressBarReview.visibility = View.GONE

                        ObjectAnimator.ofFloat( //animation
                            recycleViewReview, View.ALPHA, 0f, 1f
                        ).apply {
                            duration = 1000
                            start()
                        }
                    }
                }

                viewModel.loadMode = 0 //set load options
                viewModel.changed = false
            }
        }

        viewModel.error.observe(viewLifecycleOwner){
            if (it == true){
                Toast.makeText(context, getText(R.string.internet_error), Toast.LENGTH_SHORT).show()

                if (progressBarReview.isVisible) progressBarReview.visibility = View.GONE
                else if (progressBarLoadMore.isVisible) progressBarLoadMore.visibility = View.GONE
                else refreshReview.isRefreshing = false

                viewModel.error.value = false
                viewModel.loadMode = 0
                viewModel.changed = false
            }
        }
    }
}