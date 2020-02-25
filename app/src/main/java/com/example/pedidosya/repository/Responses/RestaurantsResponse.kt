package com.example.pedidosya.repository.Responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RestaurantsResponse : Serializable{

    @SerializedName("total")
    var total : Int = 0

    @SerializedName("max")
    var max : Int = 0

    @SerializedName("sort")
    var sort : String? = null

    @SerializedName("data")
    var data : ArrayList<restaurantData?>? = null

    class restaurantData {
        @SerializedName("deliveryTimeMinMinutes")
        var deliveryTimeMinMinutes : Int = 0

        @SerializedName("validReviewsCount")
        var validReviewsCount : Int = 0

        @SerializedName("cityName")
        var cityName : String = ""

        @SerializedName("coordinates")
        var coordinates : String? = null

        @SerializedName("logo")
        var logo : String? = null

        @SerializedName("total")
        var total : Int = 0

        @SerializedName("name")
        var name : String = ""


    }



}