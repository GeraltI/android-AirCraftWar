package com.example.aircraftwar.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aircraftwar.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

public class GameEnrollActivity extends AppCompatActivity {

    EditText userNameEnroll;
    EditText passwordEnroll;
    EditText passwordEnrollSure;
    EditText playerNameEnroll;
    Button EnrollGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//隐藏任务栏
        setContentView(R.layout.activity_game_enroll);

        ExitApplication.getInstance().addActivity(GameEnrollActivity.this);

        userNameEnroll =(EditText) findViewById (R.id.userNameEnroll);
        passwordEnroll =(EditText) findViewById (R.id.passwordEnroll);
        passwordEnrollSure =(EditText) findViewById (R.id.passwordEnrollSure);
        playerNameEnroll =(EditText) findViewById (R.id.playerNameEnroll);

        //注册按钮
        EnrollGameButton = findViewById(R.id.EnrollGameButton);
        EnrollGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(!passwordEnroll.getText().toString().trim().equals(passwordEnrollSure.getText().toString().trim()))
                    {
                        Toast.makeText(GameEnrollActivity.this,"密码与确认密码不匹配！", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        new Thread(){
                            @Override
                            public void run(){
                                Log.i("client", "send message to server");
                                MainActivity.writer.println(MainActivity.socket.getLocalPort() + " enroll " + userNameEnroll.getText().toString() + "%" + passwordEnroll.getText().toString() + "%" + playerNameEnroll.getText().toString());
                                try {

                                    BufferedReader reader = new BufferedReader(new InputStreamReader(MainActivity.socket.getInputStream()));
                                    String[] str = reader.readLine().split(" ");

                                    //账号重复

                                    switch (str[0]) {
                                        case "NOUserName":
                                            Looper.prepare();
                                            Toast.makeText(GameEnrollActivity.this, "账号已经存在", Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                            break;

                                        //名字重复
                                        case "NOPlayerName":
                                            Looper.prepare();
                                            Toast.makeText(GameEnrollActivity.this, "名字已经存在", Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                            break;
                                        case "YES":
                                            String[] data = str[1].split("%");
                                            if (data.length == 7) {

                                                SharedPreferences.Editor editor=getSharedPreferences("userSetting",0).edit();
                                                editor.putString("userName", userNameEnroll.getText().toString());
                                                editor.apply();

                                                GameMenuActivity.userName = data[0];
                                                GameMenuActivity.password = data[1];
                                                GameMenuActivity.playerName = data[2];
                                                GameMenuActivity.integral = Integer.parseInt(data[3]);
                                                GameMenuActivity.prop1Level = Integer.parseInt(data[4]);
                                                GameMenuActivity.prop2Level = Integer.parseInt(data[5]);
                                                GameMenuActivity.prop3Level = Integer.parseInt(data[6]);

                                                MainActivity.writer.println(MainActivity.socket.getLocalPort() + " enroll_over");

                                                Intent intent = new Intent();//(当前Activity，目标Activity)
                                                intent.setClass(GameEnrollActivity.this, GameMenuActivity.class);
                                                startActivity(intent);//转到游戏主界面中
                                            }
                                            Looper.prepare();
                                            Toast.makeText(GameEnrollActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                            break;
                                        default:
                                            Looper.prepare();
                                            Toast.makeText(GameEnrollActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                            break;
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();//(当前Activity，目标Activity)
            intent.setClass(GameEnrollActivity.this, MainActivity.class);
            startActivity(intent);//转到游戏主界面中
        }
        return true;
    }
}