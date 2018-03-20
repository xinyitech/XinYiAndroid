package com.xinyiandroid.utils;

import com.google.gson.Gson;

/**
 * Created by wxy on 2018/2/26.
 */

public class GsonUtil {

	private static Gson gson=new Gson();
	public static String obj2String(Object object){
		return gson.toJson(object);
	}

	public static <T> T String2Obj(String s, Class <T>c){
		return gson.fromJson(s, c);
	}
}
