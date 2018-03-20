package com.xinyiandroid.api;


import com.google.gson.Gson;
import com.lzy.okgo.convert.Converter;
import com.xinyiandroid.application.Constants;

import org.json.JSONObject;

import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created by jiajun.wang on 2018/2/25.
 * T为 要转换的数据类型
 */

public class ResultConvert<T> implements Converter<ResultEntity<T>> {

	@Override
	public ResultEntity<T> convertResponse(Response response) throws Throwable {
		ResponseBody body = response.body();
		if (body == null) return null;
		ResultEntity<T> resultEntity = new ResultEntity<T>();
		JSONObject jsonObject=new JSONObject(body.string());
		if (response.code() == Constants.SUCCESSCODE) {
			resultEntity=new Gson().fromJson(jsonObject.toString(),resultEntity.getClass());
			return resultEntity;
		} else if (response.code() == Constants.UNAUTHORIZEDCODE) {
			throw new Exception(Constants.UNAUTHORIZED);
		} else if (response.code()==Constants.SERVERERRORCODE){
			throw new Exception(Constants.SERVERERROR);
		}else {
			throw new Exception(Constants.UNKNOWERROR);
		}
	}
}
