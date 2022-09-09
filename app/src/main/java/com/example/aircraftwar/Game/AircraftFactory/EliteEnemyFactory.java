package com.example.aircraftwar.Game.AircraftFactory;

import com.example.aircraftwar.Activities.GameMenuActivity;
import com.example.aircraftwar.Activities.MainActivity;
import com.example.aircraftwar.Game.Aircraft.AbstractAircraft;
import com.example.aircraftwar.Game.Aircraft.EliteEnemy;
import com.example.aircraftwar.Game.Application.ImageManager;
import com.example.aircraftwar.Game.ShootStrategy.EnemyStraightShootStrategy;


/**
 * ELITE敌机的创建工厂
 * 创建ELITE敌机
 *
 * @author me
 */
public class EliteEnemyFactory implements AircraftFactory {
    @Override
    public AbstractAircraft createAircraft(){
        if(Math.random()<0.5){
            return new EliteEnemy(new EnemyStraightShootStrategy(),
                    ImageManager.ELITE_ENEMY_IMAGE.getWidth()/2 + (int) (Math.random() * (GameMenuActivity.screenWidth - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                    (int) (Math.random() * GameMenuActivity.screenHeight * 0.2),
                    3,
                    8,
                    20
            );
        }
        else{
            return new EliteEnemy(new EnemyStraightShootStrategy(),
                    ImageManager.ELITE_ENEMY_IMAGE.getWidth()/2 + (int) (Math.random() * (GameMenuActivity.screenWidth - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                    (int) (Math.random() * GameMenuActivity.screenHeight * 0.2),
                    -3,
                    8,
                    20
            );
        }
    }
}
