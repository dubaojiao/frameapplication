package com.du.frameapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.du.frameapplication.view.MovementView;
import com.du.frameapplication.view.SnakeMovementView;

public class SnakeActivity extends AppCompatActivity implements  android.view.GestureDetector.OnGestureListener{

    SnakeMovementView snakeMovementView;

    //定义手势检测器实例
    GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snakeMovementView = new SnakeMovementView(this);
        setContentView(snakeMovementView);
        //创建手势检测器
        detector = new GestureDetector(this,this);
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
            Toast.makeText(this,velocityX+"左滑",Toast.LENGTH_SHORT).show();
            if(snakeMovementView.direction != 1){
                snakeMovementView.sDirection = snakeMovementView.direction;
                snakeMovementView.direction= 2;
            }

        }else if(endX-beginX>minMove&&Math.abs(velocityX)>minVelocity){   //右滑
            Toast.makeText(this,velocityX+"右滑",Toast.LENGTH_SHORT).show();
            if(snakeMovementView.direction != 2){
                snakeMovementView.sDirection = snakeMovementView.direction;
                snakeMovementView.direction= 1;
            }
        }else if(beginY-endY>minMove&&Math.abs(velocityY)>minVelocity){   //上滑
            Toast.makeText(this,velocityX+"上滑",Toast.LENGTH_SHORT).show();
            if(snakeMovementView.direction != 4){
                snakeMovementView.sDirection = snakeMovementView.direction;
                snakeMovementView.direction= 3;
            }

        }else if(endY-beginY>minMove&&Math.abs(velocityY)>minVelocity){   //下滑
            Toast.makeText(this,velocityX+"下滑",Toast.LENGTH_SHORT).show();
            if(snakeMovementView.direction != 3){
                snakeMovementView.sDirection = snakeMovementView.direction;
                snakeMovementView.direction= 4;
            }
        }
        return false;
    }
}
