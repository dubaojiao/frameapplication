package com.du.frameapplication.fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.du.frameapplication.R;
import com.du.frameapplication.util.HttpConnection;
import com.du.frameapplication.util.RandomCodeUtil;
import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


public class MeasureFragment extends BaseFragment {
    ListView mList;
    Button mbtn;
    List<String> datas ;
    List<String> users;
    ArrayAdapter<String> adapter;
    Handler  mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                datas.add(msg.obj.toString());
                adapter.notifyDataSetChanged();
            }
        }
    };
    @Override
    protected int getLayoutId() {
        return R.layout.measure_fragment;
    }

    @Override
    protected void initView(View view) {
        datas  = new ArrayList<>();
        users = new ArrayList<>();
        //users.add("15559709268");
        users.add("15198804804");
        users.add("15198804801");
        users.add("15198804805");
        users.add("15198804806");
        users.add("15198804807");
        users.add("15198804808");
        users.add("15198804809");
        mList = view.findViewById(R.id.mList);
        mbtn = view.findViewById(R.id.mbtn);


        adapter  = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_list_item_1, datas);
        mList.setAdapter(adapter);


        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(String phone:users){
                    SyncHttpClient client = new SyncHttpClient();//同步请求
                    new Thread(){
                        @Override
                        public void run() {
                            // 登陆
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("phone",phone);
                                jsonObject.put("loginPwd","adf00707a1c0154a9ad8edb57c8646f4");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            ByteArrayEntity entity = null;
                            try {
                                entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
                                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            client.post(getContext(),"http://192.168.1.105:7089/web/login",entity,"application/json",new  JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    try {
                                        if(response.getInt("code") == 200){
                                            s(phone+"登陆成功");
                                            for(int x = 1;x<=10000;x++){
                                                // 登陆
                                                JSONObject jsonObject2 = new JSONObject();
                                                String p = RandomCodeUtil.randomNum(11);
                                                int tr = 1000;
                                                try {
                                                    jsonObject2.put("phone", p);
                                                    jsonObject2.put("nickname","小黑鱼"+x);
                                                    jsonObject2.put("trans",tr);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                ByteArrayEntity entity = null;
                                                try {
                                                    entity = new ByteArrayEntity(jsonObject2.toString().getBytes("UTF-8"));
                                                    entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                }
                                                client.post(getContext(),"http://192.168.1.105:7089/web/addUser",entity,"application/json",new  JsonHttpResponseHandler(){
                                                    @Override
                                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                        super.onSuccess(statusCode, headers, response);
                                                        try {
                                                            if(response.getInt("code") == 200){
                                                                s(phone+"开通账号["+p+"]金额"+tr+"分成功");
                                                            }else {
                                                                s(phone+"开通账号["+p+"]金额"+tr+"分失败");
                                                                Log.i(phone,"失败");
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }
                                        }else {
                                            s(phone+"登陆失败");
                                            Log.i(phone,"登陆失败");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }.start();
                }
            }
        });
    }
    @Override
    protected void initData() {

    }

    public void s(String str){
        Message message = new Message();
        message.what = 1;
        message.obj = str;
        mHandler.sendMessage(message);
    }

}
