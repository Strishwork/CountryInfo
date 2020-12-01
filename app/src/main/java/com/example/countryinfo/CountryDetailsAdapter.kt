package com.example.countryinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.country_details_card.view.*

class CountryDetailsAdapter(
    private var states: List<DetailsViewHolderState>,
    private val listener: ViewHolder.OnItemClickListener
) : RecyclerView.Adapter<CountryDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_details_card, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position % 2 == 0) {
            holder.itemView.circleImage.setImageResource(R.drawable.circle1)
        } else {
            holder.itemView.circleImage.setImageResource(R.drawable.circle2)
        }
        if (position == states.size - 1) {
            holder.itemView.lineImage.isVisible = false
        }
        holder.bind(states[position])
    }

    override fun getItemCount(): Int {
        return states.size
    }

    fun setCountryStates(states: List<DetailsViewHolderState>) {
        this.states = states
        notifyDataSetChanged()
    }

    class ViewHolder(view: View, private val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(view) {

        interface OnItemClickListener {
            fun onItemClick(section: DetailsSections, dialogMessage: List<String>)
        }

        fun bind(state: DetailsViewHolderState) {
            itemView.labelText.text = state.title

            with(itemView) {
                listOf(titleText, titleText2, titleText3).forEachIndexed { index, textView ->
                    state.info.getOrNull(index)?.let {
                        if (index == 1) {
                            setMyOnClickListener(state.detailsSections, state.info)
                        }
                        textView.apply {
                            text = it
                            setBackgroundResource(state.detailsSections.bgShapeId)
                        }
                    }
                }
                itemView.post { checkViewsWidth() }
            }
        }

        private fun checkViewsWidth() {
            val boundsWidth =
                itemView.titlesBounds.measuredWidth -
                        (itemView.titlesBounds.layoutParams as ViewGroup.MarginLayoutParams).marginStart
            var currentWidth = itemView.titleText.measuredWidth
            val titleTextViews = listOf(itemView.titleText2, itemView.titleText3)
            for (i in titleTextViews.indices) {
                if (titleTextViews[i].measuredWidth + currentWidth < boundsWidth) {
                    titleTextViews[i].isVisible = true
                    currentWidth += titleTextViews[i].measuredWidth
                } else {
                    titleTextViews[i].isVisible = false
                    itemView.truncateDots.isVisible = true
                    return
                }
            }
        }

        private fun setMyOnClickListener(detailsSection: DetailsSections, message: List<String>) {
            itemView.setOnClickListener {
                listener.onItemClick(
                    detailsSection,
                    message
                )
            }
        }
    }
}

enum class DetailsSections(val title: String, val bgShapeId: Int = 0) {
    DEFAULT(""),
    COUNTRY_NAME("Country"),
    CAPITAL("Capital"),
    REGION("Region"),
    POPULATION("Population"),
    CURRENCIES("Currencies", R.drawable.rounded_corner_yellow),
    LANGUAGES("Official Languages", R.drawable.rounded_corner_red),
    TIMEZONES("Time Zone", R.drawable.rounded_corner_green),
    CALLING_CODES("Calling Codes", R.drawable.rounded_corner_blue),
}