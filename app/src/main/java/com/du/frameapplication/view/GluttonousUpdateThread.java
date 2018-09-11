package com.du.frameapplication.view;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GluttonousUpdateThread extends Thread {
    private long time;
    private final int fps = 2;
    private boolean toRun = true;
    private boolean toRunContent = true;
    private GluttonousMovementView snakeMovementView;
    private SurfaceHolder surfaceHolder;

    public GluttonousUpdateThread(GluttonousMovementView rMovementView) {
        snakeMovementView = rMovementView;
        surfaceHolder = snakeMovementView.getHolder();
    }

    public void setRunning(boolean run) {
        toRun = run;
        toRunContent = run;
    }

    public void run() {
        Canvas c;
        while (toRun) {
            if(toRunContent){
                long cTime = System.currentTimeMillis();

                //if ((cTime - time) <= (1000 / fps)) {

                c = null;
                try {

                    c = surfaceHolder.lockCanvas(null);
                    snakeMovementView.updatePhysics();
                    snakeMovementView.onDraw(c);
                    Thread.sleep(snakeMovementView.sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
            //}
            //time = cTime;
            //toRun = false;
            toRunContent =  snakeMovementView.startUp && snakeMovementView.isStart;
        }
    }
}