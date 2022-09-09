package com.example.aircraftwar.Game.Prop;

import com.example.aircraftwar.Activities.GameMenuActivity;
import com.example.aircraftwar.Activities.MainActivity;
import com.example.aircraftwar.Game.Aircraft.HeroAircraft;
import com.example.aircraftwar.Game.Application.ImageManager;

public class BloodProp extends AbstractProp {
    @Override
    public void forward() {
        super.forward();
        // 横向超出边界后反向
        if (locationX - ImageManager.BLOOD_PROP_IMAGE.getWidth()/2 <= 0 || locationX + ImageManager.BLOOD_PROP_IMAGE.getWidth()/2 >= GameMenuActivity.screenWidth) {
            speedX = -speedX;
        }
        // 判定 y 轴向下飞行出界
        if (locationY >= GameMenuActivity.screenHeight) {
            vanish();
        }
    }

    public BloodProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(){
        System.out.println("BloodSupply active!");
        if(HeroAircraft.getHeroAircraft().getHp() < HeroAircraft.getHeroAircraft().getMaxHp()){
            HeroAircraft.getHeroAircraft().decreaseHp(-10);
        }
    };
}
