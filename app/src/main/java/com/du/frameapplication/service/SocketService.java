package com.du.frameapplication.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.du.frameapplication.R;
import com.du.frameapplication.activity.ConfirmActivity;

import java.util.Random;

import io.crossbar.autobahn.websocket.WebSocketConnection;
import io.crossbar.autobahn.websocket.WebSocketConnectionHandler;
import io.crossbar.autobahn.websocket.exceptions.WebSocketException;

/**
 * 第一个方法onCreate只有在Service被创建的时刻被调用。如果Service已经在运行中，这个方法将不会被调用。我们不能直接调用它，它是由系统负责调用的。

 OnStartCommand方法是最重要的方法，因为它在我们需要启动Service的时候被调用。在这个方法中，我们拥有在运行Service时传递进来的Intent，
 这样就可以与Service交换一些信息。在这个方法中，我们实现自己的逻辑：如果不是耗时的操作可以直接在这个方法中执行，
 否则可以创建一个线程。正如你看到的那样，这个方法需要返回一个整型值。这个整型代表系统应该怎么样处理这个Service：

 START_STICKY：使用这个返回值，如果系统杀死我们的Service将会重新创建。但是，发送给Service的Intent不会再投递。这样Service是一直运行的。
 START_NOT_STICKY：如果系统杀死了Service，不会重新创建，除非客户端显式地调用了onStart命令。
 START_REDELIVER_INTENT：功能与START_STICKY类似。另外，在这种情况下Intent会重新传递给Service。
 OnDestory是在Service将被销毁时系统调用的方法。

 一旦有了自定义的Service类，就要在Manifest.xml中声明，这样我们就可以使用了。
 */
public class SocketService extends Service {
    private static final String wsurl = "ws://192.168.1.105:9901/websocket/";
    private static final String TAG = "BackstageService";
    private WebSocketConnection mConnect = new WebSocketConnection();
    private String ANDROID_ID = "15559709268";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, intent.getStringExtra("name"));
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.connect();
    }

    /**
     * websocket连接，接收服务器消息
     */
    private void connect() {
        Log.i(TAG, "ws connect....");
        String url = wsurl + ANDROID_ID;
        Log.i(TAG, url);
        try {
            mConnect.connect(url, new WebSocketConnectionHandler() {
                @Override
                public void onOpen() {
                    Log.i(TAG, "Status:Connect to " + wsurl);
                    //sendUsername();
                }

                @Override
                public void onMessage(String payload) {
                    Log.i(TAG, payload);
                    String[] msg = payload.split("C&&A&&&Ac");
                    if(msg.length == 3){
                        if(msg[0].equals("login")){
                            sendNotification("登陆确认");
                            showActivity(msg);
                        }
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.i(TAG, "Connection lost..");
                    Log.i(TAG, code + "");
                    Log.i(TAG, reason);
                }
            });
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    private void showActivity(String[] payload) {
        //登陆 接口
        Intent intent = new Intent(getApplicationContext(), ConfirmActivity.class);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("url", payload[1]);
        bundle.putString("key", payload[2]);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        mConnect.sendClose();
        stopForeground(true);
        Intent intent = new Intent("com.dbjtech.waiqin.destroy");
        sendBroadcast(intent);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }
    @SuppressLint("NewApi")
    public void sendNotification(CharSequence text) { //  .setContentIntent(pendingIntent)
        Random random = new Random();
        int index = random.nextInt(10000000); // 用mNotificationManager的notify方法通知用户生成标题栏消息通知
        if (index == 0) {
            index = 1;
        }
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(index+"", TAG, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(getApplicationContext(), index+"")
                    .setContentTitle("通知")
                    .setContentText(text)
                    .setSmallIcon(android.R.drawable.stat_notify_more)
                    .setAutoCancel(true).build();
            notificationManager.notify(index, notification);
        }else {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(SocketService.this)
                    .setAutoCancel(true)
                    .setContentTitle("title")
                    .setContentText(text)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setOngoing(true);
            Notification notification = builder.build();
            notificationManager.notify(index, notification); // 通过通知管理器发送通知
        }
    }


}