package ua.sviatkuzbyt.newsnow.ui.elements.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.*
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.data.NewsContainer
import ua.sviatkuzbyt.newsnow.data.database.updateDataBaseFromReview
import ua.sviatkuzbyt.newsnow.ui.review.ReviewViewModel


class ReviewAdapter(
    private val dataSet: List<NewsContainer>,
    private val context: Context,
    private val viewModel: ReviewViewModel
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageNewsImageView: ImageView
        val sourceNewsTextView: TextView
        val labelNewsTextView: TextView
        val timeNewsTextView: TextView
        val cardViewRecycle: CardView

        val saveNewsButton: Button
        val shareNewsButton: Button

        init {
            // Define click listener for the ViewHolder's View.
            imageNewsImageView = view.findViewById(R.id.imageNewsRecycle)
            sourceNewsTextView = view.findViewById(R.id.sourceNewsRecycle)
            labelNewsTextView = view.findViewById(R.id.labelNewsRecycle)
            timeNewsTextView = view.findViewById(R.id.timeNewsRecycle)
            cardViewRecycle = view.findViewById(R.id.cardViewRecycle)

            saveNewsButton = view.findViewById(R.id.saveNewsRecycle)
            shareNewsButton = view.findViewById(R.id.shareNewsRecycle)
        }
    }

    // Create new views (invoked by the layout manager)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = if (viewType == 1){
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recycle_news_main, viewGroup, false)

        } else {
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recycle_news, viewGroup, false)
        }

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.imageNewsImageView.setImageBitmap(dataSet[position].image)

        viewHolder.sourceNewsTextView.text = dataSet[position].source
        viewHolder.labelNewsTextView.text = dataSet[position].label
        viewHolder.timeNewsTextView.text = dataSet[position].time

        viewHolder.itemView.setOnClickListener {
            try {
                val urlIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(dataSet[position].link)
                )
                startActivity(context, urlIntent, null)
            } catch (e: Exception){
                Toast.makeText(context, "App don't founded for this action", Toast.LENGTH_LONG).show()
            }

        }

        viewHolder.shareNewsButton.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.putExtra(Intent.EXTRA_TEXT, "${dataSet[position].label} : ${dataSet[position].link}")
                shareIntent.type = "text/plain"
                startActivity(context, shareIntent, null)
            } catch (e: Exception){
                Toast.makeText(context, "App don't founded for this action", Toast.LENGTH_LONG).show()
            }
        }

        if (dataSet[position].isSaved) viewHolder.saveNewsButton.background = getDrawable(context, R.drawable.ic_saved_blue)
        viewHolder.saveNewsButton.setOnClickListener {
            updateDataBaseFromReview = true
            if (!dataSet[position].isSaved){
                viewModel.addSavedNews(dataSet[position], position)
                viewHolder.saveNewsButton.background = getDrawable(context, R.drawable.ic_saved_blue)
                dataSet[position].isSaved = true
            }
            else{
                viewHolder.saveNewsButton.background = getDrawable(context, R.drawable.ic_saved_gray)
                viewModel.removeSavedNews(dataSet[position].link, position)
                dataSet[position].isSaved = false
            }

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 1 else 2
    }

}