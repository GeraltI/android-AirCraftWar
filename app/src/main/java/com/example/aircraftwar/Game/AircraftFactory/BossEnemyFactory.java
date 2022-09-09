package com.example.aircraftwar.Game.AircraftFactory;

import com.example.aircraftwar.Activities.GameMenuActivity;
import com.example.aircraftwar.Activities.MainActivity;
import com.example.aircraftwar.Game.Aircraft.AbstractAircraft;
import com.example.aircraftwar.Game.Aircraft.BossEnemy;
import com.example.aircraftwar.Game.Application.ImageManager;
import com.example.aircraftwar.Game.ShootStrategy.EnemyScatteredShootStrategy;


/**
 * BOSS敌机的创建工厂
 * 创建BOSS敌机
 * @author me
 */
public class BossEnemyFactory implements AircraftFactory {

    @Override
    public AbstractAircraft createAircraft(){
        return new BossEnemy(new EnemyScatteredShootStrategy(),
                (int) (GameMenuActivity.screenWidth /2),
                ImageManager.BOSS_ENEMY_IMAGE.getHeight()/2,
                0,
                0,
                100
        );
    }

}
