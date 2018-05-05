package pro.ahoora.zhin.healthbank.activitys;

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.models.Model2
import pro.ahoora.zhin.healthbank.utils.ApiClient
import pro.ahoora.zhin.healthbank.utils.ApiInterface

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val response = apiInterface.getUsers("darius")
        response.enqueue(object : Callback<Model2> {
            override fun onResponse(call: Call<Model2>, response: Response<Model2>) {
                val model = response.body()
                Log.e("RES", model?.name.toString())
            }

            override fun onFailure(call: Call<Model2>, t: Throwable) {
                Log.e("ERR", t.message + " :")
            }
        })

    }

    companion object {
        private val TAG = "RestActivity"
    }
}
