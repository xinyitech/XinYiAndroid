package com.xinyiandroid.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import xinyi.com.xinyiandroid.R;


/**
 * Created by Fracesuit on 2017/7/28.
 */

public class ImageLoaderUtils {
    private static int placeHolderIcon = 0;
    private static int errorIcon = 0;
    private static int fallbackIcon = 0;


    public static void init(int defaultIcon) {
        placeHolderIcon = defaultIcon;
        errorIcon = defaultIcon;
        fallbackIcon = defaultIcon;
    }

    public static void init(int placeHolderIcon, int errorIcon, int fallbackIcon) {
        ImageLoaderUtils.placeHolderIcon = placeHolderIcon;
        ImageLoaderUtils.errorIcon = errorIcon;
        ImageLoaderUtils.fallbackIcon = fallbackIcon;
    }

    public static void showImage(ImageView imageView, String imgUrl) {
        showImage(imageView, imgUrl, 0, null);
    }

    public static void showImage(ImageView imageView, String imgUrl, int defaultIcon) {
        showImage(imageView, imgUrl, defaultIcon, null);
    }

    public static void showImage(ImageView imageView, String imgUrl, int defaultIcon, RequestOptions requestOptions) {
        if (requestOptions == null) {
            requestOptions = new RequestOptions();
        }
        requestOptions.placeholder(defaultIcon == 0 ? placeHolderIcon == 0 ? R.mipmap.ic_launcher : placeHolderIcon : defaultIcon)
                .error(defaultIcon == 0 ? errorIcon == 0 ? R.mipmap.ic_launcher : errorIcon : defaultIcon)
                .fallback(defaultIcon == 0 ? fallbackIcon == 0 ? R.mipmap.ic_launcher : fallbackIcon : defaultIcon);
        Glide.with(imageView)
                .load(imgUrl)
                .apply(requestOptions)
                .into(imageView);
    }


}
