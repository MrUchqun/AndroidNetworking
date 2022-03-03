package com.example.androidnetworking.network.volley

interface VolleyHandler {
    fun onSuccess(response: String?)
    fun onError(error: String?)
}