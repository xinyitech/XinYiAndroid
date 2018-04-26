package com.xinyiandroid.ui;

import android.graphics.Bitmap;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.xinyiandroid.ui.model.ClusterBaiduItem;
import com.xylibrary.base.RxPresenter;
import com.xymaplibrary.base.BaseMapActivity;
import com.xymaplibrary.mapapi.clusterutil.MarkerManager;
import com.xymaplibrary.mapapi.clusterutil.clustering.Cluster;
import com.xymaplibrary.mapapi.clusterutil.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiajun.wang on 2018/4/25.
 */

public class AddMapMarkActivity extends BaseMapActivity  {


    //聚合管理器
    ClusterManager<ClusterBaiduItem> mClusterManager;
    MarkerManager mMarkerManager;
    List<ClusterBaiduItem> mClusterBaiduItems = new ArrayList<>();
    //当前地图状态
    MapStatus mCurrentMapStatus;
    //当前位置的图片
    Bitmap mCLBitmap;

    @Override
    public RxPresenter inject() {
        return new RxPresenter();
    }

    @Override
    public void initView() {
        super.initView();
        mClusterBaiduItems.add(new ClusterBaiduItem(new LatLng(22.5428895303695,113.94907076522427)));
        mClusterBaiduItems.add(new ClusterBaiduItem(new LatLng(22.53816666,113.94979804191999)));
        mClusterBaiduItems.add(new ClusterBaiduItem(new LatLng(22.54198669,113.94898492)));
        mClusterBaiduItems.add(new ClusterBaiduItem(new LatLng(22.542949955772668,113.95141646489829)));
        mClusterBaiduItems.add(new ClusterBaiduItem(new LatLng( 22.541169408993576,113.94813141106227)));
        mClusterBaiduItems.add(new ClusterBaiduItem(new LatLng(22.54037623,113.94970470373528)));
        mClusterBaiduItems.add(new ClusterBaiduItem(new LatLng(22.541099641645727,113.9480988479445)));
        mClusterBaiduItems.add(new ClusterBaiduItem(new LatLng(22.54199162834515,113.95087404269279)));
        mClusterBaiduItems.add(new ClusterBaiduItem(new LatLng(22.541830644861503,113.94984904853647)));

        mMarkerManager = new MarkerManager(mBaiduMap);//图标管理器
        //聚合与渲染管理器
        mClusterManager = new ClusterManager<>(this, mBaiduMap, mMarkerManager);
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);//mClusterManager 实现了接口所以可以直接这样
        //mBaiduMap.setOnMapLoadedCallback(this);地图加载完成回调
        mClusterManager.addItems(mClusterBaiduItems);//添加等待绘制的model
        mClusterManager.cluster();//开始绘制


        initListener();//添加聚合监听
    }

    private void initListener() {
        mClusterManager.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                String s="onMapStatusChangeStart";
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
                String s="onMapStatusChangeStart";
            }
            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                String s="onMapStatusChange";
            }
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //通过这里转到mClusterManager
                return mMarkerManager.onMarkerClick(marker);
            }
        });

        //单个的点击
        mClusterManager.setOnClusterItemClickListener(item -> {
            Toast.makeText(AddMapMarkActivity.this, ""+item.getmPosition().latitude, Toast.LENGTH_SHORT).show();
            return true;
        });

        //聚合的点击
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<ClusterBaiduItem>() {
            @Override
            public boolean onClusterClick(Cluster<ClusterBaiduItem> cluster) {
                ClusterOnClick(cluster);
                return true;
            }
        });
    }

    /**
     * 聚合点击
     */
    private void ClusterOnClick(Cluster<ClusterBaiduItem> clusterBaiduItems) {
        if (mBaiduMap == null) {
            return;
        }
        if (clusterBaiduItems.getItems().size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (ClusterBaiduItem clusterBaiduItem : clusterBaiduItems.getItems()) {
                builder.include(clusterBaiduItem.getPosition());
            }
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁对应的bitmap
        if (mCLBitmap != null && !mCLBitmap.isRecycled()) {
            mCLBitmap.recycle();
        }
        mClusterManager.clearItems();
        mBaiduMap.clear();
        //;销毁地图
        mMapView.onDestroy();
    }
}
