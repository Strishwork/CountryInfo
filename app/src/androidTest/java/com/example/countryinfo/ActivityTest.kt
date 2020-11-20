package com.example.countryinfo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.schibsted.spain.barista.assertion.BaristaListAssertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class ActivityTest {

    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun test_isActivityInView() {
//        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.previewRecyclerViewLayout)).check(matches(isDisplayed()))

        BaristaListAssertions.assertDisplayedAtPosition(
            R.id.detailsRecyclerView,
            0,
            R.id.country_name,
            "Afghanistan"
        )
        BaristaListAssertions.assertDisplayedAtPosition(
            R.id.detailsRecyclerView,
            0,
            R.id.country_capital,
            "Kabul"
        )
        BaristaListAssertions.assertDisplayedAtPosition(
            R.id.detailsRecyclerView,
            0,
            R.id.country_region,
            "Asia"
        )
    }

    @Test
    fun test_title_visibility() {
//        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.chooseCardTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isTitleTextDisplayed() {
//        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.chooseCardTextView)).check(matches(withText(R.string.choose_card_label)))

    }
}