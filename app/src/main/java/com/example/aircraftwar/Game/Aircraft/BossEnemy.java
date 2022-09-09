package com.example.aircraftwar.Game.Aircraft;

import com.example.aircraftwar.Activities.GameMenuActivity;
import com.example.aircraftwar.Activities.MainActivity;
import com.example.aircraftwar.Game.Bullet.BaseBullet;
import com.example.aircraftwar.Game.Prop.AbstractProp;
import com.example.aircraftwar.Game.PropFactory.BloodPropFactory;
import com.example.aircraftwar.Game.PropFactory.BombPropFactory;
import com.example.aircraftwar.Game.PropFactory.BulletPropFactory;
import com.example.aircraftwar.Game.ShootStrategy.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class BossEnemy extends AbstractAircraft{
    /**
     * locationX 老大敌机位置x坐标
     * locationY 老大敌机位置y坐标
     * speedX 老大敌机的基准速度
     * speedY 老大敌机的基准速度
     * hp 老大敌机初始生命值
     * shoot strategy 老大敌机的射击策略
     */


    public BossEnemy(ShootStrategy shootstrategy, int locationX, int locationY, int speedX, int speedY, int hp) {
        super(shootstrategy,locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= GameMenuActivity.screenHeight) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        return shootstrategy.shootStrategy(3,locationX,locationY,speedX,speedY,10);
    }

    @Override
    public List<AbstractProp> reward(){
        List<AbstractProp> res = new LinkedList<>();
        int speeds;
        if(Math.random() < 0.5){
            speeds = 1;
        }
        else{
            speeds = -1;
        }
        if(Math.random() < 0.4){
            BloodPropFactory bloodPropFactory = new BloodPropFactory(locationX,locationY,speeds,8);
            res.add(bloodPropFactory.createProp());
        }
        else if(Math.random() < 0.3 / 0.6){
            BulletPropFactory bulletPropFactory = new BulletPropFactory(locationX,locationY,speeds,8);
            res.add(bulletPropFactory.createProp());
        }
        else if(Math.random() < 0.2 / 0.7 / 0.6){
            BombPropFactory bombPropFactory = new BombPropFactory(locationX,locationY,speeds,8);
            res.add(bombPropFactory.createProp());
        }
        return res;
    }
}
