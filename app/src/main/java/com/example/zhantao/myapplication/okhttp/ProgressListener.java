package com.example.zhantao.myapplication.okhttp;

/**
 * Created by zhantao on 2017/4/10.
 */

public interface ProgressListener {
    public void onProgress(int progress);
    public void onDone(long totalSize);
}
