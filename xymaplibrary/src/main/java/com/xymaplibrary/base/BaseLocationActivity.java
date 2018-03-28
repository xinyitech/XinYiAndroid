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
        initLbs();
    }

    private void initLbs() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(0);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedAddress(true);
        option.SetIgnoreCacheException(false);
        option.setWifiCacheTimeOut(5*60*1000);
        option.setOpenGps(true);//打开Gps
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    receiveLocation(BaiduMapUtil.bdLocation2LocationInfo(location), true);
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    receiveLocation(BaiduMapUtil.bdLocation2LocationInfo(location), true);
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    receiveLocation(BaiduMapUtil.bdLocation2LocationInfo(location), true);
                } else if (location.getLocType() == BDLocation.TypeServerError) { //服务端网络定位失败
                    receiveLocation(BaiduMapUtil.bdLocation2LocationInfo(location),false);
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) { //网络不同导致定位失败，请检查网络是否通畅
                    receiveLocation(BaiduMapUtil.bdLocation2LocationInfo(location),false);
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) { //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                    receiveLocation(BaiduMapUtil.bdLocation2LocationInfo(location),false);
                }
            }
        });
        mLocationClient.start();
    }


    public abstract T  inject();
    public abstract void initView();
    public abstract void receiveLocation(LocaionInfo location, boolean isSuccess);
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
