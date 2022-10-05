package ua.sviatkuzbyt.newsnow.ui.elements

//import android.widget.Button
//import android.widget.ImageView
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.data.NewsContainer


class NewsRecycleViewAdapter(private val dataSet: List<NewsContainer>, private val context: Context) :
    RecyclerView.Adapter<NewsRecycleViewAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(_view: View) : RecyclerView.ViewHolder(_view) {
        val imageNewsImageView: ImageView
        val sourceNewsTextView: TextView
        val labelNewsTextView: TextView
        val timeNewsTextView: TextView
//        val saveNewsButton: Button
//        val shareNewsButton: Button

        init {
            // Define click listener for the ViewHolder's View.
            imageNewsImageView = _view.findViewById(R.id.imageNewsRecycle)
            sourceNewsTextView = _view.findViewById(R.id.sourceNewsRecycle)
            labelNewsTextView = _view.findViewById(R.id.labelNewsRecycle)
            timeNewsTextView = _view.findViewById(R.id.timeNewsRecycle)

//            saveNewsButton = view.findViewById(R.id.saveNewsRecycle)
//            shareNewsButton = view.findViewById(R.id.shareNewsRecycle)
        }
    }

    // Create new views (invoked by the layout manager)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycle_news, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if (dataSet[position].image != null) viewHolder.imageNewsImageView.setImageBitmap(dataSet[position].image)
        viewHolder.sourceNewsTextView.text = dataSet[position].source
        viewHolder.labelNewsTextView.text = dataSet[position].label
        viewHolder.timeNewsTextView.text = dataSet[position].time

        viewHolder.itemView.setOnClickListener {
            val urlIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(dataSet[position].link)
            )
            startActivity(context, urlIntent, null)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}