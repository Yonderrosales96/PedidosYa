package com.example.pedidosya.presenter

import android.content.Context
import com.example.pedidosya.repository.Responses.RestaurantsResponse
import com.example.pedidosya.repository.Responses.TokenResponse
import com.example.pedidosya.repository.handlers.RestaurantHandler
import com.example.pedidosya.repository.handlers.TokenHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityPresenter(val view : View ) {

    fun getToken(clientid : String,clientpassword: String){

        view.onLoad()
        TokenHandler.getToken(clientid,clientpassword,object : Callback<TokenResponse>{
            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                view.onError()
            }

            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.code() == 200 && response.isSuccessful)
                    view.onTokenSuccess(response.body()?.access_token!!)//validar respuestas
                else
                    view.onError()
            }

        })

    }

    fun getRestaurants(authorization : String,point : String, country : Int,max : Int, offset : Int){

        RestaurantHandler.getRestaurants(authorization,point,country,max,offset,object : Callback<RestaurantsResponse>{
            override fun onFailure(call: Call<RestaurantsResponse>, t: Throwable) {
                view.onErrorRestaurantsResponse()
            }

            override fun onResponse(
                call: Call<RestaurantsResponse>,
                response: Response<RestaurantsResponse>
            ) {
               if (response.code() == 200 && response.body() != null){
                   val size = response.body()?.data?.size
                   if (size!! > 0) {
                       response.body()?.data!!.add(size, null)
                       view.onRestaurantsSuccess(response.body()!!)
                   } else{
                       view.noRestaurantsData()
                   }
               }else{
                   view.onErrorRestaurantsResponse()
               }
            }

        })



    }


    interface View {
        fun onLoad()
        fun onTokenSuccess(token : String)
        fun onError()
        fun onRestaurantsSuccess(response: RestaurantsResponse)
        fun onErrorRestaurantsResponse()
        fun noRestaurantsData()
    }
}