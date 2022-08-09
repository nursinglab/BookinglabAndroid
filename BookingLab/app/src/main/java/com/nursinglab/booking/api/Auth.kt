package com.nursinglab.booking.api

import com.nursinglab.booking.component.ResponseComponent
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Auth {
    @FormUrlEncoded
    @POST("account_api/login")
    fun login(
            @Field("username") username: String?,
            @Field("password") password: String?
    ): Call<ResponseComponent?>?

    @FormUrlEncoded
    @POST("account_api/profile")
    fun profile(
            @Field("id") id: String?
    ): Call<ResponseComponent?>?
}