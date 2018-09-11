package com.du.frameapplication;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.du.frameapplication.other.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener{
    private ViewPager mViewPager;
    private List<View> mViews = new ArrayList<>();
    private List<ImageView> mImageViews = new ArrayList<>();
    private int[]  imgRes = new int[]{
            R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3, R.mipmap.guide4
    };
    private Button mButton;
    private LinearLayout mLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_layout);
        initpoint();
        initImg();
        mViewPager = (ViewPager) findViewById(R.id.guide_viewpager);
        mViewPager.setAdapter(new ViewpagerAdapter());
        mViewPager.setPageTransformer(true,new ScalePageTransformer());
        mViewPager.addOnPageChangeListener(this);
        //mViewPager.setPageTransformer(true,new DepthPageTransformer());
        mButton = (Button) findViewById(R.id.guide_start);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuideActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private void initImg() {
        for (int i = 0; i < imgRes.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.guide_item_view,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.guide_imageview);
            imageView.setBackgroundResource(imgRes[i]);
            mViews.add(view);
        }
    }


    private void initpoint() {
        //获取layout
        mLayout = (LinearLayout) findViewById(R.id.point_ly);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置每一个view即圆点的对左的偏移量
        params.setMargins(15,0,0,0);
        //根据图片多少来确定个数
        for (int i = 0; i < imgRes.length; i++) {

            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.dot_select);
            imageView.setLayoutParams(params); //把上面的控件属性设置到LinearLayout中
            if (i == 0){ //默认第一张为红色圆点
                imageView.setSelected(true);
            }else{
                imageView.setSelected(false);
            }
            //把圆点这个子视图导入我们的LinearLayout里面
            mLayout.addView(imageView);
            mImageViews.add(imageView);//跟着viewpager变换颜色
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //滑动时改变圆点的状态
        for (int i = 0; i < mImageViews.size(); i++) {
            if (i == position){
                mImageViews.get(i).setSelected(true);
            }else{
                mImageViews.get(i).setSelected(false);
            }
        }
        //当为最后一个时，显示button，并隐藏圆点
        if (position == mImageViews.size() -1){
            mLayout.setVisibility(View.GONE);
            mButton.setVisibility(View.VISIBLE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(mButton,"alpha",0f,1f);
            animator.setDuration(1000);
            animator.start();
        }else{
            mLayout.setVisibility(View.VISIBLE);
            mButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    class ViewpagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // return super.instantiateItem(container, position);
            container.addView(mViews.get(position));
            return  mViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container, position, object);
            container.removeView(mViews.get(position));
        }
    }


    class ScalePageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE=0.75f;

        @Override
        public void transformPage(View page, float position) {
            //Log.d("TAG", "<"+page.hashCode()+", "+position+">");
            // out of left screen
            if(position<-1.0f) {
                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);
            }
            // slide left
            else if(position<=0.0f) {
                page.setAlpha(1.0f);
                page.setTranslationX(0.0f);
                page.setScaleX(1.0f);
                page.setScaleY(1.0f);
            }
            // slide right
            else if(position<=1.0f) {
                page.setAlpha(1.0f-position);
                page.setTranslationX(-page.getWidth()*position);
                float scale=MIN_SCALE+(1.0f-MIN_SCALE)*(1.0f-position);
                page.setScaleX(scale);
                page.setScaleY(scale);
            }
            // out of right screen
            else {
                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);
            }
        }
    }
}
