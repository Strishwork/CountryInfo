package com.example.countryinfo

import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.twocoffeesoneteam.glidetovectoryou.GlideApp
import kotlinx.android.synthetic.main.tablet_country_details_fragment.*
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
        val id = arguments?.get(COUNTRY_ID) as? String ?: return
        viewModel.countryLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            handleState(it)
        })
        viewModel.countryClicked(id)
    }

    private fun handleState(countryDetailsViewState: CountryDetailsViewState) {
        when (countryDetailsViewState) {
            is CountryDetailsViewState.Default -> {
                with(countryDetailsViewState.countryDetails) {
                    tabletCountryNameTextView.text = name
                    tabletCountryPopulationTextView.text = population
                    tabletCountryTimezoneTextView.text = timezones.getOrNull(0)
                    tabletCountryLanguageTextView.text = languages.getOrNull(0)
                    tabletCountryCapitalTextView.text = capital
                    tabletCountryCallingCodesTextView.text = callingCodes.getOrNull(0)
                    tabletCountryRegionTextView.text = region
                    tabletCountryCurrenciesTextView.text = currencyNames.getOrNull(0)
                    country_flag.loadSvgImage(this)
                }
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

    private fun ImageView.loadSvgImage(country: CountryDetails) {
        val requestBuilder = GlideApp.with(requireContext())
            .`as`(PictureDrawable::class.java)
            .listener(SvgSoftwareLayerSetter())
        requestBuilder.load(country.flag.toUri()).into(this)
    }
}