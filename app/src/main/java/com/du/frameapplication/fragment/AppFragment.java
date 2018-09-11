package com.du.frameapplication.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.du.frameapplication.R;
import com.du.frameapplication.activity.AppActivity;
import com.du.frameapplication.adapter.ViewAdapter;
import com.du.frameapplication.transformer.GalleryPageTransformer;
import com.du.frameapplication.util.Util;
import com.du.frameapplication.viewPager.ClipViewPager;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class AppFragment extends BaseFragment {
    List<View> imageViews;
    ClipViewPager pager;
    List<Integer> imgIds;
    Button btn;
    public AppFragment(){
        imgIds = new ArrayList<>();
        imgIds.add(R.mipmap.one);
        imgIds.add(R.mipmap.tow);
        imgIds.add(R.mipmap.three);
        imageViews = new ArrayList<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.app_fragment;
    }

    @Override
    protected void initView(View view) {
        ImageView imageView;
        btn = (Button)view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AppActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation
                        (getActivity(), view, "transition_morph_view");
                getActivity().startActivity(intent, options.toBundle());
            }
        });
        for(Integer id:imgIds){
            imageView = new ImageView(getActivity());
            imageView.setImageBitmap(Util.getReverseBitmapById(getActivity(), id, 0.5f));
            //imageView.setImageResource(id);
            imageViews.add(imageView);
        }

        pager=(ClipViewPager) view.findViewById(R.id.view_pager);

        PagerAdapter adapter=new ViewAdapter(imageViews,getActivity());
        pager.setPageTransformer(true, new GalleryPageTransformer());
        pager.setOffscreenPageLimit(3);  // 具体缓存页数自己订吧
        pager.setAdapter(adapter);
        // 将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象
        view.findViewById(R.id.activity_view_pager).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return pager.dispatchTouchEvent(event);
            }
        });
    }
    @Override
    protected void initData() {

    }

}
