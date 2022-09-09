package com.example.aircraftwar.Game.Application;

import com.example.aircraftwar.Activities.GameActivity;
import com.example.aircraftwar.Game.Aircraft.HeroAircraft;

public class EasyGame extends Game{
    EasyGame(boolean ifSoundOpen,int maxHp) {
        super(ifSoundOpen,maxHp);
        enemyMaxNumber = 5;
    }
}
