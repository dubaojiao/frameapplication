package com.du.frameapplication.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.du.frameapplication.R;

public class MsgDialog extends Dialog {
    Context context;
    String type;

    public TextView dTv;
    public TextView wTv1;
    public TextView wTv2;
    public TextView timeTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context,R.layout.dialog_msg,null);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        dTv = (TextView)view.findViewById(R.id.dTv);
        wTv1 = (TextView)view.findViewById(R.id.wTv1);
        wTv2 = (TextView)view.findViewById(R.id.wTv2);
        timeTv = (TextView)view.findViewById(R.id.timeTv);
    }

    public MsgDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public MsgDialog(@NonNull Context context, String type) {
        super(context);
        this.context = context;
        this.type = type;
    }

    public MsgDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected MsgDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }
}
