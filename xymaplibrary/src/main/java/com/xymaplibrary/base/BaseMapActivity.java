package com.xymaplibrary.base;


import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
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
 */

public abstract class BaseMapActivity<T extends RxPresenter> extends BaseLocationActivity {

    public MapView mMapView;
    public BaiduMap mBaiduMap;
    public GeoCoder mGeoCoder;

    @Override
    public void initView() {
        initMap();
    }

    public void initMap() {
        mMapView = findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//普通模式的
        LatLng cenpt = new LatLng(22.61667, 114.06667);
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(19)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);//
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // 反Geo搜索
                reverseGeoCode(latLng);
            }
            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                reverseGeoCode(mapPoi.getPosition());
                return false;
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
    public void receiveLocation(LocaionInfo location, boolean isSusscee) {
        if (isSusscee) {
            move2Position(location);
        } else {
            ToastUtils.showShort("");
        }
    }

    //绘制标志物，并且让地图移动到对应的位置
    public void move2Position(LocaionInfo location) {
        LatLng point = new LatLng(location.getLat(), location.getLongt());
        View view = View.inflate(getApplicationContext(), R.layout.show_position, null);
        TextView address = view.findViewById(R.id.tvAddress);
        address.setText(location.getAddress());
        InfoWindow mInfoWindow = new InfoWindow(view, point, -47);
        mBaiduMap.showInfoWindow(mInfoWindow);

        mMapView.requestFocus();
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(point);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }


    //根据经纬度反编译得到地址
    public void reverseGeoCode(LatLng latLng) {
        if (mGeoCoder==null)
            mGeoCoder= GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            }
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    ToastUtils.showShort("抱歉，未能找到结果");
                    return;
                }
                mBaiduMap.clear();
                LocaionInfo locaionInfo = new LocaionInfo();
                locaionInfo.setAddress(reverseGeoCodeResult.getAddress()+reverseGeoCodeResult.getSematicDescription());
                locaionInfo.setLat(reverseGeoCodeResult.getLocation().latitude);
                locaionInfo.setLongt(reverseGeoCodeResult.getLocation().longitude);
                move2Position(locaionInfo);
            }
        });

        boolean bl=mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLng).newVersion(1));
        if (!bl){
            ToastUtils.showShort("请重新选取位置");
            return;
        }
    }

}
