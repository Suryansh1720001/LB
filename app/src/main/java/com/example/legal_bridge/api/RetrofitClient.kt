package com.example.legal_bridge.api

import com.example.legal_bridge.helper.APIConstantValue
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {

        private const val BASE_URL = APIConstantValue.API_BASE_URL // Replace with your base URL


    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Adjust this value as needed
        .readTimeout(30, TimeUnit.SECONDS) // Adjust this value as needed
        .writeTimeout(30, TimeUnit.SECONDS) // Adjust this value as needed
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()


    // Return ApiService
    fun getApiService(): APIService {
        return retrofit.create(APIService::class.java)
    }
}
