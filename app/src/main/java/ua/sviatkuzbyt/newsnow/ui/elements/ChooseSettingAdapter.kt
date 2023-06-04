package ua.sviatkuzbyt.newsnow.ui.elements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.data.other.SettingValues
import ua.sviatkuzbyt.newsnow.ui.setting.ChooseSettingViewModel

class ChooseSettingAdapter(
    private val dataSet: List<SettingValues>,
    private val viewModel: ChooseSettingViewModel,
    private val selectedCode: String,
) : RecyclerView.Adapter<ChooseSettingAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textSetting: TextView = view.findViewById(R.id.textSetting)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.choose_setting_element, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textSetting.text = dataSet[position].value

        if (dataSet[position].code == selectedCode)
            viewHolder.itemView.setBackgroundResource(R.drawable.search_background)
        else viewHolder.textSetting.background = null

        viewHolder.itemView.setOnClickListener {
            viewModel.setRegionAndFinish(dataSet[position].code)
        }
    }

    override fun getItemCount() = dataSet.size
}