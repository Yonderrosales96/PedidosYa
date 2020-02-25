package com.example.pedidosya.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pedidosya.R
import com.example.pedidosya.repository.Responses.RestaurantsResponse


class RestaurantsRecyclerAdapter(val context : Context, val restaurants : RestaurantsResponse) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_ITEM) {
            var view =
                LayoutInflater.from(context).inflate(R.layout.list_restaurants_item, parent, false)
            return ViewHolder(view)
        }
        else {
            var view =
                LayoutInflater.from(context).inflate(R.layout.progress_bar, parent, false)
            return ProgressHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return restaurants.data?.size!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder)
            holder.actView(restaurants.data?.get(position)!!)
        if (holder is ProgressHolder)
            holder.progressBar.isIndeterminate = true
    }

    override fun getItemViewType(position: Int): Int {
        return if (restaurants.data?.get(position) != null) VIEW_ITEM else VIEW_PROG
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.name)
        val cityname = itemView.findViewById<TextView>(R.id.cityname)
        fun actView(item : RestaurantsResponse.restaurantData){
            name.text = item.name
            cityname.text = item.cityName
        }
    }
    class ProgressHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progressBar : ProgressBar = itemView.findViewById(R.id.progress_bar)


    }





}