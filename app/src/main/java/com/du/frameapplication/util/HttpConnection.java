package com.du.frameapplication.util;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by hx on 2017-1-11.
 */

public class HttpConnection {
    //public final static String BASE_URL = "http://192.168.1.105:9002/";
    public static AsyncHttpClient client = new AsyncHttpClient();//异步请求


    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context context,String url, JSONObject jsonObject, AsyncHttpResponseHandler responseHandler) {
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(context,getAbsoluteUrl(url), entity, "application/json",responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return  relativeUrl;
    }

    public static void setConnectionTimeout(int mm){
        client.setConnectTimeout(mm);
    }
}
