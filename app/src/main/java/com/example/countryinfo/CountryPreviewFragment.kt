package com.example.countryinfo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countryinfo.model.CountriesApi
import com.example.countryinfo.model.apolloClient
import kotlinx.android.synthetic.main.country_preview_layout.view.*

class CountryPreviewFragment : Fragment() {

    companion object {
        fun newInstance() = CountryPreviewFragment()
    }

    private lateinit var rootView: View
    private lateinit var viewModel: CountriesPreviewViewModel
    private lateinit var adapter: CountryPreviewAdapter
    private lateinit var listener: ItemClickedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.country_preview_layout, container, false)
        initializeRecyclerView()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(
            this, CountriesPreviewViewModelFactory(
                CountriesApi(
                    apolloClient(
                        requireContext()
                    )
                )
            )
        )
            .get(CountriesPreviewViewModel::class.java)
        viewModel.pollLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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