package ua.sviatkuzbyt.newsnow.ui.elements

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.data.other.NewsList

class NewsListAdapter(private var dataSet: MutableList<NewsList>,
                      private val context: Context,
) : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageNewsImageView: ImageView
        val sourceNewsTextView: TextView
        val labelNewsTextView: TextView
        val timeNewsTextView: TextView
        val saveNewsButton: Button
        val shareNewsButton: Button

        init {
            imageNewsImageView = view.findViewById(R.id.imageNewsRecycle)
            sourceNewsTextView = view.findViewById(R.id.sourceNewsRecycle)
            labelNewsTextView = view.findViewById(R.id.labelNewsRecycle)
            timeNewsTextView = view.findViewById(R.id.timeNewsRecycle)
            saveNewsButton = view.findViewById(R.id.saveNewsRecycle)
            shareNewsButton = view.findViewById(R.id.shareNewsRecycle)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val layout =
            if (viewType == 1) R.layout.recycle_news_main
            else R.layout.recycle_news

        val view = LayoutInflater.from(viewGroup.context).inflate(layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.imageNewsImageView.setImageBitmap(dataSet[position].image)
        viewHolder.sourceNewsTextView.text = dataSet[position].source
        viewHolder.labelNewsTextView.text = dataSet[position].label
        viewHolder.timeNewsTextView.text = dataSet[position].time

        val saveBackground =
            if (dataSet[position].isSaved) R.drawable.ic_saved_blue
            else R.drawable.ic_saved_gray
        viewHolder.saveNewsButton.setBackgroundResource(saveBackground)

        viewHolder.itemView.setOnClickListener {
            try {
                val urlIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(dataSet[position].link)
                )
                ContextCompat.startActivity(context, urlIntent, null)
            } catch (e: Exception){
                Toast.makeText(context, "App don't founded for this action", Toast.LENGTH_LONG).show()
            }
        }

        viewHolder.shareNewsButton.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, "${dataSet[position].label}: ${dataSet[position].link}")
                    type = "text/plain"
                }
                ContextCompat.startActivity(context, shareIntent, null)
            } catch (e: Exception){
                Toast.makeText(context, "App don't founded for this action", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newData: MutableList<NewsList>) {
        notifyItemRangeRemoved(0, itemCount)
        dataSet = newData
        notifyItemRangeInserted(0, itemCount)
    }

    fun addData(data: MutableList<NewsList>) {
        val startPosition = dataSet.size
        val itemCount = data.size
        dataSet.addAll(data.slice(startPosition until itemCount))
        notifyItemRangeInserted(startPosition, itemCount)
    }
}