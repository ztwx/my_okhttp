package com.example.zhantao.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    String url="http://www.jianshu.com/p/e3b49ccfe10b";

    Request request=new Request.Builder().url(url).build();

   public void getRequest(View v){
       OkHttpClient client =new OkHttpClient();
       client.newCall(request).enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               Log.d("MainActivity","失败"+e.getLocalizedMessage());
           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
               String result=response.body().string();
               Log.d( "MainActivity ","成功"+result);

               if (response.body()!=null){
                   response.body().close();
               }
           }
       });
   }
}
