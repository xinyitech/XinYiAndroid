package com.xylibrary.form;

import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.xylibrary.utils.ResUtils;

/**
 * Created by zhiren.zhang on 2017/11/30.
 */

public class FormInitUtil {

    public static void initTextView(TextView textView, FieldView.Builder builder) {
        if (builder != null) {
            if (!StringUtils.isEmpty(builder.getValueInitContent())) {
                textView.setText(builder.getValueInitContent());
            }
            if (!StringUtils.isEmpty(builder.getEdittextHint())) {
                textView.setHint(builder.getEdittextHint());
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.getValueTextSize());
            textView.setTextColor(ResUtils.getColor(builder.getValueTextColor()));
        }
    }

    public static void initEditText(EditText editText, FieldView.Builder builder) {
        if (builder != null) {
            if (!StringUtils.isEmpty(builder.getValueInitContent())) {
                int length = builder.getValueInitContent().length();
                editText.setText(builder.getValueInitContent());
                editText.setSelection(length);
            }
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.getValueTextSize());
            if (!StringUtils.isEmpty(builder.getEdittextHint())) {
                editText.setHint(builder.getEdittextHint());
            }
            if (builder.getEdittextLine() != -1) {
                editText.setLines(builder.getEdittextLine());
            }
            editText.setTextColor(ContextCompat.getColor(Utils.getApp(), builder.getValueTextColor()));
            editText.setBackground(null);
        }
    }
}
