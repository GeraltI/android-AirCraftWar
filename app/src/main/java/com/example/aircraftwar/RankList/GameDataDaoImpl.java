package com.example.aircraftwar.RankList;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.Files.readAllLines;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.aircraftwar.Activities.GameActivity;
import com.example.aircraftwar.Activities.GameSelectActivity;
import com.example.aircraftwar.Game.Application.CommonGame;
import com.example.aircraftwar.Game.Application.DifficultGame;
import com.example.aircraftwar.Game.Application.EasyGame;
import com.example.aircraftwar.Game.Application.ImageManager;


/**
 * 实现游戏数据访问对象DAO接口的实现类
 * @author xiao hao
 */
public class GameDataDaoImpl implements GameDataDao{
    private Path path;
    private final List<GameData> gameDatas;
    private List<String> lines;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SdCardPath")
    public GameDataDaoImpl(String gameLevel) {

        switch (gameLevel){
            case"easy":
                path = Paths.get("/data/data/com.example.aircraftwar/gameData/EasyGameDatas.txt");
                break;
            case"common":
                path = Paths.get("/data/data/com.example.aircraftwar/gameData/CommonGameDatas.txt");
                break;
            case"difficult":
                path = Paths.get("/data/data/com.example.aircraftwar/gameData/DifficultGameDatas.txt");
                break;
        }
        File file = new File(String.valueOf(path));
        File parentFile = new File(file.getParent());
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        gameDatas = new ArrayList<>();
        try {
            if (path != null) {
                lines = Files.readAllLines(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (lines != null) {
            for (String str : lines) {
                String[] datas = str.split("%");
                if(datas.length !=3){
                    break;
                }
                gameDatas.add(new GameData(datas[0],Integer.parseInt(datas[1]),datas[2]));
            }
        }
    }

    @Override
    public List<GameData> getAllGameDatas() {
        return gameDatas;
    }

    @Override
    public void doAdd(String userName,int gameScore,String gameDateTime) {
        gameDatas.add(new GameData(userName,gameScore,gameDateTime));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void doDelete(GameData gameData) {
        gameDatas.remove(gameData);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setAllGameDatas() {
        lines = new ArrayList<>();
        for(GameData gameData : gameDatas){
            String s = new String(gameData.getPlayerName() + "%" + gameData.getGameScore() + "%" + gameData.getGameDateTime());
            lines.add(s);
        }
        try {
            Files.write(path, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
