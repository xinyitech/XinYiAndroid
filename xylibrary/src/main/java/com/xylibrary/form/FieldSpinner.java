package com.xylibrary.form;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Fracesuit on 2017/8/17.
 */

public class FieldSpinner extends AppCompatSpinner implements IFormField {

    List<DictField> datas = new ArrayList<>();
    FieldView.Builder builder;

    public FieldSpinner(FieldView.Builder builder, Context context) {
        super(context);
        init(builder);
    }

    private void init(FieldView.Builder builder) {
        this.builder = builder;
        if (builder != null && !StringUtils.isEmpty(builder.getValueInitContent())) {
            String defautSelectValue = null;
            if (builder.getValueInitContent() != null) {
                for (String name_value : builder.getValueInitContent().split(",")) {
                    final String[] split = name_value.split("_");
                    if ("check".equals(split[0])) {
                        defautSelectValue = split[1];
                        continue;
                    }
                    datas.add(new DictField(split[0], split[1]));
                }

                attachDataSource();
                setVaule(defautSelectValue);
            }
        }
        setOnSpinnerItemSelectListener(null);
    }

    @Override
    public String getValue() {
        final int selectedItemPosition = getSelectedItemPosition();
        if (selectedItemPosition > -1) {
            return datas.get(selectedItemPosition).getValue();
        } else {
            return null;
        }

    }

    @Override
    public void setVaule(@NonNull String value) {
        if (value == null) return;
        int i = 0;
        for (DictField data : datas) {
            if (value.equals(data.getValue())) {
                setSelection(i);
                break;
            }
            i++;
        }
    }


    public void setData(List<DictField> datas, String defautSelectValue) {
        this.datas = datas;
        attachDataSource();
        setVaule(defautSelectValue);
    }

    public void setData(List<DictField> datas) {
        setData(datas, null);
    }

    private void attachDataSource() {
        ArrayAdapter<DictField> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, datas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setAdapter(adapter);

    }

    public interface OnSpinnerItemSelectListener {
        void onItemSelected(AdapterView<?> parent, View view, int position, long id);
    }

    public void setOnSpinnerItemSelectListener(final OnSpinnerItemSelectListener onSpinnerItemSelectListener) {
        final OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                if (builder != null) {
                    builder.valueInitContent(null);
                }

                FormInitUtil.initTextView(tv, builder);
                if (onSpinnerItemSelectListener != null) {
                    onSpinnerItemSelectListener.onItemSelected(parent, view, position, id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        super.setOnItemSelectedListener(onItemSelectedListener);
    }
}
