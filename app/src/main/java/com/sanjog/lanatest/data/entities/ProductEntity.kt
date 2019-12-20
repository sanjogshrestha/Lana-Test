package com.sanjog.lanatest.data.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by sanjogstha on 2019-12-17.
 * sanjogshrestha.nepal@gmail.com
 */
@Entity(tableName = "product")
data class ProductEntity(@PrimaryKey @NonNull
                         var code : String,
                         var name : String,
                         var price : Double,
                         var lastUpdate : Long)