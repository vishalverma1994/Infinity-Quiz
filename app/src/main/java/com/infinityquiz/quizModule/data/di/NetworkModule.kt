package com.infinityquiz.quizModule.data.di

import android.content.Context
import com.google.gson.Gson
import com.infinityquiz.quizModule.data.network.CacheInterceptor
import com.infinityquiz.quizModule.data.network.ForceCacheInterceptor
import com.infinityquiz.quizModule.data.network.QuizApi
import com.infinityquiz.quizModule.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    /**
     * Provides a singleton instance of Gson.
     *
     * Gson is a Java library that can be used to convert Java Objects into their JSON representation.
     * It can also be used to convert a JSON string to an equivalent Java object.
     *
     * This function is annotated with:
     *   - `@Provides`: Indicates that this function provides a dependency that can be injected by Dagger.
     *   - `@Singleton`: Indicates that this function should provide a single instance of Gson throughout the application's lifecycle.
     *
     * @return A singleton instance of Gson.
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    /**
     * Provides a configured Retrofit instance for making network requests.
     *
     * This function builds a Retrofit instance with the specified base URL,
     * OkHttpClient for network handling, and Gson for JSON serialization/deserialization.
     *
     * The Retrofit instance is configured with:
     *   - A base URL, defined by the [BASE_URL] constant.
     *   - An [OkHttpClient] instance for handling network requests and responses. This can include
     *     interceptors for logging, headers, or other custom behaviors.
     *   - A [GsonConverterFactory] to convert JSON responses to Kotlin objects (and vice-versa)
     *     using a provided [Gson] instance.
     *
     * This function is annotated with:
     *   - `@Provides`: Indicates that this function provides a dependency that can be injected.
     *   - `@Singleton`: Indicates that a single instance of Retrofit should be created and shared
     *     throughout the application's lifecycle.
     *
     * @param okHttpClient The OkHttpClient instance to use for network requests.
     * @param gson The Gson instance to use for JSON serialization/deserialization.
     * @return A configured Retrofit instance.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    /**
     * Provides a singleton instance of OkHttpClient configured with caching and interceptors.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(Cache(File(context.cacheDir, "http-cache"), 10L * 1024L * 1024L)) // 10 MiB
            .addNetworkInterceptor(CacheInterceptor()) // only if Cache-Control header is not enabled from the server
            .addInterceptor(ForceCacheInterceptor(context))
            .build()
    }

    /**
     * Provides an instance of the [QuizApi] service.
     *
     * This function is a Dagger provider method that creates and returns an instance of the
     * [QuizApi] interface, which is used to interact with the quiz API. It leverages Retrofit
     * to create a type-safe API client.
     *
     * @param retrofit The pre-configured [Retrofit] instance used to build the API service.
     *                 This instance should already be set up with the base URL, converters,
     *                 and any necessary interceptors.
     * @return An instance of [QuizApi] that can be used to make API calls.
     * @throws IllegalStateException if Retrofit is not properly configured
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): QuizApi {
        return retrofit.create(QuizApi::class.java)
    }
}