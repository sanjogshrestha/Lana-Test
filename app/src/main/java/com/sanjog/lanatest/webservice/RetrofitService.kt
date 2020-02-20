package com.sanjog.lanatest.webservice

import com.sanjog.lanatest.data.model.ProductResultsDto
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by sanjogstha on 2019-12-12.
 * sanjogshrestha.nepal@gmail.com
 */

interface RetrofitService{
    @GET("bins/4bwec")
    fun getProducts(): Observable<ProductResultsDto>
}


