package com.summit_khj_test.sync;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.google.common.util.concurrent.ListenableFuture;

//백그라운드 작업 정의
public class SunshineWorkManager extends Worker {

    public SunshineWorkManager(Context context, WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        try {
            //임시로 스레드 사용
            Context context = getApplicationContext();
            SunshineSyncTask.syncWeather(context);
            Thread.sleep(2000);
        } catch (Exception e) {
            return Result.failure();
        }

        return Result.success();
    }

    //추후 구현 요망
    @Override
    public void onStopped() {
        super.onStopped();
    }
}
