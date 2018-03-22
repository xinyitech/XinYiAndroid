package com.xylibrary.form;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.blankj.utilcode.util.StringUtils;


/**
 * Created by Fracesuit on 2017/8/8.
 */

public class FieldEditText extends AppCompatEditText implements IFormField {

    public FieldEditText(FieldView.Builder builder, Context context) {
        super(context);
        FormInitUtil.initEditText(this, builder);
    }

    public FieldEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public String getValue() {
        return getText().toString();
    }

    @Override
    public void setVaule(String value) {
        setText(value);
        if (!StringUtils.isEmpty(value)) {
            setSelection(value.length());
        }
    }
}
