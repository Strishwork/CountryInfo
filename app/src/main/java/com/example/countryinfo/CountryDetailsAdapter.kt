package com.example.countryinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            holder.itemView.lineImage.visibility = View.GONE
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

            for (i in state.info.indices) {
                if (i > 2)
                    break
                when (i) {
                    0 -> {
                        itemView.titleText.text = state.info[i]
                        if (state.isInfoHighlighted) {
                            itemView.titleText.setBackgroundResource(state.detailsSections.bgShapeId)
                        }
                    }
                    1 -> {
                        itemView.titleText2.text = state.info[i]
                        itemView.titleText2.setBackgroundResource(state.detailsSections.bgShapeId)
                        itemView.titleText2.visibility = View.VISIBLE
                        dialogMessage = state.info
                        viewSection = state.detailsSections
                        itemView.postDelayed({
                            checkViewsWidth()
                        }, 50)
                    }
                    2 -> {
                        itemView.titleText3.text = state.info[i]
                        itemView.titleText3.setBackgroundResource(state.detailsSections.bgShapeId)
                        itemView.titleText3.visibility = View.VISIBLE
                        itemView.postDelayed({
                            checkViewsWidth()
                        }, 50)
                    }
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
                itemView.titleText2.visibility = View.GONE
                itemView.titleText3.visibility = View.GONE
                itemView.truncateDots.visibility = View.VISIBLE
                return
            } else if (view1 + view2 + view3 > boundsWidth
            ) {
                itemView.titleText3.visibility = View.GONE
                itemView.truncateDots.visibility = View.VISIBLE
            }
        }
    }
}

enum class DetailsSections(val title: String, val bgShapeId: Int = 0) {
    COUNTRY_NAME("Country"),
    CAPITAL("Capital"),
    REGION("Region"),
    POPULATION("Population"),
    CURRENCIES("Currencies", R.drawable.rounded_corner_yellow),
    LANGUAGES("Official Languages", R.drawable.rounded_corner_red),
    TIMEZONES("Time Zone", R.drawable.rounded_corner_green),
    CALLING_CODES("Calling Codes", R.drawable.rounded_corner_blue),
}