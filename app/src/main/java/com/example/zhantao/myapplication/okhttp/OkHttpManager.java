package com.example.zhantao.myapplication.okhttp;

import android.os.Handler;

import com.example.zhantao.myapplication.model.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by zhantao on 2017/4/10.
 */

public class OkHttpManager {

    private static OkHttpManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private Gson mGson;

    private OkHttpManager(){
        initOkHttp();
        mHandler=new Handler();
        mGson=new Gson();
    }
    public static synchronized OkHttpManager getInstance(){
        if (mInstance==null)
            mInstance=new OkHttpManager();
        return mInstance;
    }

    private void initOkHttp(){
        mOkHttpClient=new OkHttpClient().newBuilder()
                .readTimeout(5000, TimeUnit.SECONDS)
                .connectTimeout(5000,TimeUnit.SECONDS).build();

    }


    public void request(SimpleHttpClient client, final BaseCallBack callback){

        if (callback==null){
            throw new NullPointerException("callback is null");
        }
        mOkHttpClient.newCall(client.buildRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendonFailureMessage(callback,call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()){
                    String result=response.body().string();
                    if (callback.mType==null||callback.mType==String.class){
                        sendonSuccessMessage(callback,result);
                    }else {
                        sendonSuccessMessage(callback,mGson.fromJson(result,callback.mType));

                    }

                    if (response.body()!=null){
                        response.body().close();;
                    }

                }else {

                    sendonErrorMessage(callback,response.code());
                }

            }
        });

    }
    private void sendonFailureMessage(final BaseCallBack callback, final Call call, final IOException e){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(call,e);
            }
        });
    }
    private void sendonErrorMessage(final BaseCallBack callback, final int coad){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(coad);
            }
        });
    }
    private void sendonSuccessMessage(final BaseCallBack callback, final Object result){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
               callback.onSuccess(result);
            }
        });
    }

}
