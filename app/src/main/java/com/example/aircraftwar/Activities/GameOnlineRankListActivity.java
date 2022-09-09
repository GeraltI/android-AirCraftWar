package com.example.aircraftwar.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aircraftwar.R;
import com.example.aircraftwar.RankList.GameData;
import com.example.aircraftwar.RankList.GameDataDaoImpl;
import com.example.aircraftwar.RankList.RankListAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class GameOnlineRankListActivity extends AppCompatActivity {

    TextView OnlineGameLevelText;
    ListView OnlineGameRankListView;

    private String gameLevel;
    private RankListAdapter rankListAdapter;
    private List<GameData> gameDataList;
    private GameData gameData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//隐藏任务栏
        setContentView(R.layout.activity_game_online_rank_list);

        ExitApplication.getInstance().addActivity(GameOnlineRankListActivity.this);

        initDatas();


        OnlineGameLevelText = (TextView) findViewById(R.id.OnlineGameLevelText);

        switch(gameLevel){
            case"easy":
                OnlineGameLevelText.setText("联网简单排行榜");
                break;
            case"common":
                OnlineGameLevelText.setText("联网普通排行榜");
                break;
            case"difficult":
                OnlineGameLevelText.setText("联网困难排行榜");
                break;
        }

        OnlineGameRankListView = (ListView) findViewById(R.id.GameRankListView);

        while(gameDataList.size() == 0){

        }
        rankListAdapter = new RankListAdapter(GameOnlineRankListActivity.this, R.layout.game_rank_list_item,gameDataList);
        OnlineGameRankListView.setAdapter(rankListAdapter); // 为listView设置适配器

        OnlineGameRankListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gameData = (GameData) gameDataList.get(position); // 获取点击的View 中包含的Color 对象
                Toast.makeText(GameOnlineRankListActivity.this, "排名：" + (position + 1) + "  名字：" + gameData.getPlayerName() + "  游戏得分：" + gameData.getGameScore() + "\n   游戏时间：" + gameData.getGameDateTime(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDatas(){
        Intent intent = getIntent();
        gameLevel = intent.getStringExtra("gameLevel");
        gameDataList = new ArrayList<>();

        new Thread(){
            @Override
            public void run(){
                Log.i("client", "send message to server");
                MainActivity.writer.println(MainActivity.socket.getLocalPort() + " " + gameLevel);
                try {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(MainActivity.socket.getInputStream()));
                    String[] str = reader.readLine().split("_");
                    for(String r:str){
                        String []data = r.split("%");
                        gameDataList.add(new GameData(data[0],Integer.parseInt(data[1]),data[2]));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(GameOnlineRankListActivity.this,GameOnlineRankListSelectActivity.class);
            startActivity(intent);//转到单机游戏选择界面中
        }
        return true;
    }
}