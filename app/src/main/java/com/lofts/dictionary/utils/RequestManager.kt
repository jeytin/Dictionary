package com.lofts.dictionary.utils

import android.content.Context
import android.os.Build
import android.os.Handler
import okhttp3.*
import java.io.IOException
import java.net.URLEncoder
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * PACKAGE_NAME：com.lofts.dictionary.utils
 * DATE：2018-12-18 23:26
 * USER: asus
 * DESCRIBE:
 */
class RequestManager private constructor(context: Context) {

    var mOkHttpClient: OkHttpClient? = null
    var okHttpHandler: Handler? = null

    companion object {
        val TYPE_GET: Int = 0;
        val TYPE_POST: Int = 1;

        @Volatile
        private var instance: RequestManager? = null

        fun getInstance(context: Context): RequestManager {
            if (instance == null) {
                instance = RequestManager(context.applicationContext)
            }

            return instance!!
        }
    }

    init {
        mOkHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        okHttpHandler = Handler(context.mainLooper)
    }

    fun <T> requestAsyn(
        actionUrl: String,
        requestType: Int,
        paramsMap: HashMap<String, String>,
        callBack: ReqCallBack
    ): Call? {
        var call: Call? = null
        when (requestType) {
            TYPE_GET -> call = requestGetByAsyn(actionUrl, paramsMap, callBack)
            TYPE_POST -> call = requestPostByAsyn(actionUrl, paramsMap, callBack)
            else -> {
                callBack.onReqFailed("请求错误")
            }
        }

        return call
    }

    private fun requestGetByAsyn(actionUrl: String, paramsMap: HashMap<String, String>, callBack: ReqCallBack): Call {
        var builder = StringBuilder();
        var pos = 0
        for (key in paramsMap.keys) {
            if (pos > 0) {
                builder.append("&")
            }
            builder.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key))))
            pos++
        }
        var requestUrl = String.format("%s?%s", actionUrl, builder.toString())
        var request = addHeaders().url(requestUrl).build()
        var call = mOkHttpClient?.newCall(request)
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                failedCallBack("查询错误", callBack)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    successCallBack(response.body()!!.string(), callBack)
                } else {
                    failedCallBack("查询错误", callBack)
                }
            }
        })

        return call!!
    }

    private fun requestPostByAsyn(actionUrl: String, paramsMap: HashMap<String, String>, callBack: ReqCallBack): Call {
        var formBody = FormBody.Builder()
        for (key in paramsMap.keys) {
            formBody.add(key, paramsMap.get(key))
        }
        var request = addHeaders().url(actionUrl).post(formBody.build()).build()
        var call = mOkHttpClient?.newCall(request)
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                failedCallBack("查询错误", callBack)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    successCallBack(response.body()!!.string(), callBack)
                } else {
                    failedCallBack("查询错误", callBack)
                }
            }
        })

        return call!!
    }

    private fun addHeaders(): Request.Builder {
        var builder = Request.Builder()
            .addHeader("Connection", "keep-alive")
            .addHeader("platform", "2")
            .addHeader("phoneModel", Build.MODEL)
            .addHeader("systemVersion", Build.VERSION.RELEASE)
            .addHeader("appVersion", "3.2.0")
        return builder
    }

    private fun failedCallBack(msg: String, callback: ReqCallBack) {
        okHttpHandler?.post { callback.onReqFailed(msg) }
    }

    private fun successCallBack(result: Any, callback: ReqCallBack) {
        okHttpHandler?.post { callback.onReqSuccess(result) }
    }

    interface ReqCallBack {
        fun onReqSuccess(result: Any)
        fun onReqFailed(errorMsg: String)
    }

}