package com.example.aircraftwar.Game.Application;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.aircraftwar.Activities.GameActivity;
import com.example.aircraftwar.Game.Aircraft.AbstractAircraft;
import com.example.aircraftwar.Game.Aircraft.BossEnemy;
import com.example.aircraftwar.Game.Aircraft.EliteEnemy;
import com.example.aircraftwar.Game.Aircraft.HeroAircraft;
import com.example.aircraftwar.Game.Aircraft.MobEnemy;
import com.example.aircraftwar.Game.AircraftFactory.BossEnemyFactory;
import com.example.aircraftwar.Game.AircraftFactory.EliteEnemyFactory;
import com.example.aircraftwar.Game.AircraftFactory.MobEnemyFactory;
import com.example.aircraftwar.Game.BasicObject.AbstractFlyingObject;
import com.example.aircraftwar.Game.BombPropPublish.BombPropPublisher;
import com.example.aircraftwar.Game.Bullet.BaseBullet;
import com.example.aircraftwar.Game.Prop.AbstractProp;
import com.example.aircraftwar.Game.Prop.BloodProp;
import com.example.aircraftwar.Game.Prop.BombProp;
import com.example.aircraftwar.Game.Prop.BulletProp;

import java.util.LinkedList;
import java.util.List;

public abstract class Game {

    protected boolean ifSoundOpen;
    protected boolean gameOver = false;
    protected double levelUp = 0;//随时间增加难度
    protected final BombPropPublisher bombPropPublisher;

    protected final HeroAircraft heroAircraft;

    protected final List<AbstractAircraft> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected final List<AbstractProp> Props;

    protected boolean bossValid = false;
    protected int scorePast = 0;
    protected int score = 0;
    protected int time = 0;

    protected int enemyMaxNumber;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    protected int cycleTime = 0;
    protected int enemyCycleTime = 0;
    protected int cycleDuration = 20;
    protected int enemyCycleDuration = 30;

    Game(boolean ifSoundOpen,int maxHp){

        gameOver = false;
        bombPropPublisher = BombPropPublisher.getBombPropPublisher();

        heroAircraft = HeroAircraft.getHeroAircraft();
        heroAircraft.setHp(maxHp);
        heroAircraft.setMaxHp(maxHp);

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        Props = new LinkedList<>();

        this.ifSoundOpen = ifSoundOpen;
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void action(float x, float y) {
        heroAircraft.setLocation(x, y);

        // 周期性执行（控制频率）
        time++;
        if (timeCountAndNewCycleJudge()) {
            if(time % (4 * cycleDuration) == 0){
                //增加游戏难度
                timeLevelup();
            }
            // 英雄飞机射出子弹
            heroShootAction();
        }
        if(timeCountAndNewEnemyCycleJudge()){
            // 新敌机产生
            createEnemyAircraft();
            // 敌机射出子弹
            enemyShootAction();
        }
        // 子弹移动
        bulletsMoveAction();

        // 飞机移动
        aircraftsMoveAction();

        // 道具移动
        propsMoveAction();

        // 撞击检测
        crashCheckAction();

        // 后处理
        postProcessAction();

        // 游戏结束检查
        if (heroAircraft.getHp() <= 0) {

            //游戏结束标志
            gameOver = true;

            //音效
            if(ifSoundOpen){
                GameActivity.gameBinder.playGameOver();
            }

            GameActivity.gameBinder.stopMusicService();

            System.out.println("Game Over!");
        }

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += 1;
        if (cycleTime >= cycleDuration && cycleTime - 1 < cycleDuration) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        }
        else {
            return false;
        }
    }

    private boolean timeCountAndNewEnemyCycleJudge() {
        enemyCycleTime += 1;
        if (enemyCycleTime >= enemyCycleDuration / (1 + levelUp)) {
            // 跨越到新的周期
            enemyCycleTime %= enemyCycleDuration / (1 + levelUp);
            return true;
        }
        else {
            return false;
        }
    }

    protected void timeLevelup(){
        levelUp = levelUp + 0;
    }

    protected void createEnemyAircraft(){
        if (enemyAircrafts.size() < enemyMaxNumber)
        {
            if(score - scorePast >= 200 && !bossValid){
                BossEnemyFactory aircraftFactory = new BossEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
                bossValid = true;
                //音乐
                GameActivity.gameBinder.playMusic_BGM_BOSS();
            }
            else if(Math.random() < 0.2) {
                EliteEnemyFactory aircraftFactory = new EliteEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
            }
            else {
                MobEnemyFactory aircraftFactory = new MobEnemyFactory();
                AbstractAircraft abstractAircraft = aircraftFactory.createAircraft();
                bombPropPublisher.addBombPropSubscriber(abstractAircraft);
                enemyAircrafts.add(abstractAircraft);
            }
        }
    }

    private void heroShootAction() {
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
        //音效
        if(ifSoundOpen){
            GameActivity.gameBinder.playBullet();
        }
    }

    private void enemyShootAction(){
        for (AbstractAircraft enemyAircraft: enemyAircrafts) {
            List<BaseBullet> shootBullets = enemyAircraft.shoot();
            enemyBullets.addAll(shootBullets);
            for(BaseBullet shootBullet:shootBullets){
                bombPropPublisher.addBombPropSubscriber(shootBullet);
            }
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction() {
        for (AbstractProp prop : Props) {
            prop.forward();
        }
    }

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄
        for (BaseBullet enemyBullet : enemyBullets) {
            if (enemyBullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(enemyBullet)) {
                // 英雄机撞击到敌机子弹
                // 英雄机损失一定生命值
                heroAircraft.decreaseHp(enemyBullet.getPower());
                enemyBullet.vanish();
                bombPropPublisher.removeBombPropSubscriber(enemyBullet);
            }
        }
        // 英雄子弹攻击敌机
        for (BaseBullet heroBullet : heroBullets) {
            if (heroBullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(heroBullet)) {
                    //音效
                    if(ifSoundOpen){
                        GameActivity.gameBinder.playBulletHit();
                    }
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(heroBullet.getPower());
                    heroBullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // TODO 获得分数，产生道具补给
                        if (enemyAircraft instanceof MobEnemy) {
                            score += 10;
                        }
                        if (enemyAircraft instanceof EliteEnemy) {
                            score += 20;
                            Props.addAll(enemyAircraft.reward());
                        }
                        if (enemyAircraft instanceof BossEnemy) {
                            score += 50;
                            scorePast = score;
                            bossValid = false;
                            Props.addAll(enemyAircraft.reward());
                            //音乐
                            GameActivity.gameBinder.playMusic_BGM();
                        }
                        bombPropPublisher.removeBombPropSubscriber(enemyAircraft);
                    }
                }

                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    bombPropPublisher.removeBombPropSubscriber(enemyAircraft);
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // Todo: 我方获得道具，道具生效
        for (AbstractProp prop : Props) {
            if (prop.notValid()) {
                continue;
            }
            if(heroAircraft.crash(prop)){

                prop.effect();

                if (prop instanceof BloodProp) {
                    //音效
                    if(ifSoundOpen){
                        GameActivity.gameBinder.playGetSupply();
                    }
                }

                else if(prop instanceof BulletProp){
                    //音效
                    if(ifSoundOpen){
                        GameActivity.gameBinder.playGetSupply();
                    }
                }

                else if(prop instanceof BombProp){
                    //音效
                    if(ifSoundOpen){
                        GameActivity.gameBinder.playBombExplosion();
                    }
                    score = score + BombPropPublisher.getBombPropPublisher().scoreChange;
                }

                prop.vanish();

            }
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        Props.removeIf(AbstractFlyingObject::notValid);
        for(AbstractAircraft abstractAircraft:enemyAircrafts){
            if(abstractAircraft.notValid()){
                bombPropPublisher.removeBombPropSubscriber(abstractAircraft);
            }
        }
        for(BaseBullet baseBullet:enemyBullets){
            if(baseBullet.notValid()){
                bombPropPublisher.removeBombPropSubscriber(baseBullet);
            }
        }
    }
}
