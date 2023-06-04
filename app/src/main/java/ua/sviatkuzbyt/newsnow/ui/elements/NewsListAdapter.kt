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
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.data.other.NewsList

class NewsListAdapter(private var dataSet: MutableList<NewsList>,
                      private val context: Context,
                      private val largeFirstItem: Boolean,
                      private val viewModel: NewsViewModel
) : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageNewsImageView: ImageView = view.findViewById(R.id.imageNewsRecycle)
        val sourceNewsTextView: TextView  = view.findViewById(R.id.sourceNewsRecycle)
        val labelNewsTextView: TextView = view.findViewById(R.id.labelNewsRecycle)
        val timeNewsTextView: TextView = view.findViewById(R.id.timeNewsRecycle)
        val saveNewsButton: Button = view.findViewById(R.id.saveNewsRecycle)
        val shareNewsButton: Button = view.findViewById(R.id.shareNewsRecycle)
        val cardViewRecycle: CardView = view.findViewById(R.id.cardViewRecycle)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val layout =
            if (viewType == 1 && largeFirstItem) R.layout.recycle_news_main
            else R.layout.recycle_news

        val view = LayoutInflater.from(viewGroup.context).inflate(layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val image = dataSet[position].image
        if (image == null) viewHolder.cardViewRecycle.visibility = View.GONE
        else viewHolder.imageNewsImageView.setImageBitmap(image)

        viewHolder.sourceNewsTextView.text = dataSet[position].source
        viewHolder.labelNewsTextView.text = dataSet[position].label
        viewHolder.timeNewsTextView.text = dataSet[position].time

        viewHolder.saveNewsButton.setBackgroundResource(
            if (dataSet[position].isSaved) R.drawable.ic_saved_blue
            else R.drawable.ic_saved_gray
        )

        viewHolder.itemView.setOnClickListener {
            try {
                val urlIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(dataSet[position].link)
                )
                ContextCompat.startActivity(context, urlIntent, null)
            } catch (e: Exception){
                makeToast(context, context.getString(R.string.no_app))
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
                makeToast(context, context.getString(R.string.no_app))
            }
        }

        viewHolder.saveNewsButton.setOnClickListener {
            val item = dataSet[position]
            if(item.isSaved){
                viewModel.removeSavedNews(item.link)
                it.setBackgroundResource(R.drawable.ic_saved_gray)
                item.isSaved = false
            } else{
                viewModel.addSavedNews(item)
                it.setBackgroundResource(R.drawable.ic_saved_blue)
                item.isSaved = true
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

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 1 else 0
    }
}