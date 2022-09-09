package com.example.aircraftwar.Game.Application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.fonts.Font;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.aircraftwar.Activities.GameActivity;
import com.example.aircraftwar.Activities.GameMenuActivity;
import com.example.aircraftwar.Activities.GameSelectActivity;
import com.example.aircraftwar.Activities.MainActivity;
import com.example.aircraftwar.Game.Aircraft.AbstractAircraft;
import com.example.aircraftwar.Game.Aircraft.BossEnemy;
import com.example.aircraftwar.Game.Aircraft.EliteEnemy;
import com.example.aircraftwar.Game.Aircraft.HeroAircraft;
import com.example.aircraftwar.Game.Aircraft.MobEnemy;
import com.example.aircraftwar.Game.AircraftFactory.BossEnemyFactory;
import com.example.aircraftwar.Game.AircraftFactory.EliteEnemyFactory;
import com.example.aircraftwar.Game.AircraftFactory.MobEnemyFactory;
import com.example.aircraftwar.Game.BasicObject.AbstractFlyingObject;
import com.example.aircraftwar.Game.BombPropPublish.BombPropPublisher;
import com.example.aircraftwar.Game.Bullet.BaseBullet;
import com.example.aircraftwar.Game.MusicService.MusicService;
import com.example.aircraftwar.Game.Prop.AbstractProp;
import com.example.aircraftwar.Game.Prop.BloodProp;
import com.example.aircraftwar.Game.Prop.BombProp;
import com.example.aircraftwar.Game.Prop.BulletProp;
import com.example.aircraftwar.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private final GameActivity gameActivity;
    private Game game;
    private boolean ifOnline;
    private String gameLevel;
    private int opposingGameScore = 0;
    private int opposingGameLife = 0;
    private int Count = 0;
    private int backgroundTop;
    Bitmap backgroundImg;
    float screenWidth = GameMenuActivity.screenWidth,screenHeight = GameMenuActivity.screenHeight;
    public float x = screenWidth / 2, y = screenHeight / 4 * 3;
    public boolean gameLoop = true; //控制绘画线程的标志位
    public boolean gameBack = false;
    public String gameDataTime;
    private final SurfaceHolder gameSurfaceHolder;
    private Canvas gameCanvas;  //绘图的画布
    private Paint gamePaint;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public GameView(Context context, String gameLevel, boolean ifSoundOpen,boolean ifOnline) {
        super(context);

        this.ifOnline = ifOnline;
        this.gameLevel = gameLevel;

        //获取当时日期时间  format日期时间结构
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        gameDataTime = dateTime.format(formatter);

        loading_img();

        gameActivity = (GameActivity) context;

        switch (gameLevel){
            case"easy":
                backgroundImg = ImageManager.BACKGROUND_EASY_IMAGE;
                opposingGameLife = 1000;
                game = new EasyGame(ifSoundOpen,1000);
                break;
            case"common":
                backgroundImg = ImageManager.BACKGROUND_COMMON_IMAGE;
                opposingGameLife = 500;
                game = new CommonGame(ifSoundOpen,500);
                break;
            case"difficult":
                backgroundImg = ImageManager.BACKGROUND_DIFFICULT_IMAGE;
                opposingGameLife = 300;
                game = new DifficultGame(ifSoundOpen,300);
                break;
        }

        gameLoop = true;

        gamePaint = new Paint();  //设置画笔
        gameSurfaceHolder = this.getHolder();
        gameSurfaceHolder.addCallback(this);
        this.setFocusable(true);
    }

    private void paintImageWithPositionRevised(Canvas gameCanvas, List<? extends AbstractFlyingObject> objects) {
        if (objects != null && objects.size() == 0) {
            return;
        }

        for (int i = 0;i < objects.size();i++) {
            Bitmap image = objects.get(i).getImage();

            assert image != null : objects.getClass().getName() + " has no image! ";
            gameCanvas.drawBitmap(image, objects.get(i).getLocationX() - (float)image.getWidth() / 2,
                    objects.get(i).getLocationY() - (float)image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Canvas gameCanvas,Paint gamePaint) {
        float x = 30;
        float y = 120;
        gamePaint.setTextSize(120);
        gamePaint.setFakeBoldText(true);
        gamePaint.setColor(Color.RED);
        gameCanvas.drawText("SCORE:" + getGameScore(), x, y, gamePaint);
        y = y + 120;
        gameCanvas.drawText("LIFE:" + getGameLife(), x, y, gamePaint);
        if(ifOnline){
            gamePaint.setColor(Color.BLUE);
            y = y + 120;
            gameCanvas.drawText("OPPOSING_SCORE:" + opposingGameScore, x, y, gamePaint);
            y = y + 120;
            gameCanvas.drawText("OPPOSING_LIFE:" + opposingGameLife, x, y, gamePaint);
        }
    }

    public void draw(){
        //通过SurfaceHolder对象的lockCanvans()方法，我们可以获取当前的Canvas绘图对象
        gameCanvas = gameSurfaceHolder.lockCanvas();
        if(gameSurfaceHolder == null || gameCanvas == null){
            return;
        }
        if(Count < 50){
            gameLoop = !game.gameOver;
            if(!gameBack &&!gameLoop){
                Log.i("client", "send message to server");
                MainActivity.writer.println(MainActivity.socket.getLocalPort() + " game_" + gameLevel + "_over " + GameMenuActivity.playerName + "%" + getGameScore() + "%" + gameDataTime);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    gameActivity.gameOver();
                }
            }
            Count ++;
        }else if(Count == 50){
            if(ifOnline){
                new Thread(){
                    @Override
                    public void run(){
                        Log.i("client", "send message to server");
                        if(gameLoop){
                            MainActivity.writer.println(MainActivity.socket.getLocalPort() + " game_send_gameData " + getGameScore() + "%" +getGameLife());
                            try {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(MainActivity.socket.getInputStream()));
                                if(reader.readLine() != null){
                                    String[] str = reader.readLine().split(" ");
                                    if(isNumeric(str[0]) && isNumeric(str[1])){
                                        opposingGameScore = Integer.parseInt(str[0]);
                                        opposingGameLife = Integer.parseInt(str[1]);
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
            Count = 0;
        }
        gameCanvas.drawBitmap(backgroundImg,
                (float) ((screenWidth - backgroundImg.getWidth()) / 2),
                (float) backgroundTop,
                null);
        gameCanvas.drawBitmap(backgroundImg,
                (float) ((screenWidth - backgroundImg.getWidth()) / 2),
                (float) (backgroundTop - backgroundImg.getHeight()),
                null);
        backgroundTop += 1;
        if(backgroundTop == backgroundImg.getHeight()){
            backgroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(gameCanvas, game.enemyBullets);
        paintImageWithPositionRevised(gameCanvas, game.heroBullets);

        paintImageWithPositionRevised(gameCanvas, game.enemyAircrafts);

        paintImageWithPositionRevised(gameCanvas, game.Props);

        gameCanvas.drawBitmap(ImageManager.HERO_IMAGE, game.heroAircraft.getLocationX() - (float)ImageManager.HERO_IMAGE.getWidth() / 2,
                game.heroAircraft.getLocationY() - (float)ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(gameCanvas,gamePaint);

        gamePaint.setAntiAlias(true);
        //通过unlockCanvasAndPost(mCanvas)方法对画布内容进行提交
        gameSurfaceHolder.unlockCanvasAndPost(gameCanvas);
    }

    private boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public int getGameScore(){
        return game.score;
    }

    public int getGameLife(){
        return game.heroAircraft.getHp();
    }



    public void loading_img(){
        ImageManager.BACKGROUND_EASY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        ImageManager.BACKGROUND_COMMON_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg3);
        ImageManager.BACKGROUND_DIFFICULT_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg5);
        ImageManager.HERO_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        ImageManager.HERO_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_hero);
        ImageManager.ENEMY_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_enemy);
        ImageManager.MOB_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.mob);
        ImageManager.ELITE_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.elite);
        ImageManager.BOSS_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.boss);
        ImageManager.BLOOD_PROP_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_blood);
        ImageManager.BULLET_PROP_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bullet);
        ImageManager.BOMB_PROP_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bomb);
        ImageManager.putImg();
    }

    public void gameBack(){
        surfaceDestroyed(gameSurfaceHolder);
        GameActivity.gameBinder.stopMusicService();
        gameBack = true;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        gameLoop = false;
    }

    @Override
    public void run() {
        //设置一个循环来绘制，通过标志位来控制开启绘制还是停止
        while (gameLoop){
            synchronized (gameSurfaceHolder){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    game.action(x,y);
                }
                draw();
                try {
                    Thread.sleep(10);
                }catch (Exception e){
                }
            }
        }
    }
}
