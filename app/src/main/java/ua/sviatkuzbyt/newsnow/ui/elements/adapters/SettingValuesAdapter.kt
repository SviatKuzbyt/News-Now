//package ua.sviatkuzbyt.newsnow.ui.elements.adapters
//
//import android.graphics.Color
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import ua.sviatkuzbyt.newsnow.R
//import ua.sviatkuzbyt.newsnow.data.other.SettingValues
//import ua.sviatkuzbyt.newsnow.ui.setting.SettingValuesViewModel
//
//class SettingValuesAdapter(
//    private val dataSet: List<SettingValues>,
//    private val viewModel: SettingValuesViewModel,
//    private val selected: String?,
//    private val modeString: String,
//    private val color: String
//) : RecyclerView.Adapter<SettingValuesAdapter.ViewHolder>() {
//
//    //initializing views
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val textSetting: TextView
//        init {
//            textSetting = view.findViewById(R.id.textSetting)
//        }
//    }
//    //initializing layout
//    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(viewGroup.context)
//                .inflate(R.layout.setting_values_elements, viewGroup, false)
//
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//        //set data
//        viewHolder.textSetting.text = dataSet[position].value
//
//        if (dataSet[position].value == selected)
//            viewHolder.textSetting.setTextColor(Color.parseColor("#0099FF"))
//        else viewHolder.textSetting.setTextColor(Color.parseColor(color))
//
//        //set parameter
//        viewHolder.itemView.setOnClickListener {
//            viewModel.updateKey(
//                dataSet[position].value,
//                modeString,
//                dataSet[position].valueShort,
//                "${modeString}_code"
//            )
//        }
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    override fun getItemCount() = dataSet.size
//}