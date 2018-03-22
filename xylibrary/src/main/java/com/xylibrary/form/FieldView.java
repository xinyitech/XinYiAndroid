package com.xylibrary.form;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.Utils;

import butterknife.ButterKnife;
import xinyi.com.xylibrary.R;

/**
 * Created by zhiren.zhang on 2018/3/16.
 */

public class FieldView extends FrameLayout {

    public static final int TYPE_EDITTEXT = 1;
    public static final int TYPE_TEXTVIEW = 2;
    public static final int TYPE_RADIOGROUP = 3;
    public static final int TYPE_SPINNER = 4;
    public static final int TYPE_HIDDENVIEW = 5;

    Context context;

    Builder builder;
    private AppCompatTextView tv_label;

    public static Builder newBuilder() {
        return new Builder();
    }

    public FieldView(@NonNull Context context) {
        this(null, context);
    }

    public FieldView(Builder builder, Context context) {
        super(context, null);
        this.builder = builder;
        init(context, null);
    }


    public FieldView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public void init(Context context, AttributeSet attrs) {
        this.context = context;
        initAttributeSet(attrs);
        initView();
    }

    private void initAttributeSet(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FieldView);
            builder = new Builder()
                    .labelName(ta.getText(R.styleable.FieldView_comm_label_name))
                    .fieldName(ta.getString(R.styleable.FieldView_comm_field_name))
                    .valueInitContent(ta.getString(R.styleable.FieldView_comm_init_content))
                    .warnMessager(ta.getString(R.styleable.FieldView_comm_warnMessager))
                    .mustInput(ta.getBoolean(R.styleable.FieldView_comm_must, false))
                    .lineHalfShow(ta.getBoolean(R.styleable.FieldView_comm_line_half_show, false))
                    .showMustInput(ta.getBoolean(R.styleable.FieldView_comm_showMustInput, true))
                    .valueViewType(ta.getInteger(R.styleable.FieldView_comm_contentType, -1))
                    .textIcon(ta.getResourceId(R.styleable.FieldView_comm_textIcon, -1))
                    .fieldIndex(ta.getInteger(R.styleable.FieldView_comm_field_index, -1))
                    .edittextHint(ta.getString(R.styleable.FieldView_comm_edittext_hint))
                    .edittextLine(ta.getInt(R.styleable.FieldView_comm_edittext_line, 1))
                    .fieldDivided(ta.getBoolean(R.styleable.FieldView_comm_fieldDivided, false))
                    .formDivided(ta.getBoolean(R.styleable.FieldView_comm_formDivided, true))
                    .formHorizontal(ta.getBoolean(R.styleable.FieldView_comm_formHorizontal, true))
                    .labelWithMustIcon(ta.getBoolean(R.styleable.FieldView_comm_labelWithMustIcon, false))
                    .labelTextSize(ta.getDimensionPixelSize(R.styleable.FieldView_comm_labelTextSize, SizeUtils.sp2px(14)))
                    .valueTextSize(ta.getDimensionPixelSize(R.styleable.FieldView_comm_valueTextSize, SizeUtils.sp2px(14)))
                    .labelWidth(ta.getDimensionPixelSize(R.styleable.FieldView_comm_labelWidth, -1))
                    .labelTextColor(ta.getResourceId(R.styleable.FieldView_comm_labelTextColor, R.color.comm_black))
                    .labelBgColor(ta.getResourceId(R.styleable.FieldView_comm_labelBgColor, android.R.color.transparent))
                    .fieldViewBgColor(ta.getResourceId(R.styleable.FieldView_comm_fieldViewBgColor, R.color.comm_white))
                    .dividedColor(ta.getResourceId(R.styleable.FieldView_comm_dividedColor, R.color.comm_grey200))
                    .valueTextColor(ta.getResourceId(R.styleable.FieldView_comm_valueTextColor, R.color.comm_grey500))
                    .valueBgColor(ta.getResourceId(R.styleable.FieldView_comm_valueBgColor, android.R.color.transparent));
            ta.recycle();
        }
    }

    private void initView() {

        View fieldView = View.inflate(context, R.layout.comm_view_form_field, this);

        LinearLayout ll_root = ButterKnife.findById(fieldView, R.id.ll_root);
        LinearLayout ll_field = ButterKnife.findById(fieldView, R.id.ll_field);

        LinearLayout ll_container = ButterKnife.findById(fieldView, R.id.ll_container);
        View line_half = ButterKnife.findById(fieldView, R.id.line_half);
        ImageView img_icon = ButterKnife.findById(fieldView, R.id.img_icon);
        ImageView img_must = ButterKnife.findById(fieldView, R.id.img_must);
        View line_field = ButterKnife.findById(fieldView, R.id.line_field);
        View line_form = ButterKnife.findById(fieldView, R.id.line_form);


        //root背景
        ll_field.setBackgroundResource(builder.fieldViewBgColor);
        ll_container.setBackgroundResource(builder.valueBgColor);

        //label
        tv_label = new AppCompatTextView(context);
        if (builder.labelName != null) {
            if (builder.formHorizontal) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                tv_label.setGravity(Gravity.CENTER);
                ll_field.addView(tv_label, 0, layoutParams);
            } else {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tv_label.setGravity(Gravity.CENTER_VERTICAL);
                ll_root.addView(tv_label, 0, layoutParams);
            }

            tv_label.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.labelTextSize);
            tv_label.setText(builder.labelName);
            if (builder.labelWidth != -1) {
                tv_label.setWidth(builder.labelWidth);
            }
            tv_label.setTextColor(ContextCompat.getColor(getContext(), builder.labelTextColor));
            tv_label.setBackgroundResource(builder.labelBgColor);
        }


        //分割线
        line_field.setVisibility(builder.fieldDivided ? VISIBLE : GONE);
        line_form.setVisibility(builder.formDivided ? VISIBLE : GONE);
        line_half.setVisibility(builder.lineHalfShow ? VISIBLE : GONE);
        line_field.setBackgroundResource(builder.dividedColor);
        line_form.setBackgroundResource(builder.dividedColor);

        //icon
        if (builder.textIcon != -1) {
            img_icon.setVisibility(VISIBLE);
            img_icon.setImageResource(builder.textIcon);
        }

        //必须填写的标志
        if (builder.mustInput && builder.showMustInput) {//必须输入且要显示
            if (builder.labelWithMustIcon) {
                //放在前面
                Drawable mustDrawable = ContextCompat.getDrawable(Utils.getApp(), R.mipmap.ic_red_star);
                if (tv_label != null)
                    modifyTextViewDrawable(tv_label, mustDrawable, 2);
                img_must.setVisibility(GONE);
            } else {
                //放在后面
                img_must.setVisibility(VISIBLE);
                tv_label.setCompoundDrawables(null, null, null, null);
            }
        } else {
            img_must.setVisibility(GONE);
            tv_label.setCompoundDrawables(null, null, null, null);
        }
        //dataview
        initContentViewByType(null);
    }


    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        int childCount = getChildCount();
        if (childCount == 0) {
            super.addView(child, params);
            return;
        }
        removeView(child);
        builder.dataView = child;
        initContentViewByType(params);
    }

    private void initContentViewByType(ViewGroup.LayoutParams params) {
        FrameLayout ll_data = ButterKnife.findById(this, R.id.ll_data);
        switch (builder.valueViewType) {
            case TYPE_EDITTEXT:
                builder.dataView = new FieldEditText(builder, context);
                break;
            case TYPE_TEXTVIEW:
                builder.dataView = new FieldTextView(builder, getContext());
                break;
            case TYPE_RADIOGROUP:
                builder.dataView = new FieldRadioGroup(builder, getContext());
                break;
            case TYPE_SPINNER:
                builder.dataView = new FieldSpinner(builder, getContext());
                break;
            case TYPE_HIDDENVIEW:
                setVisibility(GONE);
                builder.dataView = new HiddenFieldView(builder, getContext());
                break;
            default:
                initDataView(builder.dataView);
        }
        if (builder.dataView != null) {
            if (params == null) {
                params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            }
            ll_data.addView(builder.dataView, 0, params);
        }

    }

    private void initDataView(View view) {
        if (view != null) {
            if (view instanceof IFormField) {
                return;
            } else if (view instanceof EditText) {
                FormInitUtil.initEditText((EditText) view, builder);
            } else if (view instanceof TextView) {
                FormInitUtil.initTextView((TextView) view, builder);
            } else if (view instanceof ViewGroup) {
                ViewGroup chlid = (ViewGroup) view;
                initDataView(chlid.getChildAt(0));
            }
        }
    }

    public String getFieldName() {
        return builder.fieldName;
    }

    private String getChlidText(View view) {
        if (view instanceof IFormField) {
            return ((IFormField) view).getValue();
        } else if (view instanceof TextView) {
            return ((TextView) view).getText().toString();
        } else if (view instanceof ViewGroup) {
            ViewGroup chlid = (ViewGroup) view;
            return getChlidText(chlid.getChildAt(0));
        }
        return "";
    }


    public String getValue() {
        return getChlidText(builder.dataView);
    }

    private void setChlidText(View view, String value) {
        if (view instanceof IFormField) {
            ((IFormField) view).setVaule(value);
        } else if (view instanceof TextView) {
            ((TextView) view).setText(value);
        } else if (view instanceof ViewGroup) {
            ViewGroup chlid = (ViewGroup) view;
            setChlidText(chlid.getChildAt(0), value);
        }
    }

    public void setValue(String value) {
        setChlidText(builder.dataView, value);
    }

    public boolean isMustInput() {
        return builder.mustInput;
    }

    public String getWarnMessager() {
        return builder.warnMessager;
    }

    public String getLabelName() {
        if (builder.labelName != null) {
            return builder.labelName.toString().replace(":", "").replace(" ", "").trim();
        }

        return null;
    }

    public View getDataView() {
        return builder.dataView;
    }


    public void setMustInput(boolean mustInput) {
        ImageView img_must = ButterKnife.findById(this, R.id.img_must);
        builder.mustInput = mustInput;
        if (builder.labelWithMustIcon) {
            if (mustInput) {
                Drawable mustDrawable = ContextCompat.getDrawable(Utils.getApp(), R.mipmap.ic_red_star);
                modifyTextViewDrawable(tv_label, mustDrawable, 2);
            } else {
                tv_label.setCompoundDrawables(null, null, null, null);
            }

        } else {
            img_must.setVisibility(mustInput && builder.mustInput ? VISIBLE : GONE);
        }

    }

    public static final class Builder {
        private int fieldViewBgColor = R.color.comm_white;
        private CharSequence labelName;
        private int labelTextColor = R.color.comm_black;
        private int labelTextSize = SizeUtils.sp2px(14);
        private int labelBgColor = android.R.color.transparent;
        private int labelWidth = -1;
        private boolean labelWithMustIcon = false;
        private View dataView;
        private int valueViewType = -1;
        private int valueTextSize = SizeUtils.sp2px(14);
        private String warnMessager;
        private boolean mustInput = false;
        private boolean showMustInput = true;
        private boolean fieldDivided = true;
        private boolean formDivided = false;
        private boolean formHorizontal = true;
        private boolean lineHalfShow = false;
        private int dividedColor = R.color.comm_grey200;
        private int valueTextColor = R.color.comm_grey500;
        private int valueBgColor = android.R.color.transparent;
        private String fieldName;
        private String valueInitContent;
        private int textIcon = -1;
        private int fieldIndex = -1;
        private String edittextHint;
        private int edittextLine = -1;
        private int edittextTextlength;

        private Builder() {
        }

        public Builder fieldViewBgColor(int val) {
            fieldViewBgColor = val;
            return this;
        }

        public Builder labelName(CharSequence val) {
            labelName = val;
            return this;
        }

        public Builder labelTextColor(int val) {
            labelTextColor = val;
            return this;
        }

        public Builder labelTextSize(int val) {
            labelTextSize = val;
            return this;
        }

        public Builder labelBgColor(int val) {
            labelBgColor = val;
            return this;
        }

        public Builder labelWidth(int val) {
            labelWidth = val;
            return this;
        }

        public Builder labelWithMustIcon(boolean val) {
            labelWithMustIcon = val;
            return this;
        }

        public Builder lineHalfShow(boolean val) {
            lineHalfShow = val;
            return this;
        }

        public Builder dataView(View val) {
            dataView = val;
            return this;
        }

        public Builder valueViewType(int val) {
            valueViewType = val;
            return this;
        }

        public Builder valueTextSize(int val) {
            valueTextSize = val;
            return this;
        }

        public Builder warnMessager(String val) {
            warnMessager = val;
            return this;
        }

        public Builder valueInitContent(String val) {
            valueInitContent = val;
            return this;
        }

        public Builder mustInput(boolean val) {
            mustInput = val;
            return this;
        }

        public Builder showMustInput(boolean val) {
            showMustInput = val;
            return this;
        }

        public Builder fieldDivided(boolean val) {
            fieldDivided = val;
            return this;
        }

        public Builder formDivided(boolean val) {
            formDivided = val;
            return this;
        }

        public Builder dividedColor(int val) {
            dividedColor = val;
            return this;
        }

        public Builder valueTextColor(int val) {
            valueTextColor = val;
            return this;
        }

        public Builder valueBgColor(int val) {
            valueBgColor = val;
            return this;
        }

        public Builder fieldName(String val) {
            fieldName = val;
            return this;
        }


        public Builder textIcon(int val) {
            textIcon = val;
            return this;
        }

        public Builder fieldIndex(int val) {
            fieldIndex = val;
            return this;
        }

        public Builder edittextHint(String val) {
            edittextHint = val;
            return this;
        }

        public Builder edittextLine(int val) {
            edittextLine = val;
            return this;
        }

        public Builder edittextTextlength(int val) {
            edittextTextlength = val;
            return this;
        }

        public Builder formHorizontal(boolean val) {
            formHorizontal = val;
            return this;
        }

        public FieldView build(Context context) {
            return new FieldView(this, context);
        }

        public String getValueInitContent() {
            return valueInitContent;
        }


        public CharSequence getLabelName() {
            return labelName;
        }


        public View getDataView() {
            return dataView;
        }


        public int getValueTextSize() {
            return valueTextSize;
        }


        public int getValueTextColor() {
            return valueTextColor;
        }

        public String getEdittextHint() {
            return edittextHint;
        }

        public int getEdittextLine() {
            return edittextLine;
        }


    }

    private   void modifyTextViewDrawable(TextView v, Drawable drawable, int index) {
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        //index 0:左 1：上 2：右 3：下
        if (index == 0) {
            v.setCompoundDrawables(drawable, null, null, null);
        } else if (index == 1) {
            v.setCompoundDrawables(null, drawable, null, null);
        } else if (index == 2) {
            v.setCompoundDrawables(null, null, drawable, null);
        } else if (index == 3) {
            v.setCompoundDrawables(null, null, null, drawable);
        } else {
            v.setCompoundDrawables(null, null, null, null);
        }
    }
}
