package ua.sviatkuzbyt.newsnow.ui.elements

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.*
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.data.database.SavedNewsEntity
import ua.sviatkuzbyt.newsnow.ui.saved.SavedViewModel


class SavedAdapter(
    private val dataSet: List<SavedNewsEntity>,
    private val context: Context,
    private val viewModel: SavedViewModel
) : RecyclerView.Adapter<SavedAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sourceNewsTextView: TextView
        val labelNewsTextView: TextView
        val timeNewsTextView: TextView

        val saveNewsButton: Button
        val shareNewsButton: Button

        init {
            // Define click listener for the ViewHolder's View.
            sourceNewsTextView = view.findViewById(R.id.sourceNewsRecycle)
            labelNewsTextView = view.findViewById(R.id.labelNewsRecycle)
            timeNewsTextView = view.findViewById(R.id.timeNewsRecycle)

            saveNewsButton = view.findViewById(R.id.saveNewsRecycle)
            shareNewsButton = view.findViewById(R.id.shareNewsRecycle)
        }
    }

    // Create new views (invoked by the layout manager)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recycle_saved_news, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

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

        viewHolder.saveNewsButton.setOnClickListener {
            viewModel.deleteSavedNews(dataSet[position].link, position)
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}