package ua.sviatkuzbyt.newsnow.ui.elements.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.ui.search.SearchViewModel

class HistoryAdapter(
    private val dataSet: List<String>,
    private val viewModel: SearchViewModel,
    private val listener: HistoryInterface
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>()  {

    //send text for search
    interface HistoryInterface{ fun searchNewsFromHistory(news: String) }

    //initializing views and layout
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textHistory: TextView
        val closetButton: Button

        init {
            textHistory = view.findViewById(R.id.textHistory)
            closetButton = view.findViewById(R.id.ClosetButton)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.history_recycle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //set data
        val historyString = dataSet[position]
        holder.textHistory.text = historyString

        //initializing delete history
        holder.closetButton.setOnClickListener {
            viewModel.deleteHistory(historyString, position)
        }
        //initializing search history
        holder.itemView.setOnClickListener {
            this.listener.searchNewsFromHistory(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size
}