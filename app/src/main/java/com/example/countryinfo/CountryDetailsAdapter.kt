package com.example.countryinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.country_details_card.view.*
import java.text.DecimalFormat

class CountryDetailsAdapter(
    private var countryDetails: CountryDetails,
    private val listener: ViewHolder.OnItemClickListener
) : RecyclerView.Adapter<CountryDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_details_card, parent, false)
        return ViewHolder(view, countryDetails, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return DetailsStates.values().size
    }

    class ViewHolder(
        view: View,
        private val countryDetails: CountryDetails,
        private val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var viewState: DetailsStates? = null
        var dialogMessage = emptyList<String>()

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            viewState?.let { listener.onItemClick(it, dialogMessage) }
        }

        interface OnItemClickListener {
            fun onItemClick(state: DetailsStates, dialogMessage: List<String>)
        }

        fun bind(pos: Int) {
            if (pos % 2 == 0) {
                itemView.circleImage.setImageResource(R.drawable.circle1)
            } else {
                itemView.circleImage.setImageResource(R.drawable.circle2)
            }
            when (pos) {
                0 -> {
                    itemView.labelText.text = DetailsStates.COUNTRY_NAME.title
                    itemView.titleText.text = countryDetails.name
                }
                1 -> {
                    itemView.labelText.text = DetailsStates.CAPITAL.title
                    itemView.titleText.text = countryDetails.capital
                }
                2 -> {
                    itemView.labelText.text = DetailsStates.REGION.title
                    itemView.titleText.text = countryDetails.region
                }
                3 -> {
                    itemView.labelText.text = DetailsStates.POPULATION.title
                    itemView.titleText.text = formatPopulation(countryDetails.population)
                }
                4 -> {
                    itemView.labelText.text = DetailsStates.CURRENCIES.title
                    bindMultiple(DetailsStates.CURRENCIES, countryDetails)
                }
                5 -> {
                    itemView.labelText.text = DetailsStates.LANGUAGES.title
                    bindMultiple(DetailsStates.LANGUAGES, countryDetails)
                }
                6 -> {
                    itemView.labelText.text = DetailsStates.TIMEZONES.title
                    bindMultiple(DetailsStates.TIMEZONES, countryDetails)
                }
                7 -> {
                    itemView.labelText.text = DetailsStates.CALLING_CODES.title
                    bindMultiple(DetailsStates.CALLING_CODES, countryDetails)
                }
            }
        }

        private fun bindMultiple(state: DetailsStates, countryDetails: CountryDetails) {
            if (state == DetailsStates.values().last()) {
                itemView.lineImage.visibility = View.GONE
            }
            when (state) {
                DetailsStates.CURRENCIES -> {
                    setTextViews(countryDetails.currencyNames, state)
                    dialogMessage = countryDetails.currencyNames
                }
                DetailsStates.LANGUAGES -> {
                    setTextViews(countryDetails.languages, state)
                    dialogMessage = countryDetails.languages
                }
                DetailsStates.TIMEZONES -> {
                    setTextViews(countryDetails.timezones, state)
                    dialogMessage = countryDetails.timezones
                }
                DetailsStates.CALLING_CODES -> {
                    setTextViews(countryDetails.callingCodes, state)
                    dialogMessage = countryDetails.callingCodes
                }
            }
            viewState = state
        }

        private fun setTextViews(values: List<String>, state: DetailsStates) {
            for (i in values.indices) {
                if (i > 2)
                    break
                when (i) {
                    0 -> {
                        itemView.titleText.text = values[i]
                        itemView.titleText.setBackgroundResource(state.bgShapeId)
                    }
                    1 -> {
                        itemView.titleText2.text = values[i]
                        itemView.titleText2.setBackgroundResource(state.bgShapeId)
                        itemView.titleText2.visibility = View.VISIBLE
                    }
                    2 -> {
                        itemView.titleText3.text = values[i]
                        itemView.titleText3.setBackgroundResource(state.bgShapeId)
                        itemView.titleText3.visibility = View.VISIBLE
                    }
                }
            }
        }

        private fun formatPopulation(value: Double): String {
            val format = DecimalFormat("0.##")
            val res = if (value >= 1_000_000) {
                format.format(value / 1_000_000) + " m"
            } else {
                format.format(value)
            }
            return res.replace(".", ",")
        }

    }
}

enum class DetailsStates(val title: String, val bgShapeId: Int = 0) {
    COUNTRY_NAME("Country"),
    CAPITAL("Capital"),
    REGION("Region"),
    POPULATION("Population"),
    CURRENCIES("Currencies", R.drawable.rounded_corner_yellow),
    LANGUAGES("Official Languages", R.drawable.rounded_corner_red),
    TIMEZONES("Time Zone", R.drawable.rounded_corner_green),
    CALLING_CODES("Calling Codes", R.drawable.rounded_corner_blue),
}