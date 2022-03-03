package com.example.androidnetworking.network.retrofit

import com.example.android_advanced_kotlin.activity.network.volley.VolleyHttp
import com.example.androidnetworking.network.retrofit.services.PosterService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHttp {
    val IS_TESTER = true
    val SERVER_DEVELOPMENT = "https://jsonplaceholder.typicode.com/"
    val SERVER_PRODUCTION = "https://jsonplaceholder.typicode.com/"

    val retrofit =
        Retrofit.Builder().baseUrl(server()).addConverterFactory(GsonConverterFactory.create()).build()

    fun server(): String {
        if (VolleyHttp.IS_TESTER)
            return VolleyHttp.SERVER_DEVELOPMENT
        return VolleyHttp.SERVER_PRODUCTION
    }

    val posterService: PosterService = retrofit.create(PosterService::class.java)
}