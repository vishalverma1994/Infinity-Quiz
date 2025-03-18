package com.infinityquiz.quizModule.data.network

import android.content.Context
import com.infinityquiz.quizModule.util.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * An OkHttp Interceptor that forces the use of the cache when the device is offline.
 *
 * This interceptor checks the device's network connectivity. If no internet connection is
 * available, it modifies the request to use the cache exclusively, bypassing the network
 * entirely. If an internet connection is available, it allows the request to proceed normally.
 *
 * @property context The application context, used to check for network availability.
 */
class ForceCacheInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        if (!NetworkUtils.isInternetAvailable(context)) {
            builder.cacheControl(CacheControl.FORCE_CACHE);
        }
        return chain.proceed(builder.build());
    }
}
