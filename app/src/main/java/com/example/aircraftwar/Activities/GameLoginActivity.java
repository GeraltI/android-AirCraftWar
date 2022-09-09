package com.example.aircraftwar.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Objects;

public class GameLoginActivity extends AppCompatActivity {

    EditText userNameLogin;
    EditText passwordLogin;
    CheckBox rememberPasswordBox;
    Button LoginGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//隐藏任务栏
        setContentView(R.layout.activity_game_login);

        ExitApplication.getInstance().addActivity(GameLoginActivity.this);

        userNameLogin =(EditText) findViewById (R.id.userNameLogin);
        passwordLogin =(EditText) findViewById (R.id.passwordLogin);

        rememberPasswordBox = (CheckBox) findViewById (R.id.rememberPasswordBox);

        boolean rememberPassword = getSharedPreferences("userSetting", 0).getBoolean("rememberPassword", false);

        userNameLogin.setText(getSharedPreferences("userSetting",0).getString("userName",""));
        rememberPasswordBox.setChecked(rememberPassword);
        if(rememberPassword){
            passwordLogin.setText(getSharedPreferences("userSetting",0).getString("password",""));
        }

        //登录按钮
        LoginGameButton = findViewById(R.id.LoginGameButton);
        LoginGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(userNameLogin.getText().toString().trim().equals("admin") && passwordLogin.getText().toString().trim().equals("123"))
                    {
                        GameMenuActivity.userName = "admin";
                        GameMenuActivity.password = "123";
                        GameMenuActivity.playerName = "user";
                        GameMenuActivity.integral = 100;
                        GameMenuActivity.prop1Level = 0;
                        GameMenuActivity.prop2Level = 0;
                        GameMenuActivity.prop3Level = 0;
                        Intent intent = new Intent();//(当前Activity，目标Activity)
                        intent.setClass(GameLoginActivity.this, GameMenuActivity.class);
                        startActivity(intent);//转到游戏主界面中
                    }
                    else{
                        new Thread(){
                            @Override
                            public void run(){
                                Log.i("client", "send message to server");
                                MainActivity.writer.println(MainActivity.socket.getLocalPort() + " login " + userNameLogin.getText().toString() + "%" + passwordLogin.getText().toString());
                                try {

                                    BufferedReader reader = new BufferedReader(new InputStreamReader(MainActivity.socket.getInputStream()));
                                    String[] str = reader.readLine().split(" ");

                                    //账号密码正确
                                    if(str[0].equals("YES")){
                                        String[] data = str[1].split("%");
                                        if(data.length == 7){
                                            SharedPreferences.Editor editor=getSharedPreferences("userSetting",0).edit();
                                            editor.putString("userName", userNameLogin.getText().toString());
                                            editor.putString("password",passwordLogin.getText().toString());
                                            editor.putBoolean("rememberPassword",rememberPasswordBox.isChecked());
                                            editor.apply();

                                            GameMenuActivity.userName = data[0];
                                            GameMenuActivity.password = data[1];
                                            GameMenuActivity.playerName = data[2];
                                            GameMenuActivity.integral = Integer.parseInt(data[3]);
                                            GameMenuActivity.prop1Level = Integer.parseInt(data[4]);
                                            GameMenuActivity.prop2Level = Integer.parseInt(data[5]);
                                            GameMenuActivity.prop3Level = Integer.parseInt(data[6]);

                                            Intent intent = new Intent();//(当前Activity，目标Activity)
                                            intent.setClass(GameLoginActivity.this, GameMenuActivity.class);
                                            startActivity(intent);//转到游戏主界面中
                                        }
                                        Looper.prepare();
                                        Toast.makeText(GameLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }

                                    //账号密码不正确
                                    else{
                                        Looper.prepare();
                                        Toast.makeText(GameLoginActivity.this,"账号或者密码错误", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
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
            SharedPreferences.Editor editor=getSharedPreferences("userSetting",0).edit();
            editor.putBoolean("rememberPassword",rememberPasswordBox.isChecked());
            editor.apply();
            Intent intent = new Intent();//(当前Activity，目标Activity)
            intent.setClass(GameLoginActivity.this, MainActivity.class);
            startActivity(intent);//转到游戏主界面中
        }
        return true;
    }

}