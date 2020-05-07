package com.example.githubsearch.repository

import android.content.Context
import android.net.ConnectivityManager
import com.example.githubsearch.R
import com.example.githubsearch.helper.network.NetworkConnectionInterceptor
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ConfigurationAPI {
    fun create(context: Context): GithubResApi {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(context.resources.getString(R.string.host_name))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(createClientConfiguration(connectivityManager))
            .build()

        return retrofit.create(GithubResApi::class.java)
    }

    private fun createClientConfiguration(connectivityManager: ConnectivityManager): OkHttpClient{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(NetworkConnectionInterceptor(connectivityManager))

        return okHttpClient.addInterceptor(logging).build()
    }
}