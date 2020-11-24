package com.example.countryinfo

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CountryDetailsFragmentTest::class,
    CountryPreviewFragmentTest::class
)
class FragmentsTestSuite {
}