package com.example.countryinfo

import com.example.countryinfo.dagger.AppComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ TestAppModule::class ])
interface TestAppComponent : AppComponent {

    fun inject(target: CountryPreviewFragmentTest)

}