package com.example.aircraftwar.Game.Aircraft;

import com.example.aircraftwar.Game.BasicObject.AbstractFlyingObject;
import com.example.aircraftwar.Game.Bullet.BaseBullet;
import com.example.aircraftwar.Game.Prop.AbstractProp;
import com.example.aircraftwar.Game.ShootStrategy.ShootStrategy;

import java.util.List;

public abstract class AbstractAircraft extends AbstractFlyingObject implements ShootStrategy {
    /**
     * 生命值
     * 射击策略
     */
    protected int maxHp;
    protected int hp;
    protected ShootStrategy shootstrategy;


    public AbstractAircraft(ShootStrategy shootstrategy,int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.shootstrategy = shootstrategy;
        this.hp = hp;
        this.maxHp = hp;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }

    public void setMaxHp(int maxHp){
        this.maxHp = maxHp;
    }
    public int getMaxHp(){
        return maxHp;
    }

    public int getHp() {
        return hp;
    }

    @Override
    public List<BaseBullet> shootStrategy(int shootNum, int locationX, int locationY, int speedX, int speedY, int power){
        return shootstrategy.shootStrategy(shootNum,locationX,locationY,speedX,speedY,power);
    }

    public void setShootStrategy(ShootStrategy shootstrategy) {
        this.shootstrategy = shootstrategy;
    }

    /**
     * 飞机射击方法，可射击对象必须实现
     * 可射击对象需实现，返回子弹
     * 非可射击对象空实现，返回null
     * @return <BaseBullet>
     */
    public abstract List<BaseBullet> shoot();

    /**
     * 飞机被击败掉落奖励方法，掉落奖励对象必须实现
     * @return List<AbstractProp>
     * 飞机被击败掉落奖励对象实现，返回奖励
     * 非被击败掉落奖励对象空实现，返回null
     */
    public abstract List<AbstractProp> reward();
}
