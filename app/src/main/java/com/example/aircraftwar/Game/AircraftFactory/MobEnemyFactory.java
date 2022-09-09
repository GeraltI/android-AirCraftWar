package com.example.aircraftwar.Game.AircraftFactory;

import com.example.aircraftwar.Activities.GameMenuActivity;
import com.example.aircraftwar.Activities.MainActivity;
import com.example.aircraftwar.Game.Aircraft.AbstractAircraft;
import com.example.aircraftwar.Game.Aircraft.MobEnemy;
import com.example.aircraftwar.Game.Application.ImageManager;
import com.example.aircraftwar.Game.ShootStrategy.EnemyStraightShootStrategy;

/**
 * MOB敌机的创建工厂
 * 创建MOB敌机
 *
 * @author me
 */
public class MobEnemyFactory implements AircraftFactory {

    @Override
    public AbstractAircraft createAircraft(){
        return new MobEnemy(new EnemyStraightShootStrategy(),
                (int) (Math.random() * (GameMenuActivity.screenWidth - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * GameMenuActivity.screenHeight * 0.2),
                0,
                10,
                10
        );
    }

}
