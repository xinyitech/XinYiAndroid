package com.xinyiandroid.api

import com.alibaba.fastjson.TypeReference
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.lzy.okrx2.adapter.ObservableBody
import com.xylibrary.http.ResultConvert
import io.reactivex.Observable
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by jiajun.wang on 2018/3/19.
 */

class ApiManager {

    fun test() {
        OkGo.post<String>(login)
                .headers("Authorization", "")
                .upJson("")
                .execute(object : StringCallback() {

                    override fun onSuccess(response: Response<String>) {

                    }
                })
    }

    companion object {
        private val host = "http://erptest.xinyiglass.com:8000/xygerp"
        private val login = "$host/ald/auth/token"//登录接口


        //登录接口
        fun <T> Login(typeReference: TypeReference<*>, vararg a: String): Observable<T> {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("userName", a[0])
                jsonObject.put("password", a[1])
                jsonObject.put("language", a[2])
                jsonObject.put("wxLoginCode", "")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return OkGo.post<T>(login)
                    .headers("Authorization", "")
                    .upJson(jsonObject.toString())//
                    .converter(ResultConvert(typeReference))
                    .adapt(ObservableBody())


        }
    }
}
