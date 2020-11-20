package com.example.countryinfo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.country_preview_layout.view.*
import javax.inject.Inject

class CountryPreviewFragment : Fragment() {

    companion object {
        fun newInstance() = CountryPreviewFragment()
    }

    private lateinit var rootView: View
    private lateinit var adapter: CountryPreviewAdapter
    private lateinit var listener: ItemClickedListener

    @Inject
    lateinit var viewModelFactory: CountriesPreviewViewModelFactory
    private val viewModel: CountriesPreviewViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.country_preview_layout, container, false)
        initializeRecyclerView()
        (activity?.application as CountriesApplication).countriesComponent.inject(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        EspressoIdlingResource.increment()
        viewModel.countriesLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (val countriesPreviewViewState = it) {
                is CountriesPreviewViewState.Default -> {
                    adapter.updateAdapterValues(countriesPreviewViewState.countries)
                }
                is CountriesPreviewViewState.Error -> {
                    Toast.makeText(
                        context,
                        countriesPreviewViewState.error.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            EspressoIdlingResource.decrement()
        })
    }

    private fun initializeRecyclerView() {
        val recyclerView = rootView.detailsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = CountryPreviewAdapter(
            emptyList()
        ) { item -> listener.itemClicked(item) }
        recyclerView.adapter = adapter
    }

    interface ItemClickedListener {
        fun itemClicked(id: String)
    }

}