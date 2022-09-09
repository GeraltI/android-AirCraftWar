package com.example.aircraftwar.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.aircraftwar.R;

import java.util.Objects;

public class GameSelectActivity extends AppCompatActivity {

    Button EasyButton;
    Button CommonButton;
    Button DifficultButton;
    Button GameRankListButton;
    Switch SoundSelectSwitch;
    private String gameLevel;
    private boolean ifSoundOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_game_select);

        ExitApplication.getInstance().addActivity(GameSelectActivity.this);

        //音效开关
        SoundSelectSwitch = findViewById(R.id.SoundSelectSwitch);
        SoundSelectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        EasyButton = findViewById(R.id.EasyButton);
        EasyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameLevel = "easy";
                Intent intent = new Intent();//(当前Activity，目标Activity)
                intent.putExtra("gameLevel",gameLevel);
                intent.putExtra("ifSoundOpen",ifSoundOpen);
                intent.setClass(GameSelectActivity.this, GameActivity.class);
                startActivity(intent);//转到游戏选择中
            }
        });

        //普通按钮
        CommonButton = findViewById(R.id.CommonButton);
        CommonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameLevel = "common";
                Intent intent = new Intent();//(当前Activity，目标Activity)
                intent.putExtra("gameLevel",gameLevel);
                intent.putExtra("ifSoundOpen",ifSoundOpen);
                intent.setClass(GameSelectActivity.this, GameActivity.class);
                startActivity(intent);//转到游戏选择中
            }
        });

        //困难按钮
        DifficultButton = findViewById(R.id.DifficultButton);
        DifficultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameLevel = "difficult";
                Intent intent = new Intent();//(当前Activity，目标Activity)
                intent.putExtra("gameLevel",gameLevel);
                intent.putExtra("ifSoundOpen",ifSoundOpen);
                intent.setClass(GameSelectActivity.this, GameActivity.class);
                startActivity(intent);//转到游戏选择中
            }
        });

        //单机游戏得分排行榜
        GameRankListButton = findViewById(R.id.GameRankListButton);
        GameRankListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();//(当前Activity，目标Activity)
                intent.setClass(GameSelectActivity.this, GameRankListSelectActivity.class);
                startActivity(intent);//转到游戏排行榜选择中
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(GameSelectActivity.this,GameMenuActivity.class);
            startActivity(intent);//转到游戏主页面中
        }
        return true;
    }

}