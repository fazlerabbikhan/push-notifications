package com.fazlerabbikhan.pushnotifications.service

import com.fazlerabbikhan.pushnotifications.data.TokenRequest
import com.fazlerabbikhan.pushnotifications.data.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("post")
    fun updateToken(@Body token: TokenRequest): Call<TokenResponse>
}