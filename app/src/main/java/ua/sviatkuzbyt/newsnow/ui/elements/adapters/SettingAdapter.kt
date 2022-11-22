package ua.sviatkuzbyt.newsnow.ui.elements.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.*
import androidx.recyclerview.widget.RecyclerView
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.ui.setting.SettingValuesActivity
import ua.sviatkuzbyt.newsnow.data.SettingKey


class SettingAdapter(
    private val dataSet: Array<SettingKey>,
    private val context: Context,
) : RecyclerView.Adapter<SettingAdapter.ViewHolder>() {

    //initializing views
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textSettingLabel: TextView
        val textSettingValue: TextView

        init {
            textSettingLabel = view.findViewById(R.id.textSettingLabel)
            textSettingValue = view.findViewById(R.id.textSettingValue)
        }
    }

    //initializing layout
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.setting_element, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //set data
        viewHolder.textSettingLabel.text = dataSet[position].key
        viewHolder.textSettingValue.text = dataSet[position].value


        //open SettingValuesActivity
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(context, SettingValuesActivity::class.java)
            intent.putExtra("key", position)
            intent.putExtra("selected", dataSet[position].value)

            startActivity(context, intent, null)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}