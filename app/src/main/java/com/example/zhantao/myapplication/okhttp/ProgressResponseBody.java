package com.example.zhantao.myapplication.okhttp;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by zhantao on 2017/4/10.
 */

public class ProgressResponseBody  extends ResponseBody {

    private ResponseBody mResponseBody;

    private BufferedSource mBufferedSource;

    private ProgressListener mProgressListener;

    public ProgressResponseBody(ResponseBody responseBody, ProgressListener listener) {
        this.mResponseBody = responseBody;
        this.mProgressListener = listener;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {

        if(mBufferedSource==null)
    mBufferedSource=Okio.buffer(mResponseBody.source());
        return mBufferedSource;
  }
    public Source getsource(Source source ){
        return new ForwardingSource(source) {

            long totalSize=0;
            long sum=0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                if (totalSize==0){
                    totalSize=contentLength();
                }
                long len=super.read(sink,byteCount);

                sum+=(len==-1?0:len);

                int progress= (int) ((sum*1.0f/totalSize)*100);
                if (len==-1){
                    mProgressListener.onDone(totalSize);
                }else
                    mProgressListener.onProgress(progress);
                return len;
            }
        };
    }
}
