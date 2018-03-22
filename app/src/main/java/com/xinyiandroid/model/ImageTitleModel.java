package com.xinyiandroid.model;

import java.io.Serializable;

/**
 * Created by Fracesuit on 2017/7/21.
 */

public class ImageTitleModel implements Serializable {
    private int drawableId;
    private String title;
    private Class clazz;
    private String iconPath;//图片路径
    private Object data;//方便携带数据

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ImageTitleModel(int drawableId, String title) {
        this.drawableId = drawableId;
        this.title = title;
    }

    public ImageTitleModel() {
    }


    public String getIconPath() {
        return iconPath;
    }

    public ImageTitleModel setIconPath(String iconPath) {
        this.iconPath = iconPath;
        return this;
    }


    public int getDrawableId() {
        return drawableId;
    }

    public ImageTitleModel setDrawableId(int drawableId) {
        this.drawableId = drawableId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ImageTitleModel setTitle(String title) {
        this.title = title;
        return this;
    }


    public Class getClazz() {
        return clazz;
    }

    public ImageTitleModel setClazz(Class clazz) {
        this.clazz = clazz;
        return this;
    }

}
