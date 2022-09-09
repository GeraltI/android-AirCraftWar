package com.example.aircraftwar.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.aircraftwar.Game.Application.GameView;
import com.example.aircraftwar.Game.MusicService.MusicService;
import com.example.aircraftwar.RankList.GameDataDaoImpl;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {

    public static MusicService.GameBinder gameBinder;
    private Connect conn;
    private Intent intent;

    private GameView gameView;
    private String gameLevel;
    private boolean ifSoundOpen;
    private boolean ifOnline;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//隐藏任务栏

        intent = getIntent();
        gameLevel = intent.getStringExtra("gameLevel");
        ifSoundOpen = intent.getBooleanExtra("ifSoundOpen",false);
        ifOnline = intent.getBooleanExtra("ifOnline",false);

        conn = new Connect();
        intent = new Intent(this,MusicService.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);

        gameView = new GameView(this,gameLevel,ifSoundOpen,ifOnline);
        setContentView(gameView);

        ExitApplication.getInstance().addActivity(GameActivity.this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unbindService(conn);
    }

    class Connect implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            gameBinder = (MusicService.GameBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gameView.x = (int) event.getX();
        gameView.y = (int) event.getY();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            gameView.gameBack();
            intent = new Intent(GameActivity.this,GameSelectActivity.class);
            startActivity(intent);//转到单机游戏选择界面中
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void gameOver(){
        GameDataDaoImpl gameDataDaoImpl = new GameDataDaoImpl(gameLevel);
        gameDataDaoImpl.doAdd(GameMenuActivity.playerName,gameView.getGameScore(),gameView.gameDataTime);
        gameDataDaoImpl.setAllGameDatas();
        if(ifOnline){
            intent = new Intent();//(当前Activity，目标Activity)
            intent.putExtra("gameLevel",gameLevel);
            intent.setClass(GameActivity.this, GameOnlineRankListActivity.class);
            startActivity(intent);//转到联机游戏排行中
        }
        else{
            intent = new Intent();//(当前Activity，目标Activity)
            intent.putExtra("gameLevel",gameLevel);
            intent.setClass(GameActivity.this, GameRankListActivity.class);
            startActivity(intent);//转到单机游戏排行中
        }
    }

}