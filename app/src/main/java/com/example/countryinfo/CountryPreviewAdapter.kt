package com.example.countryinfo

import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder

class CountryPreviewAdapter(
    private var values: List<CountryPreview>,
    private var requestBuilder: RequestBuilder<PictureDrawable>
) : RecyclerView.Adapter<CountryPreviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_preview_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.countryName.text = item.countryName
        requestBuilder.load(item.flagUrl.toUri()).into(holder.countryFlag)
        holder.capitalName.text = item.capital
        holder.regionText.text = item.region
    }

    override fun getItemCount(): Int = values.size

    fun updateAdapterValues(updatedValues: List<CountryPreview>) {
        values = updatedValues
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val countryFlag: ImageView = view.findViewById(R.id.countryFlag)
        val countryName: TextView = view.findViewById(R.id.country_name)
        val capitalName: TextView = view.findViewById(R.id.country_capital)
        val regionText: TextView = view.findViewById(R.id.country_region)
    }
}