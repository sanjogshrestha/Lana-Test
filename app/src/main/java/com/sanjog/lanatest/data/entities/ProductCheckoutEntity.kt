package com.sanjog.lanatest.data.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by sanjogstha on 2019-12-17.
 * sanjogshrestha.nepal@gmail.com
 */
@Entity(tableName = "product_checkout")
data class ProductCheckoutEntity(  @PrimaryKey @NonNull
                                   var code : String,
                                   var name : String,
                                   var price : Double,
                                   var count: Int,
                                   var expectedTotal : Double,
                                   var total : Double)