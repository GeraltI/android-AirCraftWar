package com.example.aircraftwar.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.aircraftwar.R;

import java.util.Objects;

public class GameOnlineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//隐藏任务栏
        setContentView(R.layout.activity_game_online);

        ExitApplication.getInstance().addActivity(GameOnlineActivity.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(GameOnlineActivity.this,GameMenuActivity.class);
            startActivity(intent);//转到游戏主页面中
        }
        return true;
    }
}