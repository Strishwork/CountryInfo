package com.example.countryinfo

import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideApp
import kotlinx.android.synthetic.main.country_details_fragment.view.*
import javax.inject.Inject

class CountryDetailsFragment : Fragment(), CountryDetailsAdapter.ViewHolder.OnItemClickListener {

    companion object {
        private const val COUNTRY_ID = "country_id"
        fun newInstance(countryId: String) = CountryDetailsFragment().apply {
            arguments = bundleOf(COUNTRY_ID to countryId)
        }
    }

    private lateinit var rootView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CountryDetailsAdapter

    @Inject
    lateinit var viewModelFactory: CountryDetailsViewModelFactory
    private val viewModel: CountryDetailsViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.country_details_fragment, container, false)
        recyclerView = rootView.detailsRecyclerView
        adapter = CountryDetailsAdapter(emptyList(), this)
        recyclerView.adapter = adapter
        (activity?.application as CountriesApplication).countriesComponent.inject(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val id = arguments?.get(COUNTRY_ID) as? String
        viewModel.countryLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            handleState(it)
            EspressoIdlingResource.decrement()
        })
        if (id != null) {
            EspressoIdlingResource.increment()
            viewModel.countryClicked(id)
        }
    }

    private fun handleState(countryDetailsViewState: CountryDetailsViewState) {
        when (countryDetailsViewState) {
            is CountryDetailsViewState.Default -> {
                adapter.setCountryStates(countryDetailsViewState.detailsViewHolderStateList)
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

    override fun onItemClick(section: DetailsSections, dialogMessage: List<String>) {
        showDialog(section, dialogMessage)
    }

    private fun showDialog(section: DetailsSections, dialogMessage: List<String>) {
        val dialogBuilder = AlertDialog.Builder(requireContext()).setTitle(section.title)
        val message = dialogMessage.joinToString(separator = "\n")
        dialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, id ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun loadFlag(country: CountryDetails) {
        val requestBuilder = GlideApp.with(requireContext())
            .`as`(PictureDrawable::class.java)
            .listener(SvgSoftwareLayerSetter())
        requestBuilder.load(country.flag.toUri()).into(rootView.countryFlagImage)
    }

}