package com.example.aircraftwar.Game.BombPropPublish;

import java.util.LinkedList;
import java.util.List;

public class BombPropPublisher {
    public int scoreChange;
    private volatile static BombPropPublisher bombPropPublisher;
    private BombPropPublisher(){

    }
    public static BombPropPublisher getBombPropPublisher() {
        if (bombPropPublisher == null) {
            synchronized (BombPropPublisher.class) {
                if (bombPropPublisher == null) {
                    bombPropPublisher = new BombPropPublisher();
                }
            }
        }
        return bombPropPublisher;
    }

    //观察者列表

    private final List<BombPropSubscriber> bombPropSubscriberList = new LinkedList<>();

    //增加观察者

    public void addBombPropSubscriber(BombPropSubscriber bombPropSubscriber) {
        bombPropSubscriberList.add(bombPropSubscriber);
    }

    //删除观察者

    public void removeBombPropSubscriber(BombPropSubscriber bombPropSubscriber) {
        bombPropSubscriberList.remove(bombPropSubscriber);
    }

    //通知所有观察者

    public void notifyAllBombPropSubscribers() {
        for (BombPropSubscriber bombPropSubscriber : bombPropSubscriberList) {
            bombPropSubscriber.response();
        }
    }

    //炸弹道具生效

    public void bombPropEffect() {
        scoreChange = 0;
        notifyAllBombPropSubscribers();
    }
}
