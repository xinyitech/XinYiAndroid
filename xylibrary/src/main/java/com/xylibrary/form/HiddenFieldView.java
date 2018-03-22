package com.xylibrary.form;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;


/**
 * Created by Fracesuit on 2017/8/8.
 */

public class HiddenFieldView extends AppCompatTextView implements IFormField, IHiddenField {
    public HiddenFieldView(FieldView.Builder builder, Context context) {
        super(context);
        init(builder);
    }

    private void init(FieldView.Builder builder) {
        if (builder != null) {
            setText(builder.getValueInitContent());
        }
        hidden();
    }


    @Override
    public String getValue() {
        return getText().toString();
    }

    @Override
    public void setVaule(String value) {
        setText(value);
    }


    @Override
    public void hidden() {
        setVisibility(GONE);
    }
}
