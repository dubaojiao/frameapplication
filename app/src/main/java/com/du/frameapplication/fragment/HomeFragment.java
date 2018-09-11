package com.du.frameapplication.fragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.du.frameapplication.R;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends BaseFragment {
    private CircleRefreshLayout mRefreshLayout;
    TextView  home_text;
    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initView(View view) {
        mRefreshLayout = (CircleRefreshLayout) view.findViewById(R.id.refresh_layout);
        home_text = (TextView)view.findViewById(R.id.home_text);
        initEvent();
    }
    @Override
    protected void initData() {

    }

    private void initEvent(){
        mRefreshLayout.setOnRefreshListener(
                new CircleRefreshLayout.OnCircleRefreshListener() {
                    @Override
                    public void refreshing() {
                        // do something when refresh starts
                        Toast.makeText(getContext(),"refreshing",Toast.LENGTH_SHORT).show();
                        Log.i("HomeFragment","refreshing");
                        stop();
                    }

                    @Override
                    public void completeRefresh() {
                        Log.i("HomeFragment","completeRefresh");
                        //Toast.makeText(getContext(),"completeRefresh",Toast.LENGTH_SHORT).show();
                        // do something when refresh complete
                        home_text.setText("数据刷新成功");
                    }
                });
    }

    private void stop(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("HomeFragment","finishRefreshing");
                //Toast.makeText(getContext(),"finishRefreshing",Toast.LENGTH_SHORT).show();
                mRefreshLayout.finishRefreshing();
            }
        },2000);
    }

}
