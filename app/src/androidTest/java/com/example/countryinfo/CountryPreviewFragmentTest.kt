package com.example.countryinfo

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.schibsted.spain.barista.assertion.BaristaImageViewAssertions.assertHasDrawable
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class CountryPreviewFragmentTest {

    @Test
    fun test() {
        val scenario = launchFragmentInContainer<CountryPreviewFragment>()
        assertHasDrawable(R.id.smileImageView, R.drawable.smile)
    }

}