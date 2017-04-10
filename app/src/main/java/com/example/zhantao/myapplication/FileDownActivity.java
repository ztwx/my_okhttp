package com.example.zhantao.myapplication;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zhantao.myapplication.okhttp.ProgressListener;
import com.example.zhantao.myapplication.okhttp.ProgressResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileDownActivity extends AppCompatActivity {
    public String url="http://112.124.22.238:8081/course_api/css/net_music.apk";
    public String fileName="net_music.apk";

    @Bind(R.id.btn_download)
    Button btnDownload;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private OkHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_down);
        ButterKnife.bind(this);
        initOkhttp();
    }


    private void initOkhttp() {
        //httpClient=new OkHttpClient();
        httpClient=new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response=chain.proceed(chain.request());
                return response.newBuilder().body(new ProgressResponseBody(response.body(),new Prg())).build();
            }
        }).build();
    }
    class Prg implements ProgressListener{

        @Override
        public void onProgress(final int progress) {
            //progressBar.setProgress(progress);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(progress);
                }
            });

        }

        @Override
        public void onDone(long totalSize) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FileDownActivity.this,"下载完成",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @OnClick(R.id.btn_download)
    public void onClicked() {
        downloadAPK();
    }

    private void downloadAPK() {

        Request request=new Request.Builder()
                .url(url)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("onFailure: ","请求数据失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                writeFile(response);
            }
        });
    }
        Handler handle = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    int progress=0;
                    progressBar.setProgress(progress);
                }

            }
        };


    private void writeFile(Response response) {

        InputStream is=null;
        FileOutputStream fos=null;
        is=response.body().byteStream();
        String path= Environment.getExternalStorageDirectory().getAbsolutePath();
        File file=new File(path,fileName);

        try {
            fos=new FileOutputStream(file);
            byte[] bytes=new byte[1024];
            int len=0;

            long totalSize= response.body().contentLength();
            long sum=0;

            while ((len=is.read(bytes))!=-1){

                fos.write(bytes);
                sum+=len;
                int progress=(int) ((sum*1.0f/totalSize)*100);
                Message msg=handle.obtainMessage(1);
                msg.arg1=progress;
                handle.sendMessage(msg);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
