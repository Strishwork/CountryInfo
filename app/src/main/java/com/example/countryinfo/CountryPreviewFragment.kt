package com.example.countryinfo

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import com.example.countryinfo.model.CountriesApi
import com.example.countryinfo.model.apolloClient
import com.github.twocoffeesoneteam.glidetovectoryou.GlideApp

class CountryPreviewFragment : Fragment() {

    companion object {
        fun newInstance() = CountryPreviewFragment()
    }

    private lateinit var rootView: View
    private lateinit var countriesApi: CountriesApi
    private lateinit var viewModel: CountriesPreviewViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CountryPreviewAdapter
    private lateinit var requestBuilder: RequestBuilder<PictureDrawable>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        countriesApi = CountriesApi(
            apolloClient(
                requireContext()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.country_preview_layout, container, false)
        requestBuilder = GlideApp.with(requireContext())
            .`as`(PictureDrawable::class.java)
            .listener(SvgSoftwareLayerSetter())
        initializeRecyclerView()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, CountriesPreviewViewModelFactory(countriesApi))
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
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = CountryPreviewAdapter(emptyList(), requestBuilder)
        recyclerView.adapter = adapter
    }

}