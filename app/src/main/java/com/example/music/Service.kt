package com.example.music

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Service {
    @FormUrlEncoded
    @POST("/Music.php")
    fun postRequst(@Field("name")id:String, @Field("genre")genre:String) : Call<ResponseBody>
}