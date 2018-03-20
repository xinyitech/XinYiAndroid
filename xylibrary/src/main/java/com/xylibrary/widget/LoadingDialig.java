package com.xylibrary.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;

import xinyi.com.xylibrary.R;


/**
 * Created by jiajun.wang on 2018/2/25.
 */

public class LoadingDialig extends AlertDialog{
    public LoadingDialig(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading_view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT ;
        getWindow().setAttributes(params);
        setCanceledOnTouchOutside(false);
        setCancelable(true);


    }
}
