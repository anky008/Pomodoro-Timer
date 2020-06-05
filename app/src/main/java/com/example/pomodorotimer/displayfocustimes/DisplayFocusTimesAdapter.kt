package com.example.pomodorotimer.displayfocustimes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.pomodorotimer.*
import com.example.pomodorotimer.database.FocusTime
import org.w3c.dom.Text

class DisplayFocusTimesAdapter : Adapter<DisplayFocusTimesAdapter.ViewHolder>() {

    var data= listOf<FocusTime>()

    set(value){
        field = value
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.each_focus_time, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount()=data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val startTimeText:TextView=itemView.findViewById(R.id.start_time_text)
        val qualityImage:ImageView=itemView.findViewById(R.id.quality_image_view)
        val durationText:TextView=itemView.findViewById(R.id.duration_text)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val item=data[position]
        holder.startTimeText.text = convertLongToDateString(item.startTimeMilli)
        holder.qualityImage.setImageResource(getImageResource(item.startTimeMilli,item.endTimeMilli))
        holder.durationText.text=getDuration(item.startTimeMilli,item.endTimeMilli)
    }
}