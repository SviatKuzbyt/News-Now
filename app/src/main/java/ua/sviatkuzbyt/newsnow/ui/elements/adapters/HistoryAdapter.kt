package ua.sviatkuzbyt.newsnow.ui.elements.adapters

import android.content.Context
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
    private val context: Context,
    private val viewModel: SearchViewModel
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>()  {

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
        val historyString = dataSet[position]
        holder.textHistory.text = historyString

        holder.closetButton.setOnClickListener {
            viewModel.deleteHistory(historyString)
            viewModel.deleteHistory.value = position
        }
    }

    override fun getItemCount() = dataSet.size
}