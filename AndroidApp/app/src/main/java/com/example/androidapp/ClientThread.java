package com.example.androidapp;

import android.os.Handler;
import android.os.Message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class ClientThread implements Runnable{
    public Handler handler;
    public OutputStream outputStream;
    public DataOutputStream dataOutputStream;
    private static final String host = "192.168.10.105";
    private static final int port = 8888;
    ClientThread(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        while (true){
            try {
                Socket socket = new Socket(host,port);
                InputStream in = socket.getInputStream();
                outputStream = socket.getOutputStream();
                byte[] bytes = new byte[1024];
                int length;
                while ((length = in.read(bytes)) != -1) {
                     String content = new String(bytes, 0,length, "utf-8");
                     Message message = new Message();
                     message.what = 5;
                     message.obj = content;
                     handler.sendMessage(message);
                     dataOutputStream = new DataOutputStream(outputStream);
                 }
            } catch (IOException e) {
                System.out.println("正在重连....");
                e.printStackTrace();
            }
        }
    }
}
