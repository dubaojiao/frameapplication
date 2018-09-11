package com.du.frameapplication.view;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class RoomUpdateThread extends Thread {
    private long time;
    private final int fps = 2;
    private boolean toRun = true;
    private boolean toRunContent = true;
    private RoomMovementView roomMovementView;
    private SurfaceHolder surfaceHolder;

    public RoomUpdateThread(RoomMovementView rMovementView) {
        roomMovementView = rMovementView;
        surfaceHolder = roomMovementView.getHolder();
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
                    roomMovementView.onDraw(c);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
}