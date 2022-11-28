package ua.sviatkuzbyt.newsnow.ui.review

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.changeSavedNewsForReview
import ua.sviatkuzbyt.newsnow.ui.elements.adapters.ReviewAdapter

class ReviewFragment : Fragment() {

    //publish views, vars
    lateinit var viewModel: ReviewViewModel
    lateinit var progressBarLoadMore: ProgressBar
    lateinit var progressBarReview: ProgressBar
    lateinit var refreshReview: SwipeRefreshLayout
    lateinit var recycleViewReview: RecyclerView
    lateinit var adapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Created ViewModel
        viewModel = ViewModelProvider(this)[ReviewViewModel::class.java]
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**    INITIALIZATION VIEWS    */
        //set progress bars and refresh
        progressBarLoadMore = view.findViewById(R.id.progressBarLoadMore)
        progressBarLoadMore.isIndeterminate = true

        progressBarReview = view.findViewById(R.id.progressBarReview)

        refreshReview = view.findViewById(R.id.refreshReview)
        refreshReview.setColorSchemeResources(R.color.blue)

        //set recycleViewReview
        recycleViewReview = view.findViewById(R.id.recycleViewReview)
        recycleViewReview.layoutManager = LinearLayoutManager(activity)

        if (changeSavedNewsForReview){
            viewModel.updateChanges()
            changeSavedNewsForReview = false
        }

        adapter = ReviewAdapter( //Set adapter
            viewModel.list.value!!, requireActivity(), viewModel
        )
        recycleViewReview.adapter = adapter

        /**   CODE ON START FRAGMENT    */
        //set progressbar if necessary
        when(viewModel.loadMode){
            1 -> {
                progressBarReview.visibility = View.VISIBLE
            }
            2 -> progressBarLoadMore.visibility = View.VISIBLE
            3 -> refreshReview.isRefreshing = true
        }

        /**    INITIALIZATION REFRESH REVIEW    */
        refreshReview.setOnRefreshListener {
            if (viewModel.loadMode == 0){
                viewModel.firstUpdate() //call "get news"
                viewModel.loadMode = 3
            }
            else refreshReview.isRefreshing = false
        }

        /**    INITIALIZATION SCROLL IN RECYCLE    */
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

        /**    OBSERVES    */

        //get news
        viewModel.list.observe(viewLifecycleOwner){
            //when we call update info
            if (viewModel.loaded){

                if(viewModel.loadMode == 3){
                    adapter.notifyItemRangeRemoved(0, viewModel.oldSize) //delete all
                    adapter.notifyItemRangeInserted(0, viewModel.newElements) //and add all
                    refreshReview.isRefreshing = false
                }
                //update view list
                else{
                    adapter.notifyItemRangeInserted(
                        it.size - viewModel.newElements,
                        viewModel.newElements
                    )
                    //hide progress more
                    if(viewModel.loadMode == 2) progressBarLoadMore.visibility = View.GONE

                    //or hide progress review
                    else if(viewModel.loadMode == 1){
                        progressBarReview.visibility = View.GONE
                        //animation
                        ObjectAnimator.ofFloat(recycleViewReview, View.ALPHA, 0f, 1f).apply {
                            duration = 1000
                            start()
                        }
                    }
                }
                //set load options
                viewModel.loadMode = 0
                viewModel.loaded = false
            }

        }

        //error
        viewModel.error.observe(viewLifecycleOwner){
            if (it == true){
                Toast.makeText(context, getText(R.string.internet_error), Toast.LENGTH_SHORT).show()

                //hide views
                when(viewModel.loadMode){
                    1 -> progressBarReview.visibility = View.GONE
                    2 -> progressBarLoadMore.visibility = View.GONE
                    3 -> refreshReview.isRefreshing = false
                }

                //update operators values
                viewModel.error.value = false
                viewModel.loadMode = 0
                viewModel.loaded = false
            }
        }
    }
}