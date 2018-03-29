package com.xylibrary.http;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.blankj.utilcode.util.StringUtils;
import com.lzy.okgo.convert.Converter;
import com.xylibrary.application.Constants;

import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created by jiajun.wang on 2018/2/25.
 * T为 要转换的数据类型
 */

public class ResultConvert<T> implements Converter<T> {

    private TypeReference typeReference;

    public ResultConvert(TypeReference typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public T convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) return null;
        if (response.code() == Constants.SUCCESSCODE) {
            JSONObject mJsonObject = JSON.parseObject(response.body().string());
            if (mJsonObject.getIntValue("code") == 0) {
                if (StringUtils.isEmpty(mJsonObject.getString("data"))) {
                    return (T) JSON.parseObject("{}", typeReference, Feature.AllowComment);
                } else {
                    return (T) JSON.parseObject(mJsonObject.getString("data"), typeReference, Feature.AllowComment);
                }
            } else {
                throw new Exception(mJsonObject.getString("message"));
            }
        } else if (response.code() == Constants.UNAUTHORIZEDCODE) {
            throw new Exception(Constants.UNAUTHORIZED);
        } else if (response.code() == Constants.SERVERERRORCODE) {
            throw new Exception(Constants.SERVERERROR);
        } else {
            throw new Exception(Constants.UNKNOWERROR);
        }
    }


}
