package com.example.countryinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.countryinfo.model.CountriesApi
import com.example.countryinfo.model.apolloClient

class CountryDetailsFragment : Fragment() {

    companion object {
        private const val COUNTRY_ID = "country_id"
        fun newInstance(countryId: String) = CountryDetailsFragment().apply {
            arguments = bundleOf(COUNTRY_ID to countryId)
        }
    }

    private lateinit var rootView: View
    private lateinit var viewModel: CountryDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.country_details_fragment, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(
            this, CountryDetailsViewModelFactory(
                CountriesApi(
                    apolloClient(
                        requireContext()
                    )
                )
            )
        )
            .get(CountryDetailsViewModel::class.java)
        viewModel.pollLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (val countryDetailsViewState = it) {
                is CountryDetailsViewState.Default -> {

                }
                is CountryDetailsViewState.Error -> {
                    Toast.makeText(
                        context,
                        countryDetailsViewState.error.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

}