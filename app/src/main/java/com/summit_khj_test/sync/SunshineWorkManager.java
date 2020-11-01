package com.summit_khj_test.sync;

import android.content.Context;
import android.os.AsyncTask;

import androidx.work.ListenableWorker;

import androidx.work.WorkerParameters;
import com.google.common.util.concurrent.ListenableFuture;

public class SunshineWorkManager extends ListenableWorker {

    private AsyncTask<Void, Void, Void> mFetchWeatherTask;

    public SunshineWorkManager(Context context, WorkerParameters params) {
        super(context, params);
    }

    @Override
    public ListenableFuture<Result> startWork() {
        //스레드를 사용하여 작업 생성
        //workManager에서 스레딩을 통한 작업 방법 알아야 함

        //ListenableWorker의 스레딩
        //https://developer.android.com/topic/libraries/architecture/workmanager/advanced/listenableworker?hl=ko

        //Firebase JobDispatcher에서 WorkManager로 이전
        //https://developer.android.com/topic/libraries/architecture/workmanager/migrating-fb?hl=ko

        return (ListenableFuture<Result>) Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();
    }
}
