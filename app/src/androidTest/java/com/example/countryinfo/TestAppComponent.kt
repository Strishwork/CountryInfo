package com.example.countryinfo

import com.example.countryinfo.dagger.AppComponent
import com.example.countryinfo.dagger.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, MockViewModelFactoriesModule::class])
interface TestAppComponent : AppComponent {

    fun inject(target: CountryDetailsFragmentTest)
    fun inject(target: CountryPreviewFragmentTest)
    fun inject(target: FragmentNavigationTest)

}