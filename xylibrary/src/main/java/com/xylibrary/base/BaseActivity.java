package com.xylibrary.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.fingdo.statelayout.StateLayout;
import com.xylibrary.widget.LoadingDialig;

import xinyi.com.xylibrary.R;

/**
 * Created by jiajun.wang on 2018/3/19.
 */

public abstract class BaseActivity extends AppCompatActivity implements com.xylibrary.base.IBaseView {

    @Nullable
    public Toolbar toolBar;

    @Nullable
    public StateLayout mStateLayout;


    public LoadingDialig mLoadingDialig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutResource());
        toolBar = findViewById(R.id.toolBar);
        mStateLayout = findViewById(R.id.mStateLayout);
        mLoadingDialig = new LoadingDialig(this);
    }


    @LayoutRes
    public abstract int getLayoutResource();


    @Override
    public void showNoNetWork() {
        if (mStateLayout != null)
            mStateLayout.showNoNetworkView();
    }

    @Override
    public void showLoading() {
        if (mLoadingDialig != null)
            mLoadingDialig.show();
    }

    @Override
    public void showLoadFail() {
        if (mStateLayout != null)
            mStateLayout.showNoNetworkView();
    }

    @Override
    public void showContentView() {
        if (mStateLayout != null)
            mStateLayout.showContentView();
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialig != null)
            mLoadingDialig.dismiss();
    }

    @Override
    public void showEmptyView() {
        if (mStateLayout != null)
            mStateLayout.showEmptyView();
    }

}
