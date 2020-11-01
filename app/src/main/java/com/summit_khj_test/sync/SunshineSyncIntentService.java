package com.summit_khj_test.sync;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

//IntentService는 인텐트를 전달하여 서비스의 어떤 작업을 수행하는데 사용
public class SunshineSyncIntentService extends IntentService {
    public SunshineSyncIntentService() {
        super("SunshineSyncIntentService");
    }

    //Intent가 전달될 때 수행되는 job
    @Override
    protected void onHandleIntent(Intent intent) {
        SunshineSyncTask.syncWeather(this);
    }
}
