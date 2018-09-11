package com.du.frameapplication.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;


import com.du.frameapplication.R;
import com.du.frameapplication.socket.Constant;
import com.du.frameapplication.socket.SnakeClient;
import com.du.frameapplication.view.InputDialog;

public class MulActivity extends SActivity  implements View.OnClickListener{
    //界面内容
    AutoCompleteTextView niceNameTv;
    Button niceNameBtn;
    Button cBtn;
    Button jBtn;
    //弹框内容
    Button qBtn;
    Button close;
    AutoCompleteTextView pwd;
    //1 创建 2  加入
    int type = 1;
    ///弹框
    InputDialog inputDialog;
    //是否登录
    boolean  flag;
    // 客户端
    SnakeClient snakeClient;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //登陆的响应
                    loginReturn(msg.getData().getString("state"),msg.getData().getString("msg"));
                    break;
                case 1:
                    createGame(msg.getData().getString("state"),msg.getData().getString("msg"));
                    break;
                case 2:
                    joinGame(msg.getData().getString("state"),msg.getData().getString("msg"));
                    break;
                case 99:

                    showError(msg.getData().getString("msg"));
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mul);
        flag = false;
        initView();
    }

    public void showError(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        niceNameTv = (AutoCompleteTextView)findViewById(R.id.niceNameTv);
        niceNameBtn = (Button)findViewById(R.id.niceNameBtn);
        niceNameBtn.setOnClickListener(this);
        cBtn = (Button)findViewById(R.id.cBtn);
        cBtn.setOnClickListener(this);
        jBtn = (Button)findViewById(R.id.jBtn);
        jBtn.setOnClickListener(this);
        cBtn.setEnabled(false);
        jBtn.setEnabled(false);
    }

    public void loginReturn(String state,String msg){
        if(state.equals("1")){
            niceNameBtn.setEnabled(false);
            niceNameTv.setEnabled(false);
            flag = true;
            cBtn.setEnabled(true);
            jBtn.setEnabled(true);
            Toast.makeText(this,"登陆成功，可选择加入房间或者新建房间",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"登陆登陆失败,请重试"+msg,Toast.LENGTH_SHORT).show();
            niceNameBtn.setEnabled(true);
            niceNameTv.setEnabled(true);
            cBtn.setEnabled(false);
            jBtn.setEnabled(false);
            flag = false;
            snakeClient = null;
        }
    }

    public void createGame(String state,String msg){
        Toast.makeText(this,state +"-------"+msg,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, RoomActivity.class);
        String[] ms = msg.split("&&");
        if (ms.length == 4) {
            intent.putExtra("type",1);
            intent.putExtra("one",ms[3]);
            intent.putExtra("nice",niceNameTv.getText().toString());
            startActivity(intent);
        }else {
            showError("创建结果解析失败");
        }
    }

    public void joinGame(String state,String msg){
        Toast.makeText(this,state +"-------"+msg,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, RoomActivity.class);
        String[] ms = msg.split("&&");
        if (ms.length == 5) {
            intent.putExtra("type",2);
            intent.putExtra("one",ms[3]);
            intent.putExtra("tow",ms[4]);
            intent.putExtra("nice",niceNameTv.getText().toString());
            startActivity(intent);
        }else {
            showError("加入结果解析失败");
        }
    }

    @Override
    public void onClick(View v) {
        int id= v.getId();
        switch (id){
            case R.id.niceNameBtn:
                if( niceNameTv.getText() == null ||niceNameTv.getText().toString().equals("")){
                    Toast.makeText(this,"请输入昵称",Toast.LENGTH_SHORT).show();
                    return;
                }
                snakeClient = SnakeClient.init(niceNameTv.getText().toString(),this);
                niceNameBtn.setEnabled(false);
                niceNameTv.setEnabled(false);
                break;
            case R.id.cBtn:
                if(flag){
                     inputDialog = new InputDialog(this);
                    inputDialog.show();
                    qBtn = inputDialog.qBtn;
                    close = inputDialog.close;
                    pwd = inputDialog.pwd;
                    qBtn.setOnClickListener(this);
                    close.setOnClickListener(this);
                    type = 1;
                }else {
                    Toast.makeText(this,"请输入昵称并确定",Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.jBtn:
                if(flag){
                     inputDialog = new InputDialog(this);
                    inputDialog.show();
                    qBtn = inputDialog.qBtn;
                    close = inputDialog.close;
                    pwd = inputDialog.pwd;
                    qBtn.setOnClickListener(this);
                    close.setOnClickListener(this);
                    type = 2;
                }else {
                    Toast.makeText(this,"请输入昵称并确定",Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.qBtn:
                String p = pwd.getText().toString();
                if(p == null || p.equals("")){
                    Toast.makeText(this,"请输入房间密码",Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(this,"点击了确认",Toast.LENGTH_SHORT).show();
                    if(type == 1){
                        snakeClient.send(Constant.CREATE+"&&"+p,this);
                    }else {
                        snakeClient.send(Constant.JION+"&&"+p,this);
                    }
                    cBtn.setEnabled(false);
                    jBtn.setEnabled(false);
                }
                break;
            case R.id.close:
                inputDialog.dismiss();
                break;
            default:return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(snakeClient != null){
            snakeClient.send(Constant.DESTROY+"&&DESTROY",this);
        }
        snakeClient= null;
    }
}
