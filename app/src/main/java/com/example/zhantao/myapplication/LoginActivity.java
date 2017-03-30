package com.example.zhantao.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.etxt_usernanme)
    EditText etxtUsernanme;
    @Bind(R.id.etxt_password)
    EditText etxtPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;
    private OkHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        httpClient=new OkHttpClient();
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {

        String username=etxtUsernanme.getText().toString().trim();
        String password=etxtPassword.getText().toString().trim();

            loginWithForm(username,password);//form表单形式
          //loginWithJson(username,password);//json参数形式


    }

    private void loginWithJson(String username, String password) {

    }

    private void loginWithForm(String username, String password) {

        String url=config.API.BESE_URL+"login";
        RequestBody body=new FormBody.Builder().add("username",username).add("password",password).build();
        Request request=new Request
                .Builder()
                .url(url)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("onFailure: ","请求服务器失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String json=response.body().string().trim();

                    try {
                        JSONObject jsonobj=new JSONObject(json);

                        final String message=jsonobj.optString("message");
                        final int success=jsonobj.optInt("success");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (success==1){
                                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
