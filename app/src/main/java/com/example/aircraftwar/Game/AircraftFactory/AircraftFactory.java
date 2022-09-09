package com.example.aircraftwar.Game.AircraftFactory;


import com.example.aircraftwar.Game.Aircraft.AbstractAircraft;

/**
 * 所有种类敌机创建工厂的抽象父类
 * 创建敌机（BOSS, ELITE, MOB）
 *
 * @author me
 */
public interface AircraftFactory {

    /**
     * 创建飞机方法
     * @return AbstractAircraft
     */
    AbstractAircraft createAircraft();
}
