package com.du.frameapplication.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.du.frameapplication.R;
import com.du.frameapplication.activity.AppActivity;

import java.util.List;

public class ViewAdapter extends PagerAdapter {
    private List<View> datas;
    private Activity mContext;
    public ViewAdapter(List<View> list,Activity context) {
        mContext = context;
        datas=list;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=datas.get(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.i("ViewAdapter",view.toString());
                //mContext.startActivity(new Intent(mContext, AppActivity.class));



            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(datas.get(position));
    }

}