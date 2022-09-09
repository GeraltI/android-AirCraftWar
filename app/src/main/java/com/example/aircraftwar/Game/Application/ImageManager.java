package com.example.aircraftwar.Game.Application;

import android.graphics.Bitmap;
import android.graphics.Matrix;


import com.example.aircraftwar.Activities.GameMenuActivity;
import com.example.aircraftwar.Game.Aircraft.*;
import com.example.aircraftwar.Game.Bullet.EnemyBullet;
import com.example.aircraftwar.Game.Bullet.HeroBullet;
import com.example.aircraftwar.Game.Prop.BloodProp;
import com.example.aircraftwar.Game.Prop.BombProp;
import com.example.aircraftwar.Game.Prop.BulletProp;

import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author hitsz
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     */
    private static final Map<String, Bitmap> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static Bitmap BACKGROUND_EASY_IMAGE;
    public static Bitmap BACKGROUND_COMMON_IMAGE;
    public static Bitmap BACKGROUND_DIFFICULT_IMAGE;
    public static Bitmap HERO_IMAGE;
    public static Bitmap HERO_BULLET_IMAGE;
    public static Bitmap ENEMY_BULLET_IMAGE;
    public static Bitmap MOB_ENEMY_IMAGE;
    public static Bitmap ELITE_ENEMY_IMAGE;
    public static Bitmap BOSS_ENEMY_IMAGE;
    public static Bitmap BLOOD_PROP_IMAGE;
    public static Bitmap BOMB_PROP_IMAGE;
    public static Bitmap BULLET_PROP_IMAGE;

    public static Bitmap get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static Bitmap get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }

    public static void putImg(){

        BACKGROUND_EASY_IMAGE = imageCompress(BACKGROUND_EASY_IMAGE);
        BACKGROUND_COMMON_IMAGE = imageCompress(BACKGROUND_COMMON_IMAGE);
        BACKGROUND_DIFFICULT_IMAGE = imageCompress(BACKGROUND_DIFFICULT_IMAGE);

        CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
        CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), BOSS_ENEMY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
        CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BloodProp.class.getName(), BLOOD_PROP_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BombProp.class.getName(), BOMB_PROP_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BulletProp.class.getName(), BULLET_PROP_IMAGE);
    }

    private static Bitmap imageCompress(Bitmap oldBitmap){
        Bitmap newBitmap;
        int width = oldBitmap.getWidth();
        int height = oldBitmap.getHeight();
        float scaleWidth;//缩放宽度
        float scaleHeight;//缩放高度
        Matrix matrix = new Matrix();

        scaleHeight = scaleWidth = ((float) GameMenuActivity.screenHeight) / height;

        matrix.postScale(scaleWidth,scaleHeight);
        newBitmap = Bitmap.createBitmap(oldBitmap,
                0,
                0,
                width,
                height,
                matrix,
                true);//根据比例缩放获得新图
        return newBitmap;
    }

}
