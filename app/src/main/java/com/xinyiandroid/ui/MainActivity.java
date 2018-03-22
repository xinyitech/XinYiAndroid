package com.xinyiandroid.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyiandroid.adapter.ImageTitleAdapter;
import com.xinyiandroid.model.ImageTitleModel;
import com.xinyiandroid.utils.recycleview.RecyclerViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xinyi.com.xinyiandroid.R;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ImageTitleAdapter<ImageTitleModel> titleAdapter = new ImageTitleAdapter<>();

        List<ImageTitleModel> imageTitleModels = new ArrayList<>();
        imageTitleModels.add(new ImageTitleModel(R.mipmap.ic_launcher, "StateLayoutActivity").setClazz(StateLayoutActivity.class));
        imageTitleModels.add(new ImageTitleModel(R.mipmap.ic_launcher, "FormActivity").setClazz(FormActivity.class));
        imageTitleModels.add(new ImageTitleModel(R.mipmap.ic_launcher, "BdMapActivity").setClazz(BdMapActivity.class));
        titleAdapter.addData(imageTitleModels);

        RecyclerViewHelper.initRecyclerViewG(mRecyclerView, true, titleAdapter, 3);

        titleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImageTitleModel item = titleAdapter.getItem(position);
                ActivityUtils.startActivity(item.getClazz());
            }
        });

    }
}
