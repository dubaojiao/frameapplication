package com.du.frameapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.du.frameapplication.view.MovementView;

public class PelletActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MovementView(this));


    }


}
