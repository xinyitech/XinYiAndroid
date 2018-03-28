package com.xinyiandroid.ui;

import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.fingdo.statelayout.StateLayout;
import com.xinyiandroid.presenter.main.MainPresenter;
import com.xymaplibrary.base.BaseLocationActivity;
import com.xymaplibrary.modle.LocaionInfo;

import butterknife.Bind;
import butterknife.OnClick;
import xinyi.com.xinyiandroid.R;

public class OkgoHttpRequestActivity extends BaseLocationActivity<MainPresenter> {


    @Bind(R.id.btnRequest)
    Button btnRequest;
    @Bind(R.id.mStateLayout)
    StateLayout mStateLayout;

    @Override
    public MainPresenter inject() {
        return new MainPresenter();
    }

    @Override
    public void initView() {

    }

    @Override
    public void receiveLocation(LocaionInfo location, boolean isSuccess) {
        if (isSuccess) {
            ToastUtils.showShort(JSON.toJSONString(location));
        }
    }


    @Override
    public int getLayoutResource() {
        return R.layout.activity_statelayout;
    }

    @OnClick(R.id.btnRequest)
    public void onViewClicked() {
        mPresenter.Login("xyg_albc", "a123456", "ZHS");
    }


}
