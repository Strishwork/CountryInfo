package com.example.countryinfo

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    FragmentNavigationTest::class,
    CountryDetailsFragmentTest::class,
    CountryPreviewFragmentTest::class
)
class FragmentsTestSuite {
}