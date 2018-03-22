package com.xylibrary.form;

/**
 * Created by Fracesuit on 2017/8/17.
 */

public class DictField {
    private String name;
    private String value;
    private int type;
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return name;
    }

    public DictField() {
    }

    public DictField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public DictField setType(int type) {
        this.type = type;
        return this;
    }
}
