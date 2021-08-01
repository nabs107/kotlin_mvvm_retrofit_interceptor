package com.velmurugan.mvvmwithkotlincoroutinesandretrofit

import android.content.ContentValues.TAG
import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import okio.BufferedSource
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.nio.charset.StandardCharsets

interface RetrofitService {

    @GET("movielist.json")
    suspend fun getAllMovies() : Response<List<Movie>>

    @GET("success_products.json")
    suspend fun getProducts() : Response<ProductsApi>

    companion object {
        var retrofitService: RetrofitService? = null

        var interceptor: ApiInterceptor = ApiInterceptor()

        var okHttpClient: OkHttpClient = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://gist.githubusercontent.com/nabs107/db7c726080f43154f7f4d93f7f935d0c/raw/b32e72cbde69db35c68c0643e94860cbc1ef5cdd/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}