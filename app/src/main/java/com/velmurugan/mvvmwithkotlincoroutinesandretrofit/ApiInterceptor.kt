package com.velmurugan.mvvmwithkotlincoroutinesandretrofit

import android.content.ContentValues
import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import okio.BufferedSource
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.nio.charset.StandardCharsets

class ApiInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val response = chain.proceed(chain.request())

        val responseBody = response.body

        val bufferedSource: BufferedSource? = responseBody?.source()

        bufferedSource?.request(Long.MAX_VALUE)

        val buffer: Buffer? = bufferedSource?.buffer

        val responseString = buffer?.clone()?.readString(StandardCharsets.UTF_8)

        try {
            val json: Any = JSONTokener(responseString).nextValue()

            if (json is JSONObject) {
                responseString?.let {
                    val jsonObject = JSONObject(responseString)

                    if (jsonObject.has("response") && jsonObject.getString("response") == "false") {
                        jsonObject.remove("response")

                        val modifiedResponseBody: ResponseBody = jsonObject.toString()
                            .toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())

                        return response.newBuilder().body(modifiedResponseBody).build()
                    }
                }
            }
        } catch (exception: JSONException) {
            Log.d(ContentValues.TAG, "intercept: " + exception.localizedMessage)
        }

        return response
    }

}