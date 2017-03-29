package com.example.zhantao.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserinfoActivity extends AppCompatActivity {

    @Bind(R.id.btn_get)
    Button btnGet;
    @Bind(R.id.imgview)
    ImageView imgview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_get)
    public void onViewClicked() {


        getUserInfo();
    }

    private void getUserInfo() {
        OkHttpClient client =new OkHttpClient();
        final Request request=new Request.Builder()
                .get()
                .url("file:///E:/apache-tomcat-8.0.42-windows-x64/apache-tomcat-8.0.42/webapps/test.html?id=btn_get")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("UserInfoActivity","失败"+e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//              if (response.code()==200){
//                  String json=response.body().string();
//                Log.d( "UserInfoActivity","成功"+json);
//              }
                if (response.isSuccessful()){
                    String json = response.body().string();
                    showuser(json);
                }
            }
        });
    }
    private void showuser(final String json){
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               try {
                   JSONObject jsonObject=new JSONObject(json);
                   String text=jsonObject.optString("btn_get");
                   //String src=jsonObject.optString("imgview");

                   btnGet.setText(text);
                   //Picasso.with(UserinfoActivity.this).load(src).resize(200,200).centerCrop().into(imgview);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       });
    }
}
