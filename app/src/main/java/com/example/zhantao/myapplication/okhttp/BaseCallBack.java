package com.example.zhantao.myapplication.okhttp;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by zhantao on 2017/4/10.
 */

public abstract class BaseCallBack<T> {

    static Type getSuperclassTypeParameter(Class<?> subclass){
        Type superclass=subclass.getGenericSuperclass();
        if (subclass instanceof Class){
            return null;
        }
        ParameterizedType parameterized= (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public Type mType;

    public BaseCallBack(){
        mType=getSuperclassTypeParameter(this.getClass());

    }
    public void onSuccess(T t){}
    public void onFailure(Call call, IOException e){}
    public void onError(int code){}
}
