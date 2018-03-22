package com.xylibrary.form;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;

/**
 * Created by Fracesuit on 2017/8/8.
 */

public class FieldRadioButton extends AppCompatRadioButton implements IFormField {
    String value;


    public FieldRadioButton(Context context, String text, String value) {
        super(context);
        setText(text);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setVaule(String value) {
        this.value = value;
    }
}
