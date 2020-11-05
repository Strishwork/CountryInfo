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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countryinfo.model.CountriesApi
import com.example.countryinfo.model.apolloClient
import com.github.twocoffeesoneteam.glidetovectoryou.GlideApp
import kotlinx.android.synthetic.main.country_details_fragment.view.*

class CountryDetailsFragment : Fragment(), CountryDetailsAdapter.ViewHolder.OnItemClickListener {

    companion object {
        private const val COUNTRY_ID = "country_id"
        private const val DETAILS_DIALOG = "dialog"
        fun newInstance(countryId: String) = CountryDetailsFragment().apply {
            arguments = bundleOf(COUNTRY_ID to countryId)
        }
    }

    private lateinit var rootView: View
    private lateinit var viewModel: CountryDetailsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CountryDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.country_details_fragment, container, false)
        recyclerView = rootView.detailsRecyclerView
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val id = arguments?.get(COUNTRY_ID) as String
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
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    adapter = CountryDetailsAdapter(countryDetailsViewState.countryDetails, this)
                    recyclerView.adapter = adapter
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
        })
        viewModel.countryClicked(id)
    }

    override fun onItemClick(state: DetailsStates, dialogMessage: List<String>) {
        activity?.supportFragmentManager?.let {
            CountryDetailsDialog.newInstance(
                state,
                ArrayList(dialogMessage)
            ).show(it, DETAILS_DIALOG)
        }
    }

    private fun loadFlag(country: CountryDetails) {
        val requestBuilder = GlideApp.with(requireContext())
            .`as`(PictureDrawable::class.java)
            .listener(SvgSoftwareLayerSetter())
        requestBuilder.load(country.flag.toUri()).into(rootView.countryFlagImage)
    }

}