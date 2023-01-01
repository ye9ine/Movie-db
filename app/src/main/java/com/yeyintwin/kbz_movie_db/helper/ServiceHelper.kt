package com.yeyintwin.kbz_movie_db.helper

import MovieDbApi
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceHelper {
    val api: MovieDbApi by lazy {

        val okHttpBuilder = OkHttpClient.Builder()
            .addInterceptor(Interceptor())
            .readTimeout(25, TimeUnit.SECONDS)
            .connectTimeout(25, TimeUnit.SECONDS)

        val okClient = okHttpBuilder.build()
        Retrofit.Builder()
            .baseUrl(MovieDbConstant().rootAddress)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okClient)
            .build()
            .create(MovieDbApi::class.java)
    }

    class Interceptor: okhttp3.Interceptor {
        override fun intercept(chain: okhttp3.Interceptor.Chain): Response {
            val original: Request = chain.request()
            val originalHttpUrl: HttpUrl = original.url
            val maxAge = 60

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", MovieDbConstant().apiKey)
                .build()

            val requestBuilder: Request.Builder = original.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .url(url)
            val request: Request = requestBuilder.build()
            return chain.proceed(request)
        }
    }
}