package com.example.aircraftwar.RankList;

/**
 * GameData 实体类存储每条游戏数据
 * @author xiao hao
 */
public class GameData {
    /**
     * @param UserName 玩家名字
     * @param GameScore 游戏得分
     * @param GameDateTime 游戏日期时间
     */
    private String playerName;
    private int gameScore;
    private String gameDateTime;

    public GameData(String userName, int gameScore, String gameDateTime){
        this.playerName = userName;
        this.gameScore = gameScore;
        this.gameDateTime = gameDateTime;
    }

    public String getPlayerName(){
        return playerName;
    }

    public int getGameScore(){
        return gameScore;
    }

    public String getGameDateTime(){
        return gameDateTime;
    }

}
