package com.example.androidnetworking.utils

import android.util.Log
import com.example.androidnetworking.network.volley.VolleyHttp.Companion.IS_TESTER

class Logger {

    companion object {
        fun d(tag: String, msg: String) {
            if (IS_TESTER) Log.d(tag, msg)
        }

        fun i(tag: String, msg: String) {
            if (IS_TESTER) Log.i(tag, msg)
        }

        fun v(tag: String, msg: String) {
            if (IS_TESTER) Log.v(tag, msg)
        }

        fun e(tag: String, msg: String) {
            if (IS_TESTER) Log.e(tag, msg)
        }
    }

}