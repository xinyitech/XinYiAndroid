package com.xymaplibrary.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xylibrary.widget.LoadingDialig;
import com.xymaplibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiajun.wang on 2018/4/4.
 * 下载离线地图
 */

public class OfflineMapActivity extends AppCompatActivity implements MKOfflineMapListener {

    private CityAdapter cityAdapter;
    private TextView centerTitle;
    private RecyclerView mRecyclerView;
    private String[] citys;
    private MKOfflineMap mkOfflineMap;
    private ArrayList<MKOLSearchRecord> arrayList;
    private ArrayList<ItemData> dataSources = new ArrayList<>();
    private LoadingDialig mLoadingDialig;
    private int i;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_map);
        centerTitle = findViewById(R.id.centerTitle);
        mLoadingDialig=new LoadingDialig(this);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        centerTitle.setText("离线地图管理");
        citys = getResources().getStringArray(R.array.citys);
        String cityString = "";
        for (String s : citys
                ) {
            cityString = cityString + s;
        }
        mkOfflineMap = new MKOfflineMap();
        mkOfflineMap.init(this);
        arrayList = mkOfflineMap.getHotCityList();
        for (MKOLSearchRecord mMKOLSearchRecord : arrayList
                ) {
            if (mMKOLSearchRecord != null) {
                if (cityString.contains(mMKOLSearchRecord.cityName)) {
                    ItemData itemData = new ItemData(mMKOLSearchRecord, 0);
                    dataSources.add(itemData);
                }
            }
        }
        cityAdapter = new OfflineMapActivity.CityAdapter(R.layout.item_offline_map, dataSources);
        mRecyclerView.setAdapter(cityAdapter);

       mLoadingDialig.setOnKeyListener(new DialogInterface.OnKeyListener() {
           @Override
           public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
               if (event.getAction()==KeyEvent.KEYCODE_BACK){
                   mkOfflineMap.remove(i);
               }
               return false;
           }
       });
    }

    @Override
    public void onGetOfflineMapState(int i, int i1) {
        mLoadingDialig.dismiss();
        switch (i) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
                //离线地图下载更新事件类型
                MKOLUpdateElement update = mkOfflineMap.getUpdateInfo(i1);//i1 表city id
                for (int n = 0; n < dataSources.size(); n++) {
                    if (update.cityID == dataSources.get(n).getmMKOLSearchRecord().cityID) {
                        dataSources.get(n).setProcess(update.ratio);
                        cityAdapter.notifyDataSetChanged();
                    }
                }
                break;

            case MKOfflineMap.TYPE_NEW_OFFLINE:
                //有新离线地图安装
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                //版本更新提示
                break;
            case MKOfflineMap.TYPE_NETWORK_ERROR:
                ToastUtils.showShort("网络异常");
                break;
        }
    }


    public class CityAdapter extends BaseQuickAdapter<ItemData, BaseViewHolder> {

        public CityAdapter(int layoutResId, @Nullable List<ItemData> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final ItemData item) {
            TextView city = helper.getView(R.id.tvCity);
            TextView tvState = helper.getView(R.id.tvState);
            city.setText(item.getmMKOLSearchRecord().cityName);
            final Button btDown=helper.getView(R.id.btDown);
            helper.setText(R.id.btDown, "下载");
            String ret = "";
            if ((item.getmMKOLSearchRecord().dataSize) < (1024 * 1024)) {
                ret = String.format("%dK", (item.getmMKOLSearchRecord().dataSize) / 1024);
            } else {
                ret = String.format("%.1fM", (item.getmMKOLSearchRecord().dataSize) / (1024 * 1024.0));
            }
            tvState.setText(ret);
            MKOLUpdateElement update = mkOfflineMap.getUpdateInfo(item.getmMKOLSearchRecord().cityID);//i1 表city id
            if (update != null) {
                item.setProcess(update.ratio);
            }
            if (item.getProcess() >= 0&&item.getProcess()<100) {
                helper.setText(R.id.btDown, item.getProcess() == 0 ? "下载" : "" + item.getProcess() + "%");
                helper.setOnClickListener(R.id.btDown, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLoadingDialig.show();
                        i=item.getmMKOLSearchRecord().cityID;

                        if ("更新".equals(btDown.getText().toString())){
                            mkOfflineMap.update(item.getmMKOLSearchRecord().cityID);
                        }else {
                            mkOfflineMap.start(item.getmMKOLSearchRecord().cityID);
                        }
                        mkOfflineMap.start(item.getmMKOLSearchRecord().cityID);
                    }
                });
            } else if (item.getProcess() == 100) {
                helper.getView(R.id.btDown).setOnClickListener(null);
                helper.setText(R.id.btDown, "已下载");
                if (update.update){
                    helper.setText(R.id.btDown, "更新");
                }
            }
        }
    }

    public class ItemData {
        private MKOLSearchRecord mMKOLSearchRecord;
        private int process;

        public ItemData(MKOLSearchRecord mMKOLSearchRecord, int process) {
            this.mMKOLSearchRecord = mMKOLSearchRecord;
            this.process = process;
        }

        public MKOLSearchRecord getmMKOLSearchRecord() {
            return mMKOLSearchRecord;
        }

        public void setmMKOLSearchRecord(MKOLSearchRecord mMKOLSearchRecord) {
            this.mMKOLSearchRecord = mMKOLSearchRecord;
        }

        public int getProcess() {
            return process;
        }

        public void setProcess(int process) {
            this.process = process;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mkOfflineMap.destroy();
    }
}
