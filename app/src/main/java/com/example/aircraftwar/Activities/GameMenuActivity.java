
package com.example.aircraftwar.Activities;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.DisplayMetrics;
        import android.view.KeyEvent;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import com.example.aircraftwar.R;

        import java.util.Objects;

public class GameMenuActivity extends AppCompatActivity{

    public static int screenWidth;
    public static int screenHeight;

    public static String userName;
    public static String password;
    public static String playerName;
    public static int integral;
    public static int prop1Level;
    public static int prop2Level;
    public static int prop3Level;

    TextView playerNameText;
    Button GameStartButton;
    Button OnlineGameStartButton;
    Button GameMallButton;
    Button GameOnlineRankListButton;

    //活动创建时执行
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//隐藏任务栏
        getScreenHW();
        setContentView(R.layout.activity_game_menu);

        ExitApplication.getInstance().addActivity(GameMenuActivity.this);

        playerNameText = (TextView) findViewById(R.id.playerNameText);
        playerNameText.setText(playerName);

        //单机游戏按钮
        GameStartButton = findViewById(R.id.GameStartButton);
        GameStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();//(当前Activity，目标Activity)
                intent.setClass(GameMenuActivity.this, GameSelectActivity.class);
                startActivity(intent);//转到游戏选择中
            }
        });

        //联网游戏按钮
        OnlineGameStartButton = findViewById(R.id.OnlineGameStartButton);
        OnlineGameStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();//(当前Activity，目标Activity)
                intent.setClass(GameMenuActivity.this, GameOnlineSelectActivity.class);
                startActivity(intent);//转到道具商城中
            }
        });

        //道具商城按钮
        GameMallButton = findViewById(R.id.GameMallButton);
        GameMallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();//(当前Activity，目标Activity)
                intent.setClass(GameMenuActivity.this, GameMallActivity.class);
                startActivity(intent);//转到游戏排行中
            }
        });

        //游戏排行选择按钮
        GameOnlineRankListButton = findViewById(R.id.GameOnlineRankListButton);
        GameOnlineRankListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();//(当前Activity，目标Activity)
                intent.setClass(GameMenuActivity.this, GameOnlineRankListSelectActivity.class);
                startActivity(intent);//转到联网游戏排行中
            }
        });
    }

    public void getScreenHW(){

        //定义DisplayMetrics 对象
        DisplayMetrics dm = new DisplayMetrics();

        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口的宽度
        screenWidth = dm.widthPixels;

        //窗口的高度
        screenHeight = dm.heightPixels;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();//(当前Activity，目标Activity)
            intent.setClass(GameMenuActivity.this, MainActivity.class);
            startActivity(intent);//转到游戏开始界面中
        }
        return true;
    }

}