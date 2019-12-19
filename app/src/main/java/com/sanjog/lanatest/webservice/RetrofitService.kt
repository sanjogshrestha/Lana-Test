package com.sanjog.lanatest.webservice

import com.sanjog.lanatest.LanaApplication
import com.sanjog.lanatest.data.model.ProductResultsDto
import io.reactivex.Observable
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

/**
 * Created by sanjogstha on 2019-12-12.
 * sanjogshrestha.nepal@gmail.com
 */
private const val cacheSize: Long = 10 * 1024 * 1024
private const val BASE_URL = "https://api.myjson.com/"
private val retrofit  =  Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(getClient())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private fun getClient(): OkHttpClient {
    val builder = OkHttpClient.Builder()
    builder.interceptors().clear()
    builder
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .cache(Cache(LanaApplication.getApplicationContext().cacheDir, cacheSize))
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
    return builder.build()
}

interface RetrofitService{
    @GET("bins/4bwec")
    fun getProducts(): Observable<ProductResultsDto>
}

object RetrofitApi {
    val service : RetrofitService by lazy { retrofit.create(RetrofitService::class.java)}
}

