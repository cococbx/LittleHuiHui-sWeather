package com.cbx.littlehuihuisweather.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * okhttp
 * Created by lenovo on 2017/10/19.
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String adress, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(adress).build();
        client.newCall(request).enqueue(callback);
    }

}
