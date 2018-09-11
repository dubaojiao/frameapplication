package com.du.frameapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.du.frameapplication.GuideActivity;
import com.du.frameapplication.MainActivity;
import com.du.frameapplication.R;
import com.du.frameapplication.SplseActivity;
import com.du.frameapplication.socket.Constant;
import com.du.frameapplication.socket.SnakeClient;
import com.du.frameapplication.view.MsgDialog;
import com.du.frameapplication.view.RoomMovementView;

import java.util.Timer;
import java.util.TimerTask;

public class RoomActivity extends SActivity implements  GestureDetector.OnGestureListener ,View.OnClickListener{
    // 动画View
    RoomMovementView movementView;

    //定义手势检测器实例
    GestureDetector detector;
    //界面内容
    TextView jf_text;
    TextView cs_text;
    TextView sd_text;
    Button stop_btn;
    Button tz_btn;
    Button ks_btn;
    //消息弹框
    MsgDialog msgDialog;

    // 客户端
    SnakeClient snakeClient;

    int sDirection;
    int direction;

  public  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    initView();
                    break;
                case 1:
                    showExitDialog();
                    break;
                case 2:
                    finishActivity();
                    break;
                case 98:
                    String[] m = msg.getData().getString("msg").split("&&");
                    if(m.length == 4){
                        startTime(m[3]);
                    }else {
                        showError("参数解析失败");
                    }
                    break;
                case 99:
                    showError(msg.getData().getString("msg"));
                    break;
                default:
                    break;
            }
        }

    };
    public void showError(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        movementView = findViewById(R.id.g_View);
        jf_text = (TextView)findViewById(R.id.jf_text);
        cs_text = (TextView)findViewById(R.id.cs_text);
        sd_text = (TextView)findViewById(R.id.sd_text);

        stop_btn = (Button)findViewById(R.id.stop_btn);
        stop_btn.setOnClickListener(this);
        tz_btn = (Button)findViewById(R.id.tz_btn);
        tz_btn.setOnClickListener(this);
        ks_btn = (Button)findViewById(R.id.ks_btn);
        ks_btn.setOnClickListener(this);

        initView();



        //创建手势检测器
        detector = new GestureDetector(this,this);

        Intent intent = getIntent();
       int type = intent.getIntExtra("type",1);
       String one = intent.getStringExtra("one");
       String tow = intent.getStringExtra("tow");
       String nice = intent.getStringExtra("nice");

        snakeClient = SnakeClient.init(nice,this);
        msgDialog = new MsgDialog(this);
        msgDialog.show();
        msgDialog.wTv1.setText(one);
        if(type == 1){
            //创建
            msgDialog.wTv2.setText("");
            msgDialog.timeTv.setText("");
            String msg = Constant.WH+"&&"+movementView.width+"&&"+movementView.height;
            snakeClient.send(msg,this);
        }else if(type == 2){
            startTime(tow);
        }

    }
    public void startTime(String tow){
        msgDialog.wTv2.setText(tow);
        msgDialog.timeTv.setText(10+"S");
    }

    public void finishActivity(){
        Intent intent=new Intent(this, RoomActivity.class);
        startActivity(intent);
        finish();//关闭自己
        overridePendingTransition(0, 0);
    }

    public void initView(){


    }

    //将该activity上的触碰事件交给GestureDetector处理
    public boolean onTouchEvent(MotionEvent me){
        return detector.onTouchEvent(me);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float minMove = 120;         //最小滑动距离
        float minVelocity = 0;      //最小滑动速度
        float beginX = e1.getX();
        float endX = e2.getX();
        float beginY = e1.getY();
        float endY = e2.getY();

        if(beginX-endX>minMove&&Math.abs(velocityX)>minVelocity){   //左滑
            //Toast.makeText(this,velocityX+"左滑",Toast.LENGTH_SHORT).show();
            if(direction != 1){
                sDirection = direction;
                direction= 2;
            }

        }else if(endX-beginX>minMove&&Math.abs(velocityX)>minVelocity){   //右滑
            //Toast.makeText(this,velocityX+"右滑",Toast.LENGTH_SHORT).show();
            if(direction != 2){
                sDirection = direction;
                direction= 1;
            }
        }else if(beginY-endY>minMove&&Math.abs(velocityY)>minVelocity){   //上滑
            //Toast.makeText(this,velocityX+"上滑",Toast.LENGTH_SHORT).show();
            if(direction != 4){
                sDirection = direction;
                direction= 3;
            }

        }else if(endY-beginY>minMove&&Math.abs(velocityY)>minVelocity){   //下滑
           // Toast.makeText(this,velocityX+"下滑",Toast.LENGTH_SHORT).show();
            if(direction != 3){
                sDirection = direction;
                direction= 4;
            }
        }
        return false;
    }

    // 带“是”和“否”的提示框
    public void showExitDialog(){
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("游戏结果是否重新开始")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        mHandler.sendEmptyMessage(2);
                    }
                })
                .setNegativeButton("否", null)
                .show();
    }



    @Override
    public void onClick(View v) {
        /*int id = v.getId();
        if(id == R.id.stop_btn){
            if(movementView.isStart){
                if(movementView.startUp){
                    movementView.startUp = false;
                    stop_btn.setText("继续");
                }else {
                    movementView.startUp = true;
                    stop_btn.setText("暂停");
                }
            }else {
                Toast.makeText(this,"请点击开始游戏",Toast.LENGTH_SHORT).show();
            }
        }
        if(id == R.id.ks_btn){
            movementView.isStart = true;
            movementView.startUp = true;
            ks_btn.setEnabled(false);
        }
        if (id == R.id.tz_btn){
            Toast.makeText(this,"待开发",Toast.LENGTH_SHORT).show();
        }*/
    }
}
