package com.sanjog.lanatest.di.component

import dagger.Component
import com.sanjog.lanatest.ui.MainActivity
import com.sanjog.lanatest.di.module.ApiModule
import com.sanjog.lanatest.di.module.AppModule
import com.sanjog.lanatest.ui.ListFragment
import javax.inject.Singleton

/**
 * Created by Sanjog Shrestha on 2020-02-20.
 */
@Singleton
@Component(modules = [AppModule::class, ApiModule::class])
interface ApplicationComponent {
    fun inject(listFragment: ListFragment)
    fun inject(mainActivity: MainActivity)
}