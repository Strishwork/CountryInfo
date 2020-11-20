package com.example.countryinfo

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.IdlingRegistry
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class CountryDetailsFragmentTest {

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun test() {
        val fragmentArgs = Bundle().apply {
            putString("country_id", "3")
        }

        val scenario = launchFragmentInContainer<CountryDetailsFragment>(fragmentArgs)
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 0, R.id.titleText, "Afghanistan")
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 1, R.id.titleText, "Kabul")
        assertDisplayedAtPosition(R.id.detailsRecyclerView, 2, R.id.titleText, "Asia")
        assertRecyclerViewItemCount(R.id.detailsRecyclerView, 8);
    }

}