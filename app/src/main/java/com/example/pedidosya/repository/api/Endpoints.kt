package com.example.pedidosya.repository.api

import com.example.pedidosya.repository.Responses.RestaurantsResponse
import com.example.pedidosya.repository.Responses.TokenResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface Endpoints {

    @GET("tokens")
    @Headers("Content-Type: application/json","Accept: application/json")
    fun getToken(@Query("clientId") clientId: String, @Query("clientSecret") clientSecret : String ) : Call<TokenResponse>


    @GET("search/restaurants")
    @Headers("Content-Type: application/json","Accept: application/json")
    fun getRestaurants(@Header("Authorization") authorization : String,@Query("point") point : String, @Query("country") country : Int,@Query("max") max : Int,@Query("offset") offset : Int ) : Call<RestaurantsResponse>


}