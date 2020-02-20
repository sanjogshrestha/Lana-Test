package com.sanjog.lanatest

import android.app.Application
import android.content.Context
import com.sanjog.lanatest.di.component.ApplicationComponent
import com.sanjog.lanatest.di.component.DaggerApplicationComponent
import com.sanjog.lanatest.di.module.ApiModule
import com.sanjog.lanatest.di.module.AppModule

/**
 * Created by sanjogstha on 2019-12-12.
 * sanjogshrestha.nepal@gmail.com
 */
class LanaApplication : Application() {
    lateinit var mApplicationComponent: ApplicationComponent

    init {
        instance = this
        initDagger(this)
    }

    private fun initDagger(app: LanaApplication){
        mApplicationComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .apiModule(ApiModule("https://api.myjson.com/"))
            .build()
    }

    companion object {
        private var instance: LanaApplication? = null

        fun getApplicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}