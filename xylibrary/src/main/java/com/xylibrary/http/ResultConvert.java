package com.xylibrary.http;


import com.alibaba.fastjson.JSON;
import com.lzy.okgo.convert.Converter;
import com.xylibrary.application.Constants;

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
		if (response.code() == Constants.SUCCESSCODE) {
			return JSON.parseObject(body.string(),resultEntity.getClass());
		} else if (response.code() == Constants.UNAUTHORIZEDCODE) {
			throw new Exception(Constants.UNAUTHORIZED);
		} else if (response.code()==Constants.SERVERERRORCODE){
			throw new Exception(Constants.SERVERERROR);
		}else {
			throw new Exception(Constants.UNKNOWERROR);
		}
	}
}
