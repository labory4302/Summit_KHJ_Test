package com.summit_khj_test.sync;

import android.content.Context;
import android.content.Intent;

public class SunshineSyncUtils {

    //비동기 실행을 위해 IntentService를 사용하여 즉시 동기화를 수행
    public static void startImmediateSync(final Context context) {
        Intent intentToSyncImmediately = new Intent(context, SunshineSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
