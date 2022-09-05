package com.example.android.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.service.dao.WeatherData
import kotlin.collections.ArrayList

class WeatherLocationAdapter(dataList: ArrayList<WeatherData>, private val onItemClicked: (WeatherData) -> Unit): RecyclerView.Adapter<WeatherLocationAdapter.ViewHolder>() {
    var data = dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ViewHolder(view) {
            onItemClicked(data[it])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val res = holder.itemView.context.resources

        holder.location.text = item.city + ", " + item.country
        holder.temperature.text = item.temperatura.toString() + "°C"

        if (item.temperatura!! < 5.0) {
            holder.icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_ac_unit_24, 0, 0, 0)
        }
        else {
            if (item.temperatura!! < 20.0) {
                holder.icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_cloud_24, 0, 0, 0)
            }
        }
    }

    override fun getItemCount() = data.size

    fun updateDataList(arrayList: ArrayList<WeatherData>) {
        this.data = arrayList
    }

    class ViewHolder(itemView: View, onItemClicked: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }

        val location: TextView = itemView.findViewById(R.id.locationText)
        val temperature: TextView = itemView.findViewById(R.id.weatherText)
        val icon: TextView = itemView.findViewById(R.id.weather_icon)
        val layout: LinearLayout = itemView.findViewById(R.id.linearLayout_map_id)
    }
}