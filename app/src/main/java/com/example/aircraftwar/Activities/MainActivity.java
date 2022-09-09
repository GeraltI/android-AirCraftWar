package com.example.aircraftwar.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aircraftwar.R;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String ipAddress="10.250.131.200";

    public static Socket socket;
    public static PrintWriter writer;

    private static boolean start = false;
    private long exitTime = 0;

    Button LoginButton;
    Button EnrollButton;

    //活动创建时执行
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//隐藏任务栏
        setContentView(R.layout.activity_main);
        ExitApplication.getInstance().addActivity(MainActivity.this);

        if(!start){
            new Thread(new NetConn()).start();
            start = true;
        }

        //登录按钮
        LoginButton = findViewById(R.id.LoginButton);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();//(当前Activity，目标Activity)
                intent.setClass(MainActivity.this, GameLoginActivity.class);
                startActivity(intent);//转到登录页面中
            }
        });

        //注册按钮
        EnrollButton = findViewById(R.id.EnrollButton);
        EnrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();//(当前Activity，目标Activity)
                intent.setClass(MainActivity.this, GameEnrollActivity.class);
                startActivity(intent);//转到注册页面中
            }
        });
    }

    protected static class NetConn extends Thread{
        @Override
        public void run(){
            try{
                socket = new Socket();
                //运行时修改成服务器的IP
                socket.connect(new InetSocketAddress
                        (MainActivity.ipAddress,9999),5000);
                writer = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream(),"UTF-8")),true);
                Log.i("client","connect to server");

                MainActivity.writer.println(MainActivity.socket.getLocalPort() + " connect");

                System.out.println(socket);
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else{
                new Thread(){
                    @Override
                    public void run(){
                        Log.i("client","disconnect to server");
                        writer.println(MainActivity.socket.getLocalPort() + " end");
                    }
                }.start();
                ExitApplication.getInstance().exit();
            }
        }
        return true;
    }
}

