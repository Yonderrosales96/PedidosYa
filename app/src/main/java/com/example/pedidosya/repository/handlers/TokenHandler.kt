package com.example.pedidosya.repository.handlers

import com.example.pedidosya.R
import com.example.pedidosya.repository.Responses.TokenResponse
import com.example.pedidosya.repository.api.RetroBase
import retrofit2.Callback

class TokenHandler {

    companion object {
        fun getToken(clientid: String,clientpassword: String,callback: Callback<TokenResponse>) {
            RetroBase.call().getToken(clientid, clientpassword).enqueue(callback)
        }
    }
}