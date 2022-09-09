package com.example.aircraftwar.Game.Aircraft;

import com.example.aircraftwar.Activities.GameMenuActivity;
import com.example.aircraftwar.Activities.MainActivity;
import com.example.aircraftwar.Game.BombPropPublish.BombPropPublisher;
import com.example.aircraftwar.Game.Bullet.BaseBullet;
import com.example.aircraftwar.Game.Prop.AbstractProp;
import com.example.aircraftwar.Game.ShootStrategy.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class MobEnemy extends AbstractAircraft{
    /**
     * locationX 普通敌机位置x坐标
     * locationY 普通敌机位置y坐标
     * speedX 普通敌机的基准速度
     * speedY 普通敌机的基准速度
     * hp 普通敌机初始生命值
     * shoot strategy  普通敌机射击策略
     */


    public MobEnemy(ShootStrategy shootstrategy, int locationX, int locationY, int speedX, int speedY, int hp) {
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
        return shootstrategy.shootStrategy(0,locationX,locationY,speedX,speedY,10);
    }

    @Override
    public List<AbstractProp> reward(){
        return new LinkedList<>();
    }

    @Override
    public void response() {
        if(this.isValid){
            this.vanish();
            BombPropPublisher.getBombPropPublisher().scoreChange = BombPropPublisher.getBombPropPublisher().scoreChange + 10;
        }
    }
}
