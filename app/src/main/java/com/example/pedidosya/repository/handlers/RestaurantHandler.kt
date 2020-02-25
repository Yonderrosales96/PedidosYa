package com.example.pedidosya.repository.handlers

import com.example.pedidosya.repository.Responses.RestaurantsResponse
import com.example.pedidosya.repository.Responses.TokenResponse
import com.example.pedidosya.repository.api.RetroBase
import retrofit2.Callback

class RestaurantHandler {

    companion object{
        fun getRestaurants(authorization : String,point : String, country : Int,max: Int, offSet : Int,callback: Callback<RestaurantsResponse>) {
            RetroBase.call().getRestaurants(authorization,point,country,max,offSet).enqueue(callback)
        }
    }

}