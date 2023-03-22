//package ua.sviatkuzbyt.newsnow.ui.elements.adapters
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import ua.sviatkuzbyt.newsnow.R
//import ua.sviatkuzbyt.newsnow.data.database.room.SavedNewsEntity
//import ua.sviatkuzbyt.newsnow.ui.elements.openNews
//import ua.sviatkuzbyt.newsnow.ui.elements.shareNews
//import ua.sviatkuzbyt.newsnow.ui.saved.SavedViewModel
//
//
//class SavedAdapter(
//    private val dataSet: List<SavedNewsEntity>,
//    private val context: Context,
//    private val viewModel: SavedViewModel
//) : RecyclerView.Adapter<SavedAdapter.ViewHolder>() {
//
//    //initializing views
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val sourceNewsTextView: TextView
//        val labelNewsTextView: TextView
//        val timeNewsTextView: TextView
//        val saveNewsButton: Button
//        val shareNewsButton: Button
//
//        init {
//            sourceNewsTextView = view.findViewById(R.id.sourceNewsRecycle)
//            labelNewsTextView = view.findViewById(R.id.labelNewsRecycle)
//            timeNewsTextView = view.findViewById(R.id.timeNewsRecycle)
//            saveNewsButton = view.findViewById(R.id.saveNewsRecycle)
//            shareNewsButton = view.findViewById(R.id.shareNewsRecycle)
//        }
//    }
//
//    //initializing layout
//    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(viewGroup.context)
//                .inflate(R.layout.recycle_saved_news, viewGroup, false)
//
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//
//        //set data
//        viewHolder.sourceNewsTextView.text = dataSet[position].source
//        viewHolder.labelNewsTextView.text = dataSet[position].label
//        viewHolder.timeNewsTextView.text = dataSet[position].time
//
//        //open news
//        viewHolder.itemView.setOnClickListener {
//            openNews(context, dataSet[position].link)
//        }
//        //share news
//        viewHolder.shareNewsButton.setOnClickListener {
//            shareNews(context, dataSet[position].label, dataSet[position].link)
//        }
//        //delete saved news
//        viewHolder.saveNewsButton.setOnClickListener {
//            viewModel.deleteSavedNews(dataSet[position].link, position)
//        }
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    override fun getItemCount() = dataSet.size
//}