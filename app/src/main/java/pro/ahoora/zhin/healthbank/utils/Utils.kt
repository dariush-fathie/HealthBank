package pro.ahoora.zhin.healthbank.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.IBinder
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import io.realm.Realm
import pro.ahoora.zhin.healthbank.activitys.DetailActivity
import pro.ahoora.zhin.healthbank.models.*

object Utils {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.activeNetworkInfo
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }



    fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    fun closeKeyBoard(token: IBinder, context: Context) {
        try {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(token, 0)
        } catch (e: Exception) {
            Log.e("ERR_KEYBOARD", e.message + " ")
        }

    }

}
