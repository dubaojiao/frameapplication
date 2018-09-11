package com.du.frameapplication.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.du.frameapplication.R;
import com.du.frameapplication.activity.GluttonousActivity;
import com.du.frameapplication.activity.MulActivity;
import com.du.frameapplication.activity.PelletActivity;
import com.du.frameapplication.activity.SnakeActivity;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class GameFragment extends BaseFragment {

    //定义图标数组
    private int[] imageRes = {
            R.drawable.demo1,R.drawable.demo2,R.drawable.demo3,R.drawable.demo4};
    //定义图标下方的名称数组
    private String[] name = {
            "demo1","demo2","demo3","demo4"};

    @Override
    protected int getLayoutId() {
        return R.layout.game_fragment;
    }

    @Override
    protected void initView(View view) {
        GridView gridview = (GridView)view.findViewById(R.id.gridview);
        int length = imageRes.length;
        //生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", imageRes[i]);//添加图像资源的ID
            map.put("ItemText", name[i]);//按序号做ItemText
            lstImageItem.add(map);
        }
        //生成适配器的ImageItem 与动态数组的元素相对应
        SimpleAdapter saImageItems = new SimpleAdapter(getContext(),
                lstImageItem,//数据来源
                R.layout.game_fragment_item,//item的XML实现
                //动态数组与ImageItem对应的子项
                new String[]{"ItemImage", "ItemText"},
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.img_shoukuan, R.id.txt_shoukuan});
        //添加并且显示
        gridview.setAdapter(saImageItems);
        //添加消息处理
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent ;
                if(position == 0){
                    intent =  new Intent(getActivity(), PelletActivity.class);
                }else if(position == 1){
                    intent = new Intent(getActivity(), SnakeActivity.class);
                }else if(position == 2){
                    intent = new Intent(getActivity(), GluttonousActivity.class);
                }else if(position == 3){
                    intent = new Intent(getActivity(), MulActivity.class);
                }else {
                    intent = new Intent(getActivity(), GluttonousActivity.class);
                }
                getActivity().startActivity(intent);
                Toast.makeText(getContext(),name[position],Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void initData() {

    }

}
