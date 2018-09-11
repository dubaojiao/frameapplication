package com.du.frameapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MovementView extends SurfaceView implements SurfaceHolder.Callback {

    private int xPos;
    private int yPos;

    private int xVel;
    private int yVel;

    private int width;
    private int height;

    private int circleRadius;
    private Paint circlePaint;

    UpdateThread updateThread;


    public MovementView(Context context) {
        super(context);
        getHolder().addCallback(this);

        circleRadius = 10;
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);

        xVel = 2;
        yVel = 2;
    }

    public void updatePhysics() {

        //更新当前的x,y坐标
        xPos += xVel;
        yPos += yVel;

        if (yPos - circleRadius < 0 || yPos + circleRadius > height) {


            if (yPos - circleRadius < 0) {

                //如果小球到达画布区域的上顶端，则弹回

                yPos = circleRadius;
            }else{

                //如果小球到达了画布的下端边界，则弹回

                yPos = height - circleRadius;
            }

            // 将Y坐标设置为相反方向
            yVel *= -1;
        }
        if (xPos - circleRadius < 0 || xPos + circleRadius > width) {


            if (xPos - circleRadius < 0) {

                // 如果小球到达左边缘

                xPos = circleRadius;
            } else {

                // 如果小球到达右边缘

                xPos = width - circleRadius;
            }

            // 重新设置x轴坐标
            xVel *= -1;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(canvas == null){
            return;
        }
        canvas.drawColor(Color.WHITE);

        canvas.drawCircle(xPos, yPos, circleRadius, circlePaint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Rect surfaceFrame = holder.getSurfaceFrame();
        width = surfaceFrame.width();
        height = surfaceFrame.height();

        //设置球的初始位置。
        xPos = circleRadius;
        yPos = circleRadius;
        updateThread = new UpdateThread(this);
        updateThread.setRunning(true);
        updateThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        updateThread.setRunning(false);
        while (retry) {
            try {
                updateThread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }
}
