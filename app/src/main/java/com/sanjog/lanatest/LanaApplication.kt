package com.sanjog.lanatest

import android.app.Application
import android.content.Context

/**
 * Created by sanjogstha on 2019-12-12.
 * sanjogshrestha.nepal@gmail.com
 */
class LanaApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: LanaApplication? = null

        fun getApplicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}