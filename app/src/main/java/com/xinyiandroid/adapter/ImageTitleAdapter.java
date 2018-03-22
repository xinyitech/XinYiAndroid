package com.xinyiandroid.adapter;


import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyiandroid.model.ImageTitleModel;
import com.xinyiandroid.utils.ImageLoaderUtils;

import xinyi.com.xinyiandroid.R;


/**
 * Created by Fracesuit on 2017/7/21.
 */

public class ImageTitleAdapter<T extends ImageTitleModel> extends BaseQuickAdapter<T, BaseViewHolder> {

    public ImageTitleAdapter() {
        super(R.layout.comm_item_image_title);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageTitleModel item) {
        helper.setText(R.id.tv_icon_name, item.getTitle());
        helper.setImageResource(R.id.img_icon, item.getDrawableId());
        ImageView img = helper.getView(R.id.img_icon);
        if (StringUtils.isEmpty(item.getIconPath())) {
            img.setImageResource(item.getDrawableId());
        } else {
            ImageLoaderUtils.showImage(img, item.getIconPath());
        }
    }
}
