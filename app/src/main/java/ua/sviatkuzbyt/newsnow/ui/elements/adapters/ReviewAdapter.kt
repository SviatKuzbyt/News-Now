//package ua.sviatkuzbyt.newsnow.ui.elements.adapters
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import androidx.core.content.ContextCompat.*
//import androidx.recyclerview.widget.RecyclerView
//import ua.sviatkuzbyt.newsnow.R
//import ua.sviatkuzbyt.newsnow.data.other.NewsList
//import ua.sviatkuzbyt.newsnow.ui.elements.openNews
//import ua.sviatkuzbyt.newsnow.ui.elements.shareNews
//import ua.sviatkuzbyt.newsnow.ui.review.ReviewViewModel
//import ua.sviatkuzbyt.newsnow.changeSavedNews
//
//
//class ReviewAdapter(
//    private val dataSet: List<NewsList>,
//    private val context: Context,
//    private val viewModel: ReviewViewModel
//) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
//
//    //initializing views and layout
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val imageNewsImageView: ImageView
//        val sourceNewsTextView: TextView
//        val labelNewsTextView: TextView
//        val timeNewsTextView: TextView
//
//        val saveNewsButton: Button
//        val shareNewsButton: Button
//
//        init {
//            imageNewsImageView = view.findViewById(R.id.imageNewsRecycle)
//            sourceNewsTextView = view.findViewById(R.id.sourceNewsRecycle)
//            labelNewsTextView = view.findViewById(R.id.labelNewsRecycle)
//            timeNewsTextView = view.findViewById(R.id.timeNewsRecycle)
//
//            saveNewsButton = view.findViewById(R.id.saveNewsRecycle)
//            shareNewsButton = view.findViewById(R.id.shareNewsRecycle)
//        }
//    }
//
//    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
//        //set big image for first element
//        val view = if (viewType == 1){
//            LayoutInflater.from(viewGroup.context)
//                .inflate(R.layout.recycle_news_main, viewGroup, false)
//
//        } //for others, set default size
//        else {
//            LayoutInflater.from(viewGroup.context)
//                .inflate(R.layout.recycle_news, viewGroup, false)
//        }
//        return ViewHolder(view)
//    }
//
//
//    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//        //set data
//        viewHolder.imageNewsImageView.setImageBitmap(dataSet[position].image)
//        viewHolder.sourceNewsTextView.text = dataSet[position].source
//        viewHolder.labelNewsTextView.text = dataSet[position].label
//        viewHolder.timeNewsTextView.text = dataSet[position].time
//
//        if (dataSet[position].isSaved)
//            viewHolder.saveNewsButton.background = getDrawable(context, R.drawable.ic_saved_blue)
//        else
//            viewHolder.saveNewsButton.background = getDrawable(context, R.drawable.ic_saved_gray)
//
//        //open news
//        viewHolder.itemView.setOnClickListener {
//            openNews(context, dataSet[position].link)
//        }
//        //share news
//        viewHolder.shareNewsButton.setOnClickListener {
//            shareNews(context, dataSet[position].label, dataSet[position].link)
//        }
//
//        //save news (maybe make later)
//        viewHolder.saveNewsButton.setOnClickListener {
//            changeSavedNews = true //update target
//
//            if (!dataSet[position].isSaved){
//                viewModel.addSavedNews(dataSet[position], position)
//                viewHolder.saveNewsButton.background = getDrawable(context, R.drawable.ic_saved_blue)
////                dataSet[position].isSaved = true
//            }
//            else{
//                viewHolder.saveNewsButton.background = getDrawable(context, R.drawable.ic_saved_gray)
//                viewModel.removeSavedNews(dataSet[position].link, position)
////                dataSet[position].isSaved = false
//            }
//        }
//    }
//
//    override fun getItemCount() = dataSet.size
//    //return target position
//    override fun getItemViewType(position: Int): Int {
//        return if (position == 0) 1 else 2
//    }
//}