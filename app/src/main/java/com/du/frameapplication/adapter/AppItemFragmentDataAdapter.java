package com.du.frameapplication.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.du.frameapplication.R;
import com.du.frameapplication.pojo.SysLog;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

public class AppItemFragmentDataAdapter extends RecyclerArrayAdapter<SysLog> {
    public AppItemFragmentDataAdapter(Context context) {
        super(context);
    }

    @Override
    public int getViewType(int position) {
        final int type = getItem(position).getType();
        if (type == 0) {
            return 0;
        }
        if (type == 1) {
            return 1;
        }
        if (type == 2) {
            return 2;
        }
        return -1;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new DynamicArticleHolder(parent);
    }

    public class DynamicArticleHolder extends BaseViewHolder<SysLog> {

        TextView textView;

        public DynamicArticleHolder(ViewGroup parent) {
            super(parent, R.layout.app_item_fragment_data);
            textView = $(R.id.tv_name);
        }

        @Override
        public void setData(SysLog data) {
            super.setData(data);
            textView.setText(data.getErrorMsg());
        }
    }
}