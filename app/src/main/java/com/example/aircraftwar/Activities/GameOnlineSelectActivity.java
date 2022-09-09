package com.example.aircraftwar.Activities;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.aircraftwar.R;
import com.example.aircraftwar.RankList.GameData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class GameOnlineSelectActivity extends AppCompatActivity {

    Button EasyOnlineButton;
    Button CommonOnlineButton;
    Button DifficultOnlineButton;
    Switch SoundSelectOnlineSwitch;
    private String gameLevel;
    private boolean ifSoundOpen;
    private boolean waiting = false;
    private boolean gameReady = false;
    private long exitTime = 0;
    private int oppositeUserLocalPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_game_online_select);

        waiting = false;
        gameReady = false;

        ExitApplication.getInstance().addActivity(GameOnlineSelectActivity.this);

        //音效开关
        SoundSelectOnlineSwitch = findViewById(R.id.SoundSelectOnlineSwitch);
        SoundSelectOnlineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    ifSoundOpen = true;
                }
                else
                {
                    ifSoundOpen = false;
                }
            }
        });

        //简单按钮
        EasyOnlineButton = findViewById(R.id.EasyOnlineButton);
        EasyOnlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waiting = true;
                gameLevel = "easy";
                new Thread(){
                    @Override
                    public void run(){
                        while(waiting && gameLevel.equals("easy")){
                            if ((System.currentTimeMillis() - exitTime) > 2500) {
                                exitTime = System.currentTimeMillis();
                                Log.i("client", "send message to server");
                                MainActivity.writer.println(MainActivity.socket.getLocalPort() + " game_" + gameLevel + "_wait");
                                try {
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(MainActivity.socket.getInputStream()));
                                    String[] str = reader.readLine().split(" ");
                                    if(str[0].equals("NOT_READY")){
                                        new Thread(){
                                            @Override
                                            public void run(){
                                                Looper.prepare();
                                                Toast.makeText(getApplicationContext(), "等待对手加入中\n按下返回退出等待队列", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }
                                        }.start();
                                    }
                                    else if(str[0].equals("READY")){
                                        gameReady = true;
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if(gameReady){
                                waiting = false;
                                Intent intent = new Intent();//(当前Activity，目标Activity)
                                intent.putExtra("gameLevel",gameLevel);
                                intent.putExtra("ifSoundOpen",ifSoundOpen);
                                intent.putExtra("ifOnline",true);
                                intent.setClass(GameOnlineSelectActivity.this, GameActivity.class);
                                startActivity(intent);//转到游戏选择中
                            }
                        }
                    }
                }.start();
            }
        });

        //普通按钮
        CommonOnlineButton = findViewById(R.id.CommonOnlineButton);
        CommonOnlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waiting = true;
                gameLevel = "common";
                new Thread(){
                    @Override
                    public void run(){
                        while(waiting && gameLevel.equals("common")){
                            if ((System.currentTimeMillis() - exitTime) > 2500) {
                                System.out.println(waiting);
                                exitTime = System.currentTimeMillis();
                                Log.i("client", "send message to server");
                                MainActivity.writer.println(MainActivity.socket.getLocalPort() + " game_" + gameLevel + "_wait");
                                try {
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(MainActivity.socket.getInputStream()));
                                    String[] str = reader.readLine().split(" ");
                                    if(str[0].equals("NOT_READY")){
                                        new Thread(){
                                            @Override
                                            public void run(){
                                                Looper.prepare();
                                                Toast.makeText(getApplicationContext(), "等待对手加入中\n按下返回退出等待队列", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }
                                        }.start();
                                    }
                                    else if(str[0].equals("READY")){
                                        gameReady = true;
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if(gameReady){
                                waiting = false;
                                Intent intent = new Intent();//(当前Activity，目标Activity)
                                intent.putExtra("gameLevel",gameLevel);
                                intent.putExtra("ifSoundOpen",ifSoundOpen);
                                intent.putExtra("ifOnline",true);
                                intent.setClass(GameOnlineSelectActivity.this, GameActivity.class);
                                startActivity(intent);//转到游戏选择中
                            }
                        }
                    }
                }.start();
            }
        });

        //困难按钮
        DifficultOnlineButton = findViewById(R.id.DifficultOnlineButton);
        DifficultOnlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waiting = true;
                gameLevel = "difficult";
                new Thread(){
                    @Override
                    public void run(){
                        while(waiting && gameLevel.equals("difficult")){
                            if ((System.currentTimeMillis() - exitTime) > 2500) {
                                exitTime = System.currentTimeMillis();
                                Log.i("client", "send message to server");
                                MainActivity.writer.println(MainActivity.socket.getLocalPort() + " game_" + gameLevel + "_wait");
                                try {
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(MainActivity.socket.getInputStream()));
                                    String[] str = reader.readLine().split(" ");
                                    if(str[0].equals("NOT_READY")){
                                        new Thread(){
                                            @Override
                                            public void run(){
                                                Looper.prepare();
                                                Toast.makeText(getApplicationContext(), "等待对手加入中\n按下返回退出等待队列", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }
                                        }.start();
                                    }
                                    else if(str[0].equals("READY")){
                                        gameReady = true;
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if(gameReady){
                                waiting = false;
                                Intent intent = new Intent();//(当前Activity，目标Activity)
                                intent.putExtra("gameLevel",gameLevel);
                                intent.putExtra("ifSoundOpen",ifSoundOpen);
                                intent.putExtra("ifOnline",true);
                                intent.setClass(GameOnlineSelectActivity.this, GameActivity.class);
                                startActivity(intent);//转到游戏选择中
                            }
                        }
                    }
                }.start();
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(waiting) {
                waiting = false;
                Toast.makeText(getApplicationContext(), "退出等待队列", Toast.LENGTH_SHORT).show();
                new Thread(){
                    @Override
                    public void run(){
                        Log.i("client", "send message to server");
                        MainActivity.writer.println(MainActivity.socket.getLocalPort() + " game_" + gameLevel + "_waiting_stop");
                    }
                }.start();
            }
            else{
                Intent intent = new Intent();//(当前Activity，目标Activity)
                intent.setClass(GameOnlineSelectActivity.this, GameMenuActivity.class);
                startActivity(intent);//转到游戏开始界面中
            }
        }
        return true;
    }


}