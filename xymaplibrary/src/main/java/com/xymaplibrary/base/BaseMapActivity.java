package com.xymaplibrary.base;


import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.blankj.utilcode.util.ToastUtils;
import com.xylibrary.base.RxPresenter;
import com.xymaplibrary.R;
import com.xymaplibrary.modle.LocaionInfo;

/**
 * Created by jiajun.wang on 2018/3/21.
 * rLayout 是一个布局，
 */

public abstract class BaseMapActivity<T extends RxPresenter> extends BaseLocationActivity {

    public MapView mMapView;
    public BaiduMap mBaiduMap;
    public GeoCoder mGeoCoder;
    public TextView address;
    private RelativeLayout rLayout;

    @Override
    public void initView() {
        initMap();
    }

    public void initMap() {
        rLayout = findViewById(R.id.r_layout);
        rLayout.setVisibility(View.VISIBLE);
        address = findViewById(R.id.tvAddress);
        mMapView = findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//普通模式的
        LatLng cenpt = new LatLng(22.61667, 114.06667);
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(21)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);//
        mBaiduMap.setMyLocationEnabled(false);
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                if (address != null)
                    address.setText("获取地址中...");
            }
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {}
            @Override
            public void onMapStatusChange(MapStatus mapStatus) {}
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                reverseGeoCode(mapStatus.target);//
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_base_map;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView!=null){
            mMapView.onResume();//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
    }


    @Override
    public void receiveLocation(LocaionInfo location, boolean isSusscee) {
        if (isSusscee) {
            move2Position(location);
        } else {
            ToastUtils.showShort("");
        }
    }

    //地图移动到对应的LatLng位置
    public void move2Position(LocaionInfo location) {
        LatLng point = new LatLng(location.getLat(), location.getLongt());
        address.setText(location.getAddress());
        mMapView.requestFocus();
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(point);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    //根据经纬度反编译得到地址
    public void reverseGeoCode(LatLng latLng) {
        if (mGeoCoder == null)
            mGeoCoder = GeoCoder.newInstance();

        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {}
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    ToastUtils.showShort("抱歉，未能找到结果");
                    return;
                }
                address.setText(reverseGeoCodeResult.getAddress());
            }
        });

        boolean bl = mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLng).newVersion(1));
        if (!bl) {
            ToastUtils.showShort("请重新选取位置");
            return;
        }
    }

}
