package com.lofts.dictionary.utils;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * PACKAGE_NAME：com.lofts.dictionary.utils
 * DATE：2018/6/27 15:31
 * USER: xiantao.jiang
 * DESCRIBE: OKHttp封装类
 */

public class RequestManager {

    //mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    //mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    private static final String TAG = RequestManager.class.getSimpleName();

    //请求接口根地址
    private static final String BASE_URL = "http://xxx.com/openapi";

    private static volatile RequestManager mInstance;

    //get请求
    public static final int TYPE_GET = 0;

    //post请求参数为表单
    public static final int TYPE_POST = 1;

    //okHttpClient实例
    private OkHttpClient mOkHttpClient;

    //全局处理子线程和M主线程通信
    private Handler okHttpHandler;


    public RequestManager(Context context) {
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        okHttpHandler = new Handler(context.getMainLooper());
    }


    public static RequestManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (RequestManager.class) {
                if (mInstance == null) {
                    mInstance = new RequestManager(context.getApplicationContext());
                }
            }
        }

        return mInstance;
    }


    /**
     * okHttp异步请求统一入口
     *
     * @param actionUrl   接口地址
     * @param requestType 请求类型
     * @param paramsMap   请求参数
     * @param callBack    请求返回数据回调
     * @param <T>         数据泛型
     **/
    public <T> Call requestAsyn(String actionUrl, int requestType, HashMap<String, String> paramsMap, ReqCallBack callBack) {
        Call call = null;
        switch (requestType) {
            case TYPE_GET:
                call = requestGetByAsyn(actionUrl, paramsMap, callBack);
                break;
            case TYPE_POST:
                call = requestPostByAsyn(actionUrl, paramsMap, callBack);
                break;
            default:
                break;
        }
        return call;
    }

    private Call requestGetByAsyn(String actionUrl, HashMap<String, String> paramsMap, final ReqCallBack callBack) {
        StringBuilder builder = new StringBuilder();

        int pos = 0;
        for (String key : paramsMap.keySet()) {
            if (pos > 0) {
                builder.append("&");
            }
            builder.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key))));
            pos++;
        }

        String requestUrl = String.format("%s?%s", actionUrl, builder.toString());
        final Request request = addHeaders().url(requestUrl).build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                failedCallBack("查询错误", callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    successCallBack(response.body().string(), callBack);
                } else {
                    failedCallBack("查询错误", callBack);
                }
            }
        });
        return call;

    }

    private Call requestPostByAsyn(String actionUrl, HashMap<String, String> paramsMap, final ReqCallBack callBack) {

        FormBody.Builder formBody = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            formBody.add(key, paramsMap.get(key));
        }

        final Request request = addHeaders().url(actionUrl).post(formBody.build()).build();
        final Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                failedCallBack("查询错误", callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    successCallBack(response.body().string(), callBack);
                } else {
                    failedCallBack("查询错误", callBack);
                }
            }
        });
        return call;

    }


    /**
     * 统一为请求添加头信息
     *
     * @return
     */
    private Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("platform", "2")
                .addHeader("phoneModel", Build.MODEL)
                .addHeader("systemVersion", Build.VERSION.RELEASE)
                .addHeader("appVersion", "3.2.0");
        return builder;
    }

    /**
     * 统一同意处理成功信息
     *
     * @param result
     * @param callBack
     */
    private void successCallBack(final Object result, final ReqCallBack callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onReqSuccess(result);
                }
            }
        });
    }

    /**
     * 统一处理失败信息
     *
     * @param errorMsg
     * @param callBack
     */
    private void failedCallBack(final String errorMsg, final ReqCallBack callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onReqFailed(errorMsg);
                }
            }
        });
    }

    public interface ReqCallBack {
        /**
         * 响应成功
         */
        void onReqSuccess(Object result);

        /**
         * 响应失败
         */
        void onReqFailed(String errorMsg);
    }
}
