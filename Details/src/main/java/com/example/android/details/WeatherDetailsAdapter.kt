package com.example.android.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeatherDetailsAdapter(arrayList: ArrayList<WeatherDetailsData>): RecyclerView.Adapter<WeatherDetailsAdapter.ViewHolder>() {
    var data = arrayList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.details_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.cloud.text = item.cloud.toString()
        holder.humidity.text = item.humidity.toString()
        holder.ora.text = item.ora
        holder.temperature.text = item.temperature.toString()
        holder.windSpeed.text = item.windSpeed.toString()
    }

    override fun getItemCount() = data.size

    fun updateDataList(arrayList: ArrayList<WeatherDetailsData>) {
        this.data = arrayList
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ora: TextView = itemView.findViewById(R.id.ora_id_list_item_details)
        val temperature: TextView = itemView.findViewById(R.id.temperatura_id_list_item_details)
        val cloud: TextView = itemView.findViewById(R.id.cloud_area_id_list_item_details)
        val humidity: TextView = itemView.findViewById(R.id.humidity_id_list_item_details)
        val windSpeed: TextView = itemView.findViewById(R.id.wind_speed_id_list_item_details)
    }
}