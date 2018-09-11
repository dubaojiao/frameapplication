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

import java.util.Arrays;

public class GluttonousMovementView extends SurfaceView implements SurfaceHolder.Callback {

    public GluttonousActivity gluttonousActivity;

    public boolean isStart;
    //当前移动方向
    public int direction;
    //上一次移动方向
    public int sDirection;
    //积分
    public int integral;

    //速度
    public long sleep;

    //初始数
    public int count;

    public boolean startUp;

    private int width;
    private int height;

    private Paint circlePaint;

    GluttonousUpdateThread updateThread;


    //开始坐标
    private int lVel;
    private int tVel;
    private int rVel;
    private int bVel;
    //一次跳跃大小
    private int step;

    private int[][] coordinate;

    private int[][] spots;

    public GluttonousMovementView(Context context) {
        super(context);
        gluttonousActivity = (GluttonousActivity) context;
        /*spots[0][0]=200;
        spots[0][1]=100;
        spots[0][2]=240;
        spots[0][3]=140;

        spots[1][0]=200;
        spots[1][1]=300;
        spots[1][2]=240;
        spots[1][3]=340;*/
    }

    public GluttonousMovementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gluttonousActivity = (GluttonousActivity) context;
        getHolder().addCallback(this);
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        direction = 1;
        sDirection = 1;
        integral = 0;
        startUp = false;
        isStart = false;
        count = 3;
        sleep = 150;

        step = 40;
        lVel = 80;
        tVel = 80;
        rVel = lVel+step;
        bVel = tVel+step;
        coordinate = new int[count][4];
        for(int x=0;x<coordinate.length;x++){
            coordinate[x][0] = lVel+(step*x);
            coordinate[x][1] = tVel;
            coordinate[x][2] = rVel+(step*x);
            coordinate[x][3] = bVel;
        }
        spots = new int[2][4];
    }

    public void updatePhysics() {
        //移动
        move();
        //验证是否边界
        checkBoundary();
        //验证是否吃到
        checkMove();
        //设置速度
        /*if((count - 5)%8 == 0  && sleep>= 150){
            sleep = sleep - (((count - 5)/8)*50);
        }*/
    }

    private void checkBoundary() {
        int t[] = coordinate[coordinate.length-1];
        switch (direction){
            case 1://右
                int zr  = t[2];
                if(zr >= width){
                    //耗时操作，完成之后发送消息给Handler，完成UI更新；
                    gluttonousActivity.mHandler.sendEmptyMessage(1);
                    this.startUp = false;
                    return;
                }
                break;
            case 2://左
                int zl  = t[0];
                if(zl <= 0){
                    //耗时操作，完成之后发送消息给Handler，完成UI更新；
                    gluttonousActivity.mHandler.sendEmptyMessage(1);
                    this.startUp = false;
                    return;
                }
                break;
            case 3://上
                int zu  = t[1];
                if(zu <= 0){
                    //耗时操作，完成之后发送消息给Handler，完成UI更新；
                    gluttonousActivity.mHandler.sendEmptyMessage(1);
                    this.startUp = false;
                    return;
                }
                break;
            case 4://下
                int zd  = t[3];
                if(zd >= height){
                    //耗时操作，完成之后发送消息给Handler，完成UI更新；
                    gluttonousActivity.mHandler.sendEmptyMessage(1);
                    this.startUp = false;
                    return;
                }
                break;
            default:return;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if(canvas == null){
            return;
        }
        canvas.drawColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.FILL);//设置填满
        circlePaint.setAntiAlias(true);// 设置画笔的锯齿效果
        //Log.i("SnakeMovementView",lVel+" "+tVel+" "+rVel+" "+bVel);
        for(int x=0;x<coordinate.length;x++){
            canvas.drawRect(coordinate[x][0],coordinate[x][1] , coordinate[x][2],coordinate[x][3] ,circlePaint);// 正方形
            Log.i("虫坐标",coordinate[x][0]+" "+coordinate[x][1]+" "+coordinate[x][2]+" "+coordinate[x][3]);
        }
        Log.i("SnakeMovementView","-------------------------");
        Log.i("SnakeMovementView","画点");
        for(int x=0;x<spots.length;x++){
            //canvas.drawRect(spots[x][0],spots[x][1] , spots[x][2],spots[x][3] ,circlePaint);// 正方形
            RectF oval3 = new RectF(spots[x][0],spots[x][1] , spots[x][2],spots[x][3] );// 设置个新的长方形
            canvas.drawRoundRect(oval3, 20, 20, circlePaint);//第二个参数是x半径，第三个参数是y半径
            Log.i("画点坐标",spots[x][0]+" "+spots[x][1]+" "+spots[x][2]+" "+spots[x][3]);
        }
    }

    private void move(){
        int[] velMax = coordinate[coordinate.length-1];
        int[] vel = new int[4];
        switch (direction){
            case 1://右
                vel[0] = velMax[0]+step;
                vel[1] = velMax[1];
                vel[2] = velMax[2]+step;
                vel[3] = velMax[3];
                break;
            case 2://左
                vel[0] = velMax[0]-step;
                vel[1] = velMax[1];
                vel[2] = velMax[2]-step;
                vel[3] = velMax[3];
                break;
            case 3://上
                vel[0] = velMax[0];
                vel[1] = velMax[1]-step;
                vel[2] = velMax[2];
                vel[3] = velMax[3]-step;
                break;
            case 4://下
                vel[0] = velMax[0];
                vel[1] = velMax[1]+step;
                vel[2] = velMax[2];
                vel[3] = velMax[3]+step;
                break;
            default:return;
        }

        for(int x=0;x<coordinate.length-1;x++){
            coordinate[x] = coordinate[x+1];
        }
        coordinate[coordinate.length-1] = vel;
    }

    private void checkMove(){
        Log.i("移动检测","检测");
        int t[] = coordinate[coordinate.length-1];
        for(int x=0;x<spots.length;x++){
            int[] s = spots[x];
            boolean isZ = false;
            switch (direction){
                case 1://右
                    int zr  = t[2];
                    if(s[0] <= zr && zr <= s[2] && s[1] == t[1] && s[3] == t[3]){
                        isZ  = true;
                        break;
                    }
                    break;
                case 2://左
                    int zl  = t[0];
                    if(s[0] <= zl && zl <= s[2] && s[1] == t[1] && s[3] == t[3]){
                        isZ  = true;
                        break;
                    }
                    break;
                case 3://上
                    int zt  = t[1];
                    if(s[1] <= zt && zt <= s[3]  && s[0] == t[0] && s[2] == t[2]){
                        isZ  = true;
                        break;
                    }
                    break;
                case 4://下
                    int zd  = t[3];
                    if(s[1] <= zd && zd <= s[3] && s[0] == t[0] && s[2] == t[2]){
                        isZ  = true;
                        break;
                    }
                    break;
                default:return;
            }
            if(isZ){
                Log.i("画点坐标","碰撞");
                addSnake();
                spots[x] = randomArr();
            }
        }
    }

    private int[] add(int[] velMax){
        int[] vel = new int[4];
        switch (direction){
            case 1://右
                vel[0] = velMax[0]+step;
                vel[1] = velMax[1];
                vel[2] = velMax[2]+step;
                vel[3] = velMax[3];
                break;
            case 2://左
                vel[0] = velMax[0]-step;
                vel[1] = velMax[1];
                vel[2] = velMax[2]-step;
                vel[3] = velMax[3];
                break;
            case 3://上
                vel[0] = velMax[0];
                vel[1] = velMax[1]-step;
                vel[2] = velMax[2];
                vel[3] = velMax[3]-step;
                break;
            case 4://下
                vel[0] = velMax[0];
                vel[1] = velMax[1]+step;
                vel[2] = velMax[2];
                vel[3] = velMax[3]+step;
                break;
            default:return null;
        }
        return  vel;
    }


    private void addSnake(){
        integral ++;
        int[] t = coordinate[coordinate.length-1];
        count ++;
        coordinate = Arrays.copyOf(coordinate,count);
        coordinate[coordinate.length-1] = add(t);

        //耗时操作，完成之后发送消息给Handler，完成UI更新；
        gluttonousActivity.mHandler.sendEmptyMessage(0);

        //需要数据传递，用下面方法；
        //Message msg =new Message();
        //msg.obj = "数据";//可以是基本类型，可以是对象，可以是List、map等；
        //mHandler.sendMessage(msg);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Rect surfaceFrame = holder.getSurfaceFrame();
        width = surfaceFrame.width();
        height = surfaceFrame.height();

        //设置球的初始位置。
        spots[0] = randomArr();
        spots[1] = randomArr();

        Log.i("画布","surfaceCreated");

        updateThread = new GluttonousUpdateThread(this);
        updateThread.setRunning(true);
        updateThread.start();
    }

    private int[] randomArr(){
        int x = randomX();
        int y = randomY(x);
        return new int[]{x,y,x+step,y+step};

    }

    private int randomX(){
        while (true){
            int x = (int)(Math.random()*(width-step));
            if(x%step == 0 ){
                return x;
            }
        }
    }

    private int randomY(int x){
        while (true){
            int y = (int)(Math.random()*(height-step));
            if(y%step == 0 && y>=step && y != x){
                return y;
            }
        }
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
