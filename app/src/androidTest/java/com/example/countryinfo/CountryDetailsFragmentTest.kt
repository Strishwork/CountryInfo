package com.example.countryinfo

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.schibsted.spain.barista.assertion.BaristaImageViewAssertions.assertHasDrawable
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountryDetailsFragmentTest {

    @Test
    fun test() {
        val fragmentArgs = Bundle().apply {
            putString("country_id", "3")
        }

        val scenario = launchFragmentInContainer<CountryDetailsFragment>(fragmentArgs)
        assertHasDrawable(R.id.smileImageView, R.drawable.smile);
    }

}