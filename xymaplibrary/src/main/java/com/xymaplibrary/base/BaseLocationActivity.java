package com.xymaplibrary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xylibrary.base.BaseActivity;
import com.xylibrary.base.RxPresenter;
import com.xymaplibrary.modle.LocaionInfo;
import com.xymaplibrary.utils.BaiduMapUtil;

import butterknife.ButterKnife;

/**
 * Created by jiajun.wang on 2018/3/20.
 */

public abstract class BaseLocationActivity<T extends RxPresenter> extends BaseActivity {
    public T mPresenter;

    public LocationClient mLocationClient = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mPresenter=inject();
        if (mPresenter!=null)
            mPresenter.attachView(this);
        initView();
        initMap();
    }

    private void initMap() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(0);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setWifiCacheTimeOut(5*60*1000);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                receiveLocation(BaiduMapUtil.bdLocation2LocationInfo(bdLocation));
            }
        });
        mLocationClient.start();
    }


    public abstract T  inject();
    public abstract void initView();
    public abstract void receiveLocation(LocaionInfo location);
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
