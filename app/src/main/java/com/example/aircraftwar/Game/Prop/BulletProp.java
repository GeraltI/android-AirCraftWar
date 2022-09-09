package com.example.aircraftwar.Game.Prop;

import com.example.aircraftwar.Activities.GameMenuActivity;
import com.example.aircraftwar.Game.Aircraft.HeroAircraft;
import com.example.aircraftwar.Game.Application.ImageManager;
import com.example.aircraftwar.Game.ShootStrategy.HeroScatteredShootStrategy;
import com.example.aircraftwar.Game.ShootStrategy.HeroStraightShootStrategy;

public class BulletProp extends AbstractProp{

    private long exitTime = 0;

    @Override
    public void forward() {
        super.forward();
        // 横向超出边界后反向
        if (locationX - ImageManager.BULLET_PROP_IMAGE.getWidth()/2 <= 0 || locationX + ImageManager.BULLET_PROP_IMAGE.getWidth()/2 >= GameMenuActivity.screenWidth) {
            speedX = -speedX;
        }
        // 判定 y 轴向下飞行出界
        if (locationY >= GameMenuActivity.screenHeight) {
            vanish();
        }
    }

    public BulletProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }



    @Override
    public void effect() {
        System.out.println("BulletSupply active!");
        Runnable r = () -> {
            HeroAircraft.getHeroAircraft().setShootNum(HeroAircraft.getHeroAircraft().getShootNum() + 1);
            HeroAircraft.getHeroAircraft().setShootStrategy(new HeroScatteredShootStrategy());
            exitTime = System.currentTimeMillis();
            while(System.currentTimeMillis() - exitTime < 20000){
                if(HeroAircraft.getHeroAircraft().notValid()){
                    break;
                }
            }
            System.out.println("BulletSupply not active!");
            HeroAircraft.getHeroAircraft().setShootNum(HeroAircraft.getHeroAircraft().getShootNum() - 1);
            if (HeroAircraft.getHeroAircraft().getShootNum() == 2) {
                HeroAircraft.getHeroAircraft().setShootStrategy(new HeroStraightShootStrategy());
            }
        };
        new Thread(r).start();
    }
}
