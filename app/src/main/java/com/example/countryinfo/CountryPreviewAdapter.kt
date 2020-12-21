package com.example.countryinfo

import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideApp
import kotlinx.android.synthetic.main.country_preview_card.view.*

class CountryPreviewAdapter(
    private var values: List<CountryPreview>,
    private val listener: (String) -> Unit
) : RecyclerView.Adapter<CountryPreviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_preview_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values[position])
        holder.itemView.setOnClickListener { listener(values[position].id) }
    }

    override fun getItemCount(): Int = values.size

    fun updateAdapterValues(updatedValues: List<CountryPreview>) {
        values = updatedValues
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: CountryPreview) {
            itemView.country_name.text = item.countryName
            itemView.country_flag.loadSvgImage(item.flagUrl.toUri())
            itemView.country_capital.text = item.capital
            itemView.country_region.text = item.region
        }
    }
}