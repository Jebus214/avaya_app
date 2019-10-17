package com.example.avaya_app.services



import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*
import retrofit2.Call


interface APIService {



    @Multipart
    @POST("events")
    fun registrationPost(
        @Part family: MultipartBody.Part,
        @Part type: MultipartBody.Part,
        @Part version: MultipartBody.Part,
        @Part eventBody: MultipartBody.Part): Call<ResponseBody>



}



object ApiUtils {

    val BASE_URL = "http://breeze2-132.collaboratory.avaya.com/services/EventingConnector/"

    val apiService: APIService
        get() = RetrofitClient.getClient(BASE_URL)!!.create(
            APIService::class.java)

}

