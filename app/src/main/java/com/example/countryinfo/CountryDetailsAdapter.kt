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
        holder.itemView.lineImage.isVisible = position != states.size - 1
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

        private val titles = listOf(itemView.titleText, itemView.titleText2, itemView.titleText3)

        interface OnItemClickListener {
            fun onItemClick(state: DetailsViewHolderState)
        }

        fun bind(state: DetailsViewHolderState) {
            itemView.labelText.text = state.title
            itemView.truncateDots.isVisible = false

            titles.forEachIndexed { index, textView ->
                state.info.getOrNull(index)?.let {
                    textView.apply {
                        text = it
                        if (state.detailsSections.bgShapeId == 0) {
                            setBackgroundResource(0)
                            textView.setPadding(0, 0, 0, 0)
                        } else {
                            setBackgroundResource(state.detailsSections.bgShapeId)
                        }
                    }
                }
            }
            if (state.info.size > 1) {
                itemView.setOnClickListener { listener.onItemClick(state) }
                checkViewsWidth(itemView)
            } else {
                itemView.setOnClickListener(null)
                for (i in 1 until titles.size){
                    titles[i].text = ""
                }
            }

        }

        private fun checkViewsWidth(itemView: View) {
            val boundsWidth =
                itemView.titlesBounds.measuredWidth -
                        (itemView.titlesBounds.layoutParams as ViewGroup.MarginLayoutParams).marginStart
            itemView.titleText.measure(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            var currentWidth = itemView.titleText.measuredWidth
            for (i in 1 until titles.size) {
                /*Starting from 1 because first titleText has to be
                 measured earlier in order to return correct value*/
                titles[i].measure(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                currentWidth += titles[i].measuredWidth
                if (currentWidth < boundsWidth) {
                    titles[i].isVisible = titles[i].text != ""
                } else {
                    titles[i].isVisible = false
                    itemView.truncateDots.isVisible = true
                }
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