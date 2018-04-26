package com.xinyiandroid.ui.model;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.xymaplibrary.mapapi.clusterutil.clustering.ClusterItem;

import xinyi.com.xinyiandroid.R;



/**
 * 百度地图上一个一个的item
 */
public class ClusterBaiduItem implements ClusterItem {

    //经纬度
    private LatLng mPosition;
    private int iconResouce=R.mipmap.ic_launcher;

    public ClusterBaiduItem(LatLng mPosition) {
        this.mPosition = mPosition;
    }

    public ClusterBaiduItem(LatLng mPosition, int iconResouce) {
        this.mPosition = mPosition;
        this.iconResouce = iconResouce;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }



    public LatLng getmPosition() {
        return mPosition;
    }

    public void setmPosition(LatLng mPosition) {
        this.mPosition = mPosition;
    }

    public int getIconResouce() {
        return iconResouce;
    }

    public void setIconResouce(int iconResouce) {
        this.iconResouce = iconResouce;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_launcher);
    }
}