package com.du.frameapplication.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.du.frameapplication.R;

public class InputDialog extends Dialog {
    Context context;
    String type;

    public AutoCompleteTextView pwd;
    public Button qBtn;
    public Button close;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context,R.layout.dialog_input,null);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        pwd = (AutoCompleteTextView)view.findViewById(R.id.pwd);
        qBtn = (Button) view.findViewById(R.id.qBtn);
        close = (Button) view.findViewById(R.id.close);
        /*qBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = pwd.getText().toString();
                if(p == null || p.equals("")){
                    Toast.makeText(context,"请输入房间密码",Toast.LENGTH_SHORT).show();
                }else {

                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });*/
    }

    public InputDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public InputDialog(@NonNull Context context,String type) {
        super(context);
        this.context = context;
        this.type = type;
    }

    public InputDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected InputDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }
}
