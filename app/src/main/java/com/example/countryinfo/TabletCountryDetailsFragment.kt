package com.example.countryinfo

import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.twocoffeesoneteam.glidetovectoryou.GlideApp
import kotlinx.android.synthetic.main.tablet_country_details_fragment.*
import kotlinx.android.synthetic.main.tablet_country_details_fragment.view.*
import javax.inject.Inject

class TabletCountryDetailsFragment : Fragment() {

    companion object {
        private const val COUNTRY_ID = "country_id"
        fun newInstance(countryId: String) = TabletCountryDetailsFragment().apply {
            arguments = bundleOf(COUNTRY_ID to countryId)
        }
    }

    private lateinit var rootView: View

    @Inject
    lateinit var viewModelFactory: CountryDetailsViewModelFactory
    private val viewModel: CountryDetailsViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.tablet_country_details_fragment, container, false)
        (activity?.application as CountriesApplication).countriesComponent.inject(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val id = arguments?.get(COUNTRY_ID) as? String
        viewModel.countryLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            handleState(it)
        })
        if (id != null) {
            viewModel.countryClicked(id)
        }
    }

    private fun handleState(countryDetailsViewState: CountryDetailsViewState) {
        when (countryDetailsViewState) {
            is CountryDetailsViewState.Default -> {
                tabletCountryNameTextView.text = countryDetailsViewState.countryDetails.name
                tabletCountryPopulationTextView.text = countryDetailsViewState.countryDetails.population
                tabletCountryTimezoneTextView.text = countryDetailsViewState.countryDetails.timezones.getOrNull(0)
                tabletCountryLanguageTextView.text = countryDetailsViewState.countryDetails.languages.getOrNull(0)
                tabletCountryCapitalTextView.text = countryDetailsViewState.countryDetails.capital
                tabletCountryCallingCodesTextView.text = countryDetailsViewState.countryDetails.callingCodes.getOrNull(0)
                tabletCountryRegionTextView.text = countryDetailsViewState.countryDetails.region
                tabletCountryCurrenciesTextView.text = countryDetailsViewState.countryDetails.currencyNames.getOrNull(0)
                loadFlag(countryDetailsViewState.countryDetails)
            }
            is CountryDetailsViewState.Error -> {
                Toast.makeText(
                    context,
                    countryDetailsViewState.error.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun loadFlag(country: CountryDetails) {
        val requestBuilder = GlideApp.with(requireContext())
            .`as`(PictureDrawable::class.java)
            .listener(SvgSoftwareLayerSetter())
        requestBuilder.load(country.flag.toUri()).into(rootView.country_flag)
    }
}