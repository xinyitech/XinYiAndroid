package com.xymaplibrary.base;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.xylibrary.base.RxPresenter;
import com.xymaplibrary.R;
import com.xymaplibrary.modle.LocaionInfo;

/**
 * Created by jiajun.wang on 2018/3/21.
 */

public abstract class BaseMapActivity <T extends RxPresenter> extends BaseLocationActivity {

    public MapView mMapView;
    public BaiduMap mBaiduMap;

    @Override
    public void initView() {
        initMap();
    }

    public void initMap(){
        mMapView =findViewById(R.id.mapView);
        mBaiduMap= mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//普通模式的土层
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_geo);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,
                mCurrentMarker,0xAAFFFF88,0xAA00FF00));
        mBaiduMap.setMyLocationEnabled(true);//// 开启定位图层
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_base_map;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
    }


    @Override
    public void receiveLocation(LocaionInfo location) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                .direction(100).latitude(location.getPoint().latitude)
                .longitude(location.getPoint().longitude).build();
        mBaiduMap.setMyLocationData(locData);
        mBaiduMap.setMyLocationEnabled(false);
    }
}
