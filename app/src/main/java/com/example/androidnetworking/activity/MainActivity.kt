package com.example.androidnetworking.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.android_advanced_kotlin.activity.network.volley.VolleyHttp
import com.example.androidnetworking.R
import com.example.androidnetworking.model.Poster
import com.example.androidnetworking.model.PosterResp
import com.example.androidnetworking.network.retrofit.RetrofitHttp
import com.example.androidnetworking.network.volley.VolleyHandler
import com.example.androidnetworking.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        val tv_text = findViewById<TextView>(R.id.tv_text)

        workWithRetrofit()
       /* VolleyHttp.get(VolleyHttp.API_LIST_POST, VolleyHttp.paramsEmpty(), object : VolleyHandler {
            override fun onSuccess(response: String?) {
                Logger.d("VolleyHttp", response!!)
            }

            override fun onError(error: String?) {
                Logger.d("VolleyHttp", error!!)
            }
        })

        val poster = Poster(1, 1, "PDP", "Online")
        VolleyHttp.post(
            VolleyHttp.API_CREATE_POST,
            VolleyHttp.paramsCreate(poster),
            object : VolleyHandler {
                override fun onSuccess(response: String?) {
                    Logger.d("@@@1", response!!)
                }

                override fun onError(error: String?) {
                    Logger.d("@@@", error!!)
                }

            })

        VolleyHttp.put(
            VolleyHttp.API_UPDATE_POST + poster.id,
            VolleyHttp.paramsUpdate(poster),
            object : VolleyHandler {
                override fun onSuccess(response: String?) {
                    Logger.d("@@@2", response!!)
                }

                override fun onError(error: String?) {
                    Logger.d("@@@", error!!)
                }

            })

        VolleyHttp.del(VolleyHttp.API_DELETE_POST + poster.id, object : VolleyHandler {
            override fun onSuccess(response: String?) {
                Logger.d("@@@3", response!!)
            }

            override fun onError(error: String?) {
                Logger.d("@@@", error!!)
            }

        })*/
    }

    fun workWithRetrofit(){

        RetrofitHttp.posterService.listPost().enqueue(object : Callback<ArrayList<PosterResp>> {
            override fun onResponse(call: Call<ArrayList<PosterResp>>, response: Response<ArrayList<PosterResp>>) {
                Logger.d("@@@", response.body().toString())
            }

            override fun onFailure(call: Call<ArrayList<PosterResp>>, t: Throwable) {
                Logger.d("@@@", t.message.toString())
            }
        })


        val poster = Poster(1, 1, "PDP", "Online")

        RetrofitHttp.posterService.createPost(poster).enqueue(object : Callback<PosterResp> {
            override fun onResponse(call: Call<PosterResp>, response: Response<PosterResp>) {
                Logger.d("@@@", response.body().toString())
            }

            override fun onFailure(call: Call<PosterResp>, t: Throwable) {
                Logger.d("@@@", t.message.toString())
            }
        })

        RetrofitHttp.posterService.updatePost(poster.id, poster).enqueue(object : Callback<PosterResp> {
            override fun onResponse(call: Call<PosterResp>, response: Response<PosterResp>) {
                Logger.d("@@@", response.body().toString())
            }

            override fun onFailure(call: Call<PosterResp>, t: Throwable) {
                Logger.d("@@@", t.message.toString())
            }
        })

        RetrofitHttp.posterService.deletePost(poster.id).enqueue(object : Callback<PosterResp> {
            override fun onResponse(call: Call<PosterResp>, response: Response<PosterResp>) {
                Logger.d("@@@", "" + response.body())
            }

            override fun onFailure(call: Call<PosterResp>, t: Throwable) {
                Logger.d("@@@", "" + t.message)
            }
        })
    }
}