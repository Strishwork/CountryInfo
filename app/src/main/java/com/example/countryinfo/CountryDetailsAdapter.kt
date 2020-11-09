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

    class ViewHolder(
        view: View, private val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var viewSection: DetailsSections? = null
        var dialogMessage = emptyList<String>()

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            viewSection?.let { listener.onItemClick(it, dialogMessage) }
        }

        interface OnItemClickListener {
            fun onItemClick(section: DetailsSections, dialogMessage: List<String>)
        }

        fun bind(state: DetailsViewHolderState) {
            itemView.labelText.text = state.title

            with(itemView) {
                listOf(titleText, titleText2, titleText3).forEachIndexed { index, textView ->
                    state.info.getOrNull(index)?.let {
                        textView.apply {
                            text = it
                            setBackgroundResource(state.detailsSections.bgShapeId)
                            isVisible = true
                        }
                        if (index == 0) {
                            viewSection = null
                        } else {
                            viewSection = state.detailsSections
                            dialogMessage = state.info
                            itemView.post { checkViewsWidth() }
                        }
                    } ?: run { textView.isVisible = false }
                }
            }
        }

        private fun checkViewsWidth() {
            val boundsWidth =
                itemView.titlesBounds.measuredWidth - itemView.context.resources.getDimension(R.dimen.titles_margin)
                    .toInt()
            val view1 = itemView.titleText.measuredWidth
            val view2 = itemView.titleText2.measuredWidth
            val view3 = itemView.titleText3.measuredWidth
            if (view1 + view2 > boundsWidth) {
                itemView.titleText2.isVisible = false
                itemView.titleText3.isVisible = false
                itemView.truncateDots.isVisible = true
                return
            } else if (view1 + view2 + view3 > boundsWidth) {
                itemView.titleText3.isVisible = false
                itemView.truncateDots.isVisible = true
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