package com.du.frameapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.du.frameapplication.GuideActivity;
import com.du.frameapplication.MainActivity;
import com.du.frameapplication.R;
import com.du.frameapplication.util.HttpConnection;
import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class ConfirmActivity extends Activity implements View.OnClickListener{
    ImageView loginImg;
    Button loginBtn;
    String url;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_layout);
        loginImg = findViewById(R.id.login_img);
        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);
        this.setTitle("Log");
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
         url = bundle.getString("url");
         key = bundle.getString("key");
        Log.i("url",url);
        Log.i("key",key);


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.login_btn){
            LoadingBar.make(loginBtn).show();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("key",1122334455);
                jsonObject.put("target",key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HttpConnection.post(getApplicationContext(),url,jsonObject,new  JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    LoadingBar.cancel(loginBtn);
                    try {
                        if("成功".equals(response.getString("data"))){
                            Toast.makeText(getApplicationContext(),"操作成功" ,Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),"服务器请求失败" ,Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
