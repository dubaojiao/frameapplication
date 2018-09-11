package com.du.frameapplication.fragment.item;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.du.frameapplication.R;
import com.du.frameapplication.adapter.AppItemFragmentDataAdapter;
import com.du.frameapplication.fragment.BaseFragment;
import com.du.frameapplication.pojo.SysLog;
import com.du.frameapplication.view.NormalDecoration;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AppItemFragment extends BaseFragment {
    private  static  final String TAG = "TabContentFragment";
    private int pos;
    //@BindView(R.id.recycler_view)
    RecyclerView recyclerView;



    private AppItemFragmentDataAdapter adapter;

    public static AppItemFragment getInstance(Activity activity,Integer pos,String text){
        AppItemFragment appItemFragment = new AppItemFragment();
        appItemFragment.pos = pos;
        return appItemFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.app_item_fragment;
    }

    @Override
    protected void initView(View view) {
        final List<SysLog> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            final SysLog sysLog = new SysLog();
            sysLog.setErrorMsg("使用NestedScrollView+ViewPager+RecyclerView+SmartRefreshLayout打造酷炫下拉视差效果并解决各种滑动冲突" + i);
            sysLog.setType(1);
            data.add(sysLog);
        }
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(mActivity, R.color.mainGrayF8), (int) mActivity.getResources().getDimension(R.dimen.one)));

        adapter = new AppItemFragmentDataAdapter(mActivity);
        recyclerView.setAdapter(adapter);
        adapter.addAll(data);
        adapter.setNoMore(R.layout.view_no_more);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(mActivity, "---position---" + position, Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                Log.d(TAG, "----onMoreShow");
                adapter.addAll(data);
            }

            @Override
            public void onMoreClick() {

            }
        });
    }

    @Override
    protected void initData() {

    }
}
