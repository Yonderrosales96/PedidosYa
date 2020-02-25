package com.example.pedidosya.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.pedidosya.R
import com.example.pedidosya.repository.Responses.RestaurantsResponse
import com.example.pedidosya.repository.Responses.RestaurantsResponse.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SharedPreferencesUtils {

    companion object {

        fun saveToken(context: Context, token: String) {

            val fileName = context.resources.getString(R.string.filename)
            val key = context.resources.getString(R.string.key)
            val sharedPreferencesUtil = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
            sharedPreferencesUtil.edit().putString(key, token).apply()
        }

        fun getToken(context: Context) : String{
            val fileName = context.resources.getString(R.string.filename)
            val key = context.resources.getString(R.string.key)
            val sharedPreferencesUtil = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
            val token = sharedPreferencesUtil.getString(key, "")
            return token!!
        }

        fun saveRestaurants(context: Context, data: ArrayList<restaurantData?>){
            val fileName = context.resources.getString(R.string.filename)
            val key = context.resources.getString(R.string.keyData)
            val sharedPreferencesUtil = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
            val json = Gson().toJson(data)
            sharedPreferencesUtil.edit().putString(key, json).apply()
        }

        fun cleanRestaurants(context: Context){
            val fileName = context.resources.getString(R.string.filename)
            val sharedPreferencesUtil = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
            sharedPreferencesUtil.edit().putString(context.resources.getString(R.string.keyData),"").apply()

        }

        fun getRestaurantes(context: Context): ArrayList<restaurantData>? {

            val fileName = context.resources.getString(R.string.filename)
            val key = context.resources.getString(R.string.keyData)
            val sharedPreferencesUtil = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
            val data = sharedPreferencesUtil.getString(key,"")
            if (data.isNullOrEmpty())
                return null
            else {
                val type: Type? = object : TypeToken<ArrayList<restaurantData>>() {}.type
                return Gson().fromJson(data, type)
            }

        }

        fun cleanData(context: Context){

            val fileName=context.resources.getString(R.string.filename)
            val sharedPreferencesUtil = context.getSharedPreferences(fileName,Context.MODE_PRIVATE)

            sharedPreferencesUtil.edit().putString(context.resources.getString(R.string.keyData),"").apply()
            sharedPreferencesUtil.edit().putString(context.resources.getString(R.string.key),"").apply()



        }

    }



}