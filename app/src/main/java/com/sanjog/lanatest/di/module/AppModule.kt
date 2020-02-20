package com.sanjog.lanatest.di.module

import android.app.Application
import dagger.Module
import javax.inject.Singleton
import dagger.Provides

/**
 * Created by Sanjog Shrestha on 2020-02-20.
 */
@Module
class AppModule(private val app: Application) {
    @Provides
    @Singleton
    fun provideApplication(): Application {
        return app
    }
}