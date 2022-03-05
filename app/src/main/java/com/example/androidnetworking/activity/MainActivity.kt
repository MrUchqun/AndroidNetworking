package com.example.androidnetworking.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.*
import com.example.androidnetworking.R
import com.example.androidnetworking.adapter.PosterAdapter
import com.example.androidnetworking.helper.RecyclerItemTouchHelper
import com.example.androidnetworking.model.Poster
import com.example.androidnetworking.model.PosterResp
import com.example.androidnetworking.network.retrofit.RetrofitHttp
import com.example.androidnetworking.network.volley.VolleyHandler
import com.example.androidnetworking.network.volley.VolleyHttp
import com.example.androidnetworking.utils.Logger
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pbLoading: ProgressBar
    private lateinit var btnFloating: FloatingActionButton

    var posters = ArrayList<Poster>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        pbLoading = findViewById(R.id.pb_loading)
        btnFloating = findViewById(R.id.btn_floating)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        itemTouchHelperCallback(recyclerView)
        apiPosterList()
    }

    fun refreshAdapter(posters: ArrayList<Poster>) {
        recyclerView.adapter = PosterAdapter(this, posters)
    }

    private fun itemTouchHelperCallback(recyclerView: RecyclerView) {
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val itemTouchHelperCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT,
            object : RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int,
                    position: Int
                ) {

                }
            }
        )

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
    }

    private fun apiPosterList() {
        pbLoading.visibility = View.VISIBLE
        pbLoading.progress = Animation.INFINITE
        VolleyHttp.get(VolleyHttp.API_LIST_POST, VolleyHttp.paramsEmpty(), object : VolleyHandler {
            override fun onSuccess(response: String?) {
                val postArray = Gson().fromJson(response, Array<Poster>::class.java)
                posters.clear()
                posters.addAll(postArray)
                refreshAdapter(posters)
                pbLoading.visibility = View.GONE
            }

            override fun onError(error: String?) {
                Logger.e("@@@apiPosterList/error", error!!)
                pbLoading.visibility = View.GONE
            }

        })
    }

    private fun apiPosterDelete(poster: Poster) {
        pbLoading.visibility = View.VISIBLE
        pbLoading.progress = 100
        VolleyHttp.del(VolleyHttp.API_DELETE_POST + poster.id, object : VolleyHandler {
            override fun onSuccess(response: String?) {
                Logger.d("@@@apiPosterDelete", response!!)
                apiPosterList()
                pbLoading.visibility = View.GONE
            }

            override fun onError(error: String?) {
                Logger.e("@@@apiPosterDelete/error", error!!)
                pbLoading.visibility = View.GONE
            }
        })
    }

    fun dialogPoster(poster: Poster?) {
        AlertDialog.Builder(this).setTitle("Delete Poster")
            .setMessage("Are you sure you want to delete this poster?")
            .setPositiveButton(android.R.string.yes) { dialog, which ->
                apiPosterDelete(poster!!)
            }
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }


    /*//Volley and Retrofit for Network
    //Volley
    fun workWithVolley() {
         VolleyHttp.get(VolleyHttp.API_LIST_POST, VolleyHttp.paramsEmpty(), object : VolleyHandler {
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

       })
    }

    //Retrofit2
    fun workWithRetrofit() {

        RetrofitHttp.posterService.listPost().enqueue(object : Callback<ArrayList<PosterResp>> {
            override fun onResponse(
                call: Call<ArrayList<PosterResp>>,
                response: Response<ArrayList<PosterResp>>
            ) {
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

        RetrofitHttp.posterService.updatePost(poster.id, poster)
            .enqueue(object : Callback<PosterResp> {
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
    }*/
}