package pro.ahoora.zhin.healthbank.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object Utils {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.activeNetworkInfo
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun getSpacialName(id: Int): String {
        /*

         */
        return ""
    }

}
