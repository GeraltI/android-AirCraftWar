package com.example.aircraftwar.RankList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.aircraftwar.R;

import java.util.List;

public class RankListAdapter extends ArrayAdapter<GameData> {

    private int resourceId;

    public RankListAdapter(Context context, int textViewResourceid, List<GameData> gameDataList) {
        super(context, textViewResourceid,gameDataList);
        this.resourceId = textViewResourceid;
    }

    /*
     * getView方法用于返回ListView中的item 的视图
     */
    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GameData gameData = (GameData) getItem(position);
        View view;
        /*
         * convertView 参数用于储存之前加载好的布局缓存，如果不为空，那么我们可以直接用这个用来给view赋值
         * 提高ListView的运行效率
         */

        view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        // 获取布局文件中的控件id并且显示对应信息：
        TextView Rank = (TextView) view.findViewById(R.id.Rank);
        Rank.setText(position + 1 + "   ");
        TextView userName = (TextView) view.findViewById(R.id.playerName);
        userName.setText(gameData.getPlayerName() + "   ");
        TextView gameScore = (TextView) view.findViewById(R.id.gameScore);
        gameScore.setText(gameData.getGameScore() +  "   ");
        TextView gameDateTime = (TextView) view.findViewById(R.id.gameDateTime);
        gameDateTime.setText(gameData.getGameDateTime());
        return view;
    }
}
