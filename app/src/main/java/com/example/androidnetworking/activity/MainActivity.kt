package com.example.androidnetworking.activity

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.core.view.updateMargins
import androidx.recyclerview.widget.*
import com.example.androidnetworking.R
import com.example.androidnetworking.adapter.PosterAdapter
import com.example.androidnetworking.model.Poster
import com.example.androidnetworking.network.volley.VolleyHandler
import com.example.androidnetworking.network.volley.VolleyHttp
import com.example.androidnetworking.utils.Logger
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.nabilmh.lottieswiperefreshlayout.LottieSwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnFloating: FloatingActionButton
    private lateinit var swipeRefreshLayout: LottieSwipeRefreshLayout

    var posters = ArrayList<Poster>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        apiPosterList()
    }

    private fun initViews() {
        btnFloating = findViewById(R.id.btn_floating)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        btnFloating.setOnClickListener {
            dialogCreatePoster()
        }

        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                stopLoading()
                apiPosterList()
            }, 2000)
        }

    }

    private fun playLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    private fun stopLoading() {
        swipeRefreshLayout.isRefreshing = false
    }

    fun refreshAdapter(posters: ArrayList<Poster>) {
        recyclerView.adapter = PosterAdapter(this, posters)
    }

    private fun apiPosterList() {
        playLoading()
        VolleyHttp.get(VolleyHttp.API_LIST_POST, VolleyHttp.paramsEmpty(), object : VolleyHandler {
            override fun onSuccess(response: String?) {
                val postArray = Gson().fromJson(response, Array<Poster>::class.java)
                posters.clear()
                posters.addAll(postArray)
                refreshAdapter(posters)
                stopLoading()
            }

            override fun onError(error: String?) {
                Logger.e("@@@apiPosterList/error", error!!)
                stopLoading()
            }

        })
    }

    private fun apiPosterDelete(poster: Poster) {
        playLoading()
        VolleyHttp.del(VolleyHttp.API_DELETE_POST + poster.id, object : VolleyHandler {
            override fun onSuccess(response: String?) {
                Logger.d("@@@apiPosterDelete", response!!)
                apiPosterList()
            }

            override fun onError(error: String?) {
                stopLoading()
                Logger.e("@@@apiPosterDelete/error", error!!)
            }
        })
    }

    private fun apiPosterCreate(poster: Poster) {
        playLoading()
        VolleyHttp.post(
            VolleyHttp.API_CREATE_POST,
            VolleyHttp.paramsCreate(poster),
            object : VolleyHandler {
                override fun onSuccess(response: String?) {
                    Logger.d("@@@apiPosterCreate", response!!)
                    apiPosterList()
                }

                override fun onError(error: String?) {
                    Logger.e("@@@apiPosterCreate/error", error!!)
                    stopLoading()
                }

            })
    }

    private fun apiPosterUpdate(poster: Poster) {
        playLoading()
        VolleyHttp.put(
            VolleyHttp.API_UPDATE_POST + poster.id,
            VolleyHttp.paramsUpdate(poster),
            object : VolleyHandler {
                override fun onSuccess(response: String?) {
                    Logger.d("@@@apiPosterUpdate", response!!)
                    apiPosterList()
                }

                override fun onError(error: String?) {
                    Logger.e("@@@apiPosterUpdate/error", error!!)
                    stopLoading()
                }

            })
    }

    fun dialogChooseAction(poster: Poster?) {
        val input: View =
            LayoutInflater.from(this).inflate(R.layout.choose_action_dialog, null, false)

        val tvEdit: TextView = input.findViewById(R.id.tv_edit)
        val tvDelete: TextView = input.findViewById(R.id.tv_delete)

        val optionDialog: AlertDialog = AlertDialog.Builder(this).create()
        optionDialog.setView(input)
        optionDialog.show()

        tvEdit.setOnClickListener {
            dialogPosterUpdate(poster)
            optionDialog.dismiss()
        }

        tvDelete.setOnClickListener {
            dialogPosterDelete(poster)
            optionDialog.dismiss()
        }
    }

    private fun dialogPosterDelete(poster: Poster?) {
        AlertDialog.Builder(this).setTitle("Delete Poster")
            .setMessage("Are you sure you want to delete this poster?")
            .setPositiveButton(android.R.string.yes) { dialog, which ->
                apiPosterDelete(poster!!)
            }
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun dialogPosterUpdate(poster: Poster?) {

        val input: View =
            LayoutInflater.from(this).inflate(R.layout.create_poster_dialog, null, false)

        input.findViewById<TextView>(R.id.tv_title).text = "Update Poster"

        val etTitle: EditText = input.findViewById(R.id.et_title)
        etTitle.setText(poster!!.title)

        val etBody: EditText = input.findViewById(R.id.et_body)
        etBody.setText(poster.body)

        AlertDialog.Builder(this)
            .setView(input)
            .setPositiveButton(android.R.string.yes) { _, _ ->
                poster.title = etTitle.text.toString()
                poster.body = etBody.text.toString()
                apiPosterUpdate(poster)
            }
            .setNegativeButton(android.R.string.no, null)
            .show()
    }

    private fun dialogCreatePoster() {

        val input: View =
            LayoutInflater.from(this).inflate(R.layout.create_poster_dialog, null, false)

        val etTitle: EditText = input.findViewById(R.id.et_title)
        val etBody: EditText = input.findViewById(R.id.et_body)

        AlertDialog.Builder(this)
            .setView(input)
            .setPositiveButton(android.R.string.yes) { _, _ ->
                val poster = Poster(1, 1, etTitle.text.toString(), etBody.text.toString())
                apiPosterCreate(poster)
            }
            .setNegativeButton(android.R.string.no, null)
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