package com.xylibrary.form;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xylibrary.form.FormLayout.ActionFieldType.FIELD_TYPE_ALL;
import static com.xylibrary.form.FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE;
import static com.xylibrary.form.FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE_HIDDLEN;

public class FormLayout extends LinearLayout {

    @IntDef({
            FIELD_TYPE_ALL,
            FIELD_TYPE_VISIBLE,
            FIELD_TYPE_VISIBLE_HIDDLEN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionFieldType {
        int FIELD_TYPE_ALL = 0;//所有的view
        int FIELD_TYPE_VISIBLE = 1;//仅仅可见的view
        int FIELD_TYPE_VISIBLE_HIDDLEN = 2;//仅仅可见和隐藏的view
    }

    public void addFieldView(FieldView.Builder builder) {
        addView(builder.build(getContext()));
    }


    public FormLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);

    }

    /**
     * visibleType  为true 说明只获取可见的  为false  获取所有的
     *
     * @return
     */
    public List<FieldView> getFieldViews(@FormLayout.ActionFieldType int visibleType) {
        List<FieldView> formFields = new ArrayList<>();
        getFieldViewByStep(this, visibleType, formFields);
        return formFields;
    }

    private void getFieldViewByStep(ViewGroup parentView, @ActionFieldType int visibleType, List<FieldView> formFields) {
        for (int i = 0; i < parentView.getChildCount(); i++) {
            View view = parentView.getChildAt(i);
            if (view instanceof FieldView) {
                FieldView fieldView = (FieldView) view;
                if (visibleType == FIELD_TYPE_ALL) {
                    formFields.add(((FieldView) view));
                } else if (visibleType == FIELD_TYPE_VISIBLE && fieldView.getVisibility() == View.VISIBLE) {
                    formFields.add(((FieldView) view));
                } else if (visibleType == FIELD_TYPE_VISIBLE_HIDDLEN && (fieldView.getVisibility() == View.VISIBLE || fieldView.getDataView() instanceof IHiddenField)) {
                    formFields.add(((FieldView) view));
                }
            } else if (view instanceof ViewGroup) {
                getFieldViewByStep((ViewGroup) view, visibleType, formFields);
            }
        }
    }

    public FieldView getFieldViewByName(@NonNull String name, @FormLayout.ActionFieldType int visibleType) {
        for (FieldView fieldView : getFieldViews(visibleType)) {
            if (name.equalsIgnoreCase(fieldView.getFieldName())) {
                return fieldView;
            }
        }
        return null;
    }


    public <T extends FieldView> T getDataViewByName(@NonNull String name, @FormLayout.ActionFieldType int visibleType) {
        for (FieldView fieldView : getFieldViews(visibleType)) {
            if (name.equalsIgnoreCase(fieldView.getFieldName())) {
                return (T) fieldView.getDataView();
            }
        }
        return null;
    }


    /**
     * 检查表单样式
     */
    public boolean checkForm(@FormLayout.ActionFieldType int visibleType) {
        for (FieldView fieldView : getFieldViews(visibleType)) {
            if (fieldView.isMustInput()) {
                if (TextUtils.isEmpty(fieldView.getValue())) {
                    final String warnMessager = fieldView.getWarnMessager();
                    final String labelName = fieldView.getLabelName();
                    String warn = null;
                    if (!StringUtils.isEmpty(warnMessager)) {
                        warn = warnMessager;
                    } else if (!StringUtils.isEmpty(labelName)) {
                        warn = labelName + "不得为空";
                        Toast.makeText(getContext(), warn, Toast.LENGTH_SHORT).show();
                    } else {
                        warn = "请确认表单,有必填项没有填写完成";
                    }
                    Toast.makeText(getContext(), warn, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    public void bindData(Object datas, @FormLayout.ActionFieldType int visibleType) {
        bindData(datas, "", visibleType);
    }


    public void bindData(Object datas, String prefix, @FormLayout.ActionFieldType int visibleType) {
        for (FieldView fieldView : getFieldViews(visibleType)) {
            try {
                Field field = datas.getClass().getDeclaredField(fieldView.getFieldName().replace(prefix, ""));
                field.setAccessible(true);
                String value = String.valueOf(field.get(datas));
                value = StringUtils.isEmpty(value) ? "" : value;
                fieldView.setValue(value);
            } catch (Exception e) {
                e.printStackTrace();
                fieldView.setValue("");
            }
        }
    }

    public void bindData(Map<String, Object> params, @FormLayout.ActionFieldType int visibleType) {
        bindData(params, "", visibleType);
    }

    public void bindData(Map<String, Object> params, String prefix, @FormLayout.ActionFieldType int visibleType) {
        for (FieldView FieldView : getFieldViews(visibleType)) {
            try {
                String value = (String) params.get(prefix + FieldView.getFieldName());
                value = StringUtils.isEmpty(value) ? "" : value;
                FieldView.setValue(value);
            } catch (Exception e) {
                e.printStackTrace();
                FieldView.setValue("");
            }
        }
    }

    public void reset(@FormLayout.ActionFieldType int visibleType) {
        for (FieldView fieldView : getFieldViews(visibleType)) {
            try {
                fieldView.setValue("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public Map<String, Object> getParams(String prefix, @FormLayout.ActionFieldType int visibleType) {
        Map<String, Object> map = new HashMap<>();
        for (FieldView fieldView : getFieldViews(visibleType)) {
            map.put(fieldView.getFieldName().replace(prefix, ""), fieldView.getValue());
        }
        return map;
    }

    public Map<String, Object> getParams(@FormLayout.ActionFieldType int visibleType) {
        return getParams("", visibleType);
    }

    public <T> T getParams(Class<T> clazz, @FormLayout.ActionFieldType int visibleType, String prefix) {
        JSONObject jsonObject = new JSONObject(getParams(prefix, visibleType));
        return jsonObject.toJavaObject(clazz);
    }

    public <T> T getParams(Class<T> clazz, @FormLayout.ActionFieldType int visibleType) {
        return getParams(clazz, visibleType, "");
    }


}
