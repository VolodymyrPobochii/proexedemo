package com.pobochii.someapp.data.di

import com.google.gson.GsonBuilder
import com.pobochii.someapp.data.DateDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Singleton
    @Provides
    fun provideGson(): GsonConverterFactory = GsonConverterFactory.create(
        GsonBuilder().apply {
            registerTypeAdapter(Date::class.java, DateDeserializer())
        }.create()
    )

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient, gsonFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://client.proexe.eu/")
            .addConverterFactory(gsonFactory)
            .client(httpClient)
            .build()
    }
}