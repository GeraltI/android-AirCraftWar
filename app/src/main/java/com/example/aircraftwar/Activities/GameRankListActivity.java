package com.example.aircraftwar.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aircraftwar.R;
import com.example.aircraftwar.RankList.GameData;
import com.example.aircraftwar.RankList.GameDataDaoImpl;
import com.example.aircraftwar.RankList.RankListAdapter;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class GameRankListActivity extends AppCompatActivity {

    Button DeleteButton;
    TextView GameLevelText;
    ListView GameRankListView;
    private RankListAdapter rankListAdapter;
    private boolean canDelete = false;
    private GameDataDaoImpl gameDataDaoImpl;
    private List<GameData> gameDataList;
    private GameData gameData;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//隐藏任务栏
        setContentView(R.layout.activity_game_rank_list);

        ExitApplication.getInstance().addActivity(GameRankListActivity.this);

        initDatas();

        GameLevelText = (TextView) findViewById(R.id.GameLevelText);

        Intent intent = getIntent();
        switch(intent.getStringExtra("gameLevel")){
            case"easy":
                GameLevelText.setText("简单排行榜");
                break;
            case"common":
                GameLevelText.setText("普通排行榜");
                break;
            case"difficult":
                GameLevelText.setText("困难排行榜");
                break;
        }

        GameRankListView = (ListView) findViewById(R.id.GameRankListView);
        rankListAdapter = new RankListAdapter(GameRankListActivity.this, R.layout.game_rank_list_item,gameDataList);
        GameRankListView.setAdapter(rankListAdapter); // 为listView设置适配器

        GameRankListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gameData = (GameData) gameDataList.get(position); // 获取点击的View 中包含的Color 对象
                Toast.makeText(GameRankListActivity.this, "排名：" + (position + 1) + "  名字：" + gameData.getPlayerName() + "  游戏得分：" + gameData.getGameScore() + "\n   游戏时间：" + gameData.getGameDateTime(),
                        Toast.LENGTH_SHORT).show();
                canDelete = true;
            }
        });

        //删除按钮
        DeleteButton = findViewById(R.id.DeleteButton);
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canDelete){
                    gameDataList.remove(gameData);
                    GameRankListView.setAdapter(rankListAdapter); // 为listView设置适配器
                    gameDataDaoImpl.doDelete(gameData);
                    gameDataDaoImpl.setAllGameDatas();
                }
                canDelete = false;
            }
        });
    }

    private void initDatas(){
        Intent intent = getIntent();
        gameDataDaoImpl = new GameDataDaoImpl(intent.getStringExtra("gameLevel"));
        gameDataList = gameDataDaoImpl.getAllGameDatas();
        gameDataList.sort(Comparator.comparing(GameData::getGameScore, Comparator.reverseOrder()));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(GameRankListActivity.this,GameSelectActivity.class);
            startActivity(intent);//转到单机游戏选择界面中
        }
        return true;
    }
}