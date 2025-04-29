package com.markdev.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.markdev.tasks.R
import com.markdev.tasks.service.exception.NoInternetException
import retrofit2.Response

open class BaseRepository(private val context: Context) {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Response<T> {
        if (!isConnectionAvailable()) {
            throw NoInternetException(context.getString(R.string.error_internet_connection))
        }
        return apiCall()
    }

    private fun isConnectionAvailable(): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNet = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(activeNet) ?: return false
            result = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            if (connectivityManager.activeNetwork != null) {
                result = when (connectivityManager.activeNetworkInfo!!.type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return result
    }
}