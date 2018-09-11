package com.du.frameapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

public class SplseActivity extends Activity {
    LinearLayout firstLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        firstLayout = findViewById(R.id.first_layout);
        firstLayout.setBackgroundResource(R.mipmap.guide);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("guide",MODE_PRIVATE);
                boolean isfirst = preferences.getBoolean("isfirst",true);
                if(isfirst){
                    //是第一次启动
                    startActivity(new Intent(SplseActivity.this, GuideActivity.class));
                    SharedPreferences.Editor editor =  getSharedPreferences("guide",MODE_PRIVATE).edit();
                    editor.putBoolean("isfirst",false);
                    editor.commit();
                    finish();
                }else {
                    //不是第一次启动
                    startActivity(new Intent(SplseActivity.this,MainActivity.class));
                    finish();
                }
            }
        },1500);
    }
}
