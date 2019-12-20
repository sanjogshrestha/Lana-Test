package com.sanjog.lanatest.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sanjog.lanatest.data.dao.CheckoutDao
import com.sanjog.lanatest.data.dao.ProductDao
import com.sanjog.lanatest.data.entities.ProductCheckoutEntity
import com.sanjog.lanatest.data.entities.ProductEntity

/**
 * Created by sanjogstha on 2019-12-11.
 * sanjogshrestha.nepal@gmail.com
 */
@Database(entities = [ProductCheckoutEntity::class, ProductEntity::class],
    version = 1,
    exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        private val LOCK: Any = Object()
        private const val DATABASENAME: String = "LANA_DATABASE"
        @Volatile
        private var sInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (sInstance == null){
                synchronized(LOCK){
                    sInstance = Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java, DATABASENAME
                    )
                        .build()
                }
            }
            return sInstance!!
        }

    }
    abstract fun checkoutDao(): CheckoutDao
    abstract fun productDao(): ProductDao
}