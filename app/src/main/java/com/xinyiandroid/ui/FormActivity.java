package com.xinyiandroid.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xylibrary.form.FieldSpinner;
import com.xylibrary.form.FieldView;
import com.xylibrary.form.FormLayout;

import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import xinyi.com.xinyiandroid.R;

public class FormActivity extends AppCompatActivity {

    @Bind(R.id.form)
    FormLayout mForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ButterKnife.bind(this);
        mForm.addFieldView(FieldView.newBuilder().fieldName("test").mustInput(true).textIcon(R.mipmap.ic_launcher).fieldDivided(false).fieldIndex(1).dataView(new FieldSpinner(null, this)));


    }

    public void getParams(View view) {
        if (mForm.checkForm(FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE)) {
            Map<String, Object> params = mForm.getParams(FormLayout.ActionFieldType.FIELD_TYPE_VISIBLE);
            Set<String> strings = params.keySet();
            for (String key : strings) {
                //XinYiLog.e(key + "  " + params.get(key));
            }
        }

    }
}
