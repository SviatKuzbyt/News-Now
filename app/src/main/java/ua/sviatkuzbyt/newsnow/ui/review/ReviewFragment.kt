package ua.sviatkuzbyt.newsnow.ui.review

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.ui.elements.NewsRecycleViewAdapter


class ReviewFragment : Fragment() {

    lateinit var viewModel: ReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this)[ReviewViewModel::class.java]
        Log.v("update", "оновилось, сука")
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBarReview = view.findViewById<ProgressBar>(R.id.progressBarReview)
        val recycleViewReview = view.findViewById<RecyclerView>(R.id.recycleViewReview)
        recycleViewReview.layoutManager = LinearLayoutManager(activity)

        activity?.let { owner ->
            viewModel.list.observe(owner){
                recycleViewReview.adapter = NewsRecycleViewAdapter(it, owner)

                ObjectAnimator.ofFloat(progressBarReview, View.ALPHA, 1f, 0f).apply {
                    duration = 1000
                    start()
                }

                ObjectAnimator.ofFloat(recycleViewReview, View.ALPHA, 0f, 1f).apply {
                    duration = 1000
                    start()
                }
            }
        }
    }
}