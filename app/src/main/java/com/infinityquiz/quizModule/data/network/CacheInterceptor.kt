package com.infinityquiz.quizModule.data.network

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * CacheInterceptor is an OkHttp interceptor responsible for adding a Cache-Control header
 * to all outgoing responses. This header instructs the client (e.g., a browser or OkHttp's
 * own caching mechanism) to cache the response for a specified duration.
 *
 * This specific implementation sets the `max-age` directive to 10 days, meaning that
 * clients should consider the response fresh for up to 10 days after it's received.
 *
 * Note that this interceptor only adds the cache control header, it does not handle the reading of cached data.
 * It's main goal is to enforce caching on the client side.
 */
class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(10, TimeUnit.DAYS)
            .build()
        return response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}