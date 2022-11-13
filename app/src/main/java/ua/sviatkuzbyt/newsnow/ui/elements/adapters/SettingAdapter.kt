package ua.sviatkuzbyt.newsnow.ui.elements.adapters

import android.annotation.SuppressLint
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

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textSettingLabel: TextView
        val textSettingValue: TextView


        init {
            textSettingLabel = view.findViewById(R.id.textSettingLabel)
            textSettingValue = view.findViewById(R.id.textSettingValue)
        }
    }

    // Create new views (invoked by the layout manager)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.setting_element, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textSettingLabel.text = dataSet[position].key
        viewHolder.textSettingValue.text = dataSet[position].value

        val intent = Intent(context, SettingValuesActivity::class.java)
        intent.putExtra("key", position)
        intent.putExtra("selected", dataSet[position].value)

        viewHolder.itemView.setOnClickListener {
            startActivity(context, intent, null)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}