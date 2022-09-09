package com.example.aircraftwar.Game.MusicService;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.aircraftwar.R;

import java.security.Provider;
import java.util.HashMap;

public class MusicService extends Service {
    public MusicService() {
    }
    //创建播放器对象
    private MediaPlayer player;
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();
    private SoundPool gameSoundPool;

    @Override
    public void onCreate() {
        super.onCreate();
        gameSoundPool =  new SoundPool.Builder().setMaxStreams(10).build() ;
        soundID.put(1, gameSoundPool.load(this, R.raw.bullet, 1));
        soundID.put(2, gameSoundPool.load(this, R.raw.bullet_hit, 1));
        soundID.put(3, gameSoundPool.load(this, R.raw.get_supply, 1));
        soundID.put(4, gameSoundPool.load(this, R.raw.bomb_explosion, 1));
        soundID.put(5, gameSoundPool.load(this, R.raw.game_over, 1));
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        playBGMMusic();
        return new GameBinder();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    //播放BGM音乐
    public void playBGMMusic(){
        if(player != null && player.isPlaying()){
            player.stop();
        }
        player = MediaPlayer.create(this, R.raw.bgm);
        player.setLooping(true);
        player.start();
    }
    //播放BOSSBGM音乐
    public void playBOSSBGMMusic(){
        if(player != null && player.isPlaying()){
            player.stop();
        }
        player = MediaPlayer.create(this, R.raw.bgm_boss);
        player.setLooping(true);
        player.start();
    }
    //暂停播放
    public void pauseMusic(){
        if(player != null && player.isPlaying()){
            player.pause();
        }
    }

    /**
     * 停止播放
     */
    public void stopMusic() {
        if (player != null) {
            player.stop();
            player.reset();//重置
            player.release();//释放
            player = null;
        }
    }

    public class GameBinder extends Binder {

        public void playBullet(){
            gameSoundPool.play(soundID.get(1), 1, 1, 0,0,1);
        }

        public void playBulletHit(){
            gameSoundPool.play(soundID.get(2), 1, 1, 0,0,1);
        }

        public void playGetSupply(){
            gameSoundPool.play(soundID.get(3), 1, 1, 0,0,1);
        }

        public void playBombExplosion(){
            gameSoundPool.play(soundID.get(4), 1, 1, 0,0,1);
        }

        public void playGameOver(){gameSoundPool.play(soundID.get(5), 1, 1, 0, 0, 1);}

        public void playMusic_BGM_BOSS(){
            playBOSSBGMMusic();
        }

        public void playMusic_BGM(){
            playBGMMusic();
        }

        public void stopMusicService(){
            onDestroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMusic();
    }
}
