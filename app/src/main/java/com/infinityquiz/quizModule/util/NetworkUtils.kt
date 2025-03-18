package com.infinityquiz.quizModule.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * `NetworkUtils` is a utility object that provides methods for checking network-related conditions.
 */
object NetworkUtils {

    /**
     * Checks if the device has an active internet connection.
     *
     * This function verifies that the device is connected to a network and that the network
     * has the capabilities to access the internet and is validated.
     *
     * @param context The application context. This is required to access the ConnectivityManager.
     * @return `true` if the device has an active and validated internet connection, `false` otherwise.
     */
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager ?: return false

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}