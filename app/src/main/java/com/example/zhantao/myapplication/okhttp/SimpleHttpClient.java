package com.example.zhantao.myapplication.okhttp;

import android.net.Uri;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by zhantao on 2017/4/10.
 */

public class SimpleHttpClient {

    private Builder mBuilder;

    private SimpleHttpClient(Builder builder){
        this.mBuilder=builder;
    }

    public Request buildRequest(){
        Request.Builder builder=new Request.Builder();
        if (mBuilder.method=="GET"){
            builder.url(buildGetRequestParam());
            builder.get();
        }
        else if (mBuilder.method=="POST") {
            try {
                builder.post(buildRequsetBody());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            builder.url(mBuilder.url);

        }

        return builder.build();
    }

    private String buildGetRequestParam(){
        if (mBuilder.mParams.size()<=0)
            return this.mBuilder.url;
        Uri.Builder builder=Uri.parse(mBuilder.url).buildUpon();
        for(RequestParam p:mBuilder.mParams){
            builder.appendQueryParameter(p.getKey(),p.getObj()==null?"":p.getObj().toString());
        }
        String url=builder.build().toString();
        return url;
    }
    private RequestBody buildRequsetBody()throws JSONException{
        return  null;
    }

    public void enqueue(BaseCallBack callBack){

    }

    public static Builder newBuilder(){

        return newBuilder();
    }

    public static class Builder {
        private String url;
        private String method;
        private boolean isJsonParam;
        private List<RequestParam> mParams;

        private Builder(){
            method="GET";
        }

        public SimpleHttpClient build() {
            return new SimpleHttpClient(this);
        }

        public Builder url(String url) {
            this.url=url;
            return this;
    }
        public Builder get(){
            method="GET";
            return this;
        }

        /**
         * From表单
         * @return
         */
        public Builder post(){
            method="POST";
            return this;
        }

        /**
         * Json形式
         * @return
         */
        public Builder json(){
            isJsonParam=true;
            return post();
        }
        public Builder addParam(String key,Object value){
            if (mParams == null) {
            mParams=new ArrayList<>();
            }

           mParams.add (new RequestParam(key,value));
            return this;
        }
    }
}
