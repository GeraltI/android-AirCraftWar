package com.example.aircraftwar.Game.Application;

import com.example.aircraftwar.Activities.GameActivity;
import com.example.aircraftwar.Game.Aircraft.AbstractAircraft;
import com.example.aircraftwar.Game.Aircraft.HeroAircraft;
import com.example.aircraftwar.Game.AircraftFactory.BossEnemyFactory;
import com.example.aircraftwar.Game.AircraftFactory.EliteEnemyFactory;
import com.example.aircraftwar.Game.AircraftFactory.MobEnemyFactory;

public class DifficultGame extends Game {

    private int BossTime = 0;

    DifficultGame(boolean ifSoundOpen,int maxHp) {
        super(ifSoundOpen,maxHp);
        enemyMaxNumber = 15;
    }

    protected void timeLevelup() {
        levelUp = levelUp + 0.06;
    }

    protected void createEnemyAircraft() {
        if (enemyAircrafts.size() < enemyMaxNumber) {
            if (score - scorePast >= 200 && !bossValid) {
                BossEnemyFactory aircraftFactory = new BossEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                abstractAircraft.decreaseHp(-50 * BossTime);
                abstractAircraft.setSpeedX((int) (abstractAircraft.getSpeedX() * (1 + levelUp)));
                abstractAircraft.setSpeedY((int) (abstractAircraft.getSpeedY() * (1 + levelUp)));
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
                bossValid = true;
                BossTime = BossTime + 1;
                //音乐
                GameActivity.gameBinder.playMusic_BGM_BOSS();
            } else if (Math.random() < 0.2) {
                EliteEnemyFactory aircraftFactory = new EliteEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                abstractAircraft.setSpeedX((int) (abstractAircraft.getSpeedX() * (1 + levelUp)));
                abstractAircraft.setSpeedY((int) (abstractAircraft.getSpeedY() * (1 + levelUp)));
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
            } else {
                MobEnemyFactory aircraftFactory = new MobEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                abstractAircraft.setSpeedX((int) (abstractAircraft.getSpeedX() * (1 + levelUp)));
                abstractAircraft.setSpeedY((int) (abstractAircraft.getSpeedY() * (1 + levelUp)));
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
            }
        }
    }
}
