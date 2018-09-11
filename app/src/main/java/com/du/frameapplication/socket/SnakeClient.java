package com.du.frameapplication.socket;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.du.frameapplication.activity.MulActivity;
import com.du.frameapplication.activity.RoomActivity;
import com.du.frameapplication.activity.SActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SnakeClient {
   private static SnakeClient snakeClient;



    public int port = 6060;
    Socket socket = null;
    static MulActivity activity1;
    static RoomActivity activity2;
    static int type = 1;

    public  static SnakeClient init(String name,Context context){
        if(context instanceof MulActivity){
            activity1 = (MulActivity) context;
            type=1;
        }else if(context instanceof RoomActivity){
            activity2 = (RoomActivity) context;
            type =2;
        }
        if (snakeClient == null) {
            snakeClient = new SnakeClient(name,context);
        }
        return snakeClient;
    }

    public static void main(String[] args) {
        new SnakeClient("",null);
    }
    public SnakeClient(String name,Context context) {
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.1.105", port);
                    Log.i("SnakeClient","创建");
                    new Cthread().start();
                    send(Constant.LOGIN+"&&"+name,context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void analysis(String msg){
        String[] m = msg.split("&&");
        if(m.length>2){
            if(Constant.LOGIN.equals(m[0])){
                if(m[1].equals("1")){
                    mHandlerSendMessage(0,"1","成功");
                }else {
                    snakeClient = null;
                    mHandlerSendMessage(0,"0",m[2]);
                }
            } else if (Constant.CREATE.equals(m[0])) {
                if(m[1].equals("1")){
                    mHandlerSendMessage(1,"1",msg);
                }else {
                    mHandlerSendMessage(99,"0",m[2]);
                }
            } else if (Constant.JION.equals(m[0])) {
                if(m[1].equals("1")){
                    mHandlerSendMessage(2,"1",msg);
                }else {
                    mHandlerSendMessage(99,"0",m[2]);
                }
            }else if (Constant.JIONADD.equals(m[0])) {
                type =2;
                if(m[1].equals("1")){
                    mHandlerSendMessage(98,"1",msg);
                }else {
                    mHandlerSendMessage(99,"0",m[2]);
                }
            }
        }
    }

    public void mHandlerSendMessage(int what,String state,String msg){
        Message message = new Message();
        message.what = what;
        Bundle bundle = new Bundle();
        bundle.putString("state",state);  //往Bundle中存放数据
        bundle.putString("msg",msg);  //往Bundle中put数据
        message.setData(bundle);//mes利用Bundle传递数据
        if(type == 1){
            activity1.mHandler.sendMessage(message);
        }else if(type == 2){
            activity2.mHandler.sendMessage(message);
        }
    }

    public void send(String msg,Context context){
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    if(context instanceof MulActivity){
                        activity1 = (MulActivity) context;
                        type=1;
                    }else if(context instanceof RoomActivity){
                        activity2 = (RoomActivity) context;
                        type =2;
                    }
                    Log.i("SnakeClient发送", msg);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                    pw.println(msg);
                    pw.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
         }).start();
    }


    class Cthread extends Thread {
        @Override
        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg1;
                while ((msg1 = br.readLine()) != null) {
                    System.out.println(msg1);
                    analysis(msg1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}
