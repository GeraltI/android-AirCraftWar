package com.example.aircraftwar.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.aircraftwar.R;

import java.util.Objects;

public class GameRankListSelectActivity extends AppCompatActivity {

    Button EasyRankListButton;
    Button CommonRankListButton;
    Button DifficultRankListButton;
    private String gameLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//隐藏任务栏
        setContentView(R.layout.activity_game_rank_list_select);

        ExitApplication.getInstance().addActivity(GameRankListSelectActivity.this);

        //简单排行榜按钮
        EasyRankListButton = findViewById(R.id.EasyRankListButton);
        EasyRankListButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                gameLevel = "easy";
                Intent intent = new Intent();//(当前Activity，目标Activity)
                intent.putExtra("gameLevel",gameLevel);
                intent.setClass(GameRankListSelectActivity.this, GameRankListActivity.class);
                startActivity(intent);//转到单机游戏简单排行中
            }
        });

        //普通排行榜按钮
        CommonRankListButton = findViewById(R.id.CommonRankListButton);
        CommonRankListButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                gameLevel = "common";
                Intent intent = new Intent();//(当前Activity，目标Activity)
                intent.putExtra("gameLevel",gameLevel);
                intent.setClass(GameRankListSelectActivity.this, GameRankListActivity.class);
                startActivity(intent);//转到单机游戏中等排行中
            }
        });

        //困难排行榜按钮
        DifficultRankListButton = findViewById(R.id.DifficultRankListButton);
        DifficultRankListButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                gameLevel = "difficult";
                Intent intent = new Intent();//(当前Activity，目标Activity)
                intent.putExtra("gameLevel",gameLevel);
                intent.setClass(GameRankListSelectActivity.this, GameRankListActivity.class);
                startActivity(intent);//转到单机游戏困难排行中
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(GameRankListSelectActivity.this,GameSelectActivity.class);
            startActivity(intent);//转到单机游戏选择界面中
        }
        return true;
    }
}