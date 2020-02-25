package com.example.pedidosya.repository.api

import com.example.pedidosya.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroBase {

    companion object {
        var retrofit: Retrofit? = null
        var okHttpClient: OkHttpClient? = null

        fun call(): Endpoints {
            if (retrofit == null) {

                okHttpClient = OkHttpClient().newBuilder().build()

                retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient!!)
                    .baseUrl(BuildConfig.BASE_URL)
                    .build()

                return retrofit!!.create(Endpoints::class.java)
            } else
                return retrofit!!.create(Endpoints::class.java)
        }

    }
}