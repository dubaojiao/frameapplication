package com.du.frameapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.du.frameapplication.activity.GluttonousActivity;
import com.du.frameapplication.activity.RoomActivity;

import java.util.Arrays;

public class RoomMovementView extends SurfaceView implements SurfaceHolder.Callback {

    private Paint circlePaint1;
    private Paint circlePaint2;
    private Paint circlePaint3;

    public int width;
    public int height;

    public int[][] coordinate1;
    public int[][] coordinate2;
    public int[][] spots;

    public RoomMovementView(Context context) {
        super(context);
        getHolder().addCallback(this);
        circlePaint1 = new Paint();
        circlePaint1.setColor(Color.BLUE);
        circlePaint2 = new Paint();
        circlePaint2.setColor(Color.YELLOW);
        circlePaint3 = new Paint();
        circlePaint3.setColor(Color.RED);
    }

    public RoomMovementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        circlePaint1 = new Paint();
        circlePaint1.setColor(Color.BLUE);
        circlePaint2 = new Paint();
        circlePaint2.setColor(Color.YELLOW);
        circlePaint3 = new Paint();
        circlePaint3.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(canvas == null){
            return;
        }
        canvas.drawColor(Color.WHITE);
        circlePaint1.setStyle(Paint.Style.FILL);//设置填满
        circlePaint1.setAntiAlias(true);// 设置画笔的锯齿效果
        //Log.i("SnakeMovementView",lVel+" "+tVel+" "+rVel+" "+bVel);
        for(int x=0;x<coordinate1.length;x++){
            canvas.drawRect(coordinate1[x][0],coordinate1[x][1] , coordinate1[x][2],coordinate1[x][3] ,circlePaint1);// 正方形
            Log.i("虫1坐标",coordinate1[x][0]+" "+coordinate1[x][1]+" "+coordinate1[x][2]+" "+coordinate1[x][3]);
        }
        Log.i("SnakeMovementView","-------------------------");
        for(int x=0;x<coordinate2.length;x++){
            canvas.drawRect(coordinate2[x][0],coordinate2[x][1] , coordinate2[x][2],coordinate2[x][3] ,circlePaint2);// 正方形
            Log.i("虫2坐标",coordinate2[x][0]+" "+coordinate2[x][1]+" "+coordinate2[x][2]+" "+coordinate2[x][3]);
        }
        Log.i("SnakeMovementView","-------------------------");
        Log.i("SnakeMovementView","画点");
        for(int x=0;x<spots.length;x++){
            //canvas.drawRect(spots[x][0],spots[x][1] , spots[x][2],spots[x][3] ,circlePaint);// 正方形
            RectF oval3 = new RectF(spots[x][0],spots[x][1] , spots[x][2],spots[x][3] );// 设置个新的长方形
            canvas.drawRoundRect(oval3, 20, 20, circlePaint3);//第二个参数是x半径，第三个参数是y半径
            Log.i("画点坐标",spots[x][0]+" "+spots[x][1]+" "+spots[x][2]+" "+spots[x][3]);
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Rect surfaceFrame = holder.getSurfaceFrame();
        width = surfaceFrame.width();
        height = surfaceFrame.height();

        Log.i("画布","surfaceCreated");
    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
