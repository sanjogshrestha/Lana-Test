package com.sanjog.lanatest.data.model

/**
 * Created by sanjogstha on 2019-12-15.
 * sanjogshrestha.nepal@gmail.com
 */
data class ProductDto(val code : String,
                      val name : String,
                      val price : Double,
                      var count: Int = 0)