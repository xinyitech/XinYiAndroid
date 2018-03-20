package com.xinyiandroid.api;

import com.lzy.okgo.OkGo;
import com.lzy.okrx2.adapter.ObservableBody;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;

/**
 * Created by jiajun.wang on 2018/3/19.
 */

public class ApiManager {
    private static String host="http://erptest.xinyiglass.com:8000/xygerp";
    private static String login=host+"/ald/auth/token";//登录接口


    //登录接口
    public static <T> Observable<ResultEntity<T>> Login(String...a) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("userName",a[0]);
            jsonObject.put("password",a[1]);
            jsonObject.put("language",a[2]);
            jsonObject.put("wxLoginCode","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return OkGo.<ResultEntity<T>>post(login)
                .headers("Authorization","")
                .upJson(jsonObject.toString())//
                .converter(new ResultConvert<T>())
                .adapt(new ObservableBody<ResultEntity<T>>());


    }
}
