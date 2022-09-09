package com.example.aircraftwar.Game.Application;

import com.example.aircraftwar.Activities.GameActivity;
import com.example.aircraftwar.Game.Aircraft.AbstractAircraft;
import com.example.aircraftwar.Game.Aircraft.HeroAircraft;
import com.example.aircraftwar.Game.AircraftFactory.BossEnemyFactory;
import com.example.aircraftwar.Game.AircraftFactory.EliteEnemyFactory;
import com.example.aircraftwar.Game.AircraftFactory.MobEnemyFactory;

public class CommonGame extends Game{
    CommonGame(boolean ifSoundOpen,int maxHp) {
        super(ifSoundOpen,maxHp);
        enemyMaxNumber = 10;
    }

    protected void timeLevelup(){
        levelUp = levelUp + 0.04;
    }

    protected void createEnemyAircraft(){
        if (enemyAircrafts.size() < enemyMaxNumber)
        {
            if(score - scorePast >= 300 && !bossValid){
                BossEnemyFactory aircraftFactory = new BossEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                abstractAircraft.setSpeedX((int) (abstractAircraft.getSpeedX() * (1 + levelUp)));
                abstractAircraft.setSpeedY((int) (abstractAircraft.getSpeedY() * (1 + levelUp)));
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
                bossValid = true;
                //音乐
                GameActivity.gameBinder.playMusic_BGM_BOSS();
            }
            else if(Math.random() < 0.2) {
                EliteEnemyFactory aircraftFactory = new EliteEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                abstractAircraft.setSpeedX((int) (abstractAircraft.getSpeedX() * (1 + levelUp)));
                abstractAircraft.setSpeedY((int) (abstractAircraft.getSpeedY() * (1 + levelUp)));
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
            }
            else {
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
