package com.summit_khj_test.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class SunshineFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchWeatherTask;

    //운영체제가 작업을 실행할 준비가 된다면 자동으로 실행
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mFetchWeatherTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                SunshineSyncTask.syncWeather(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //작업이 끝났음을 의미
                //true는 지금은 이 작업을 수행할 수 없으니 향후에 다시 시작시켜달라는 것을 의미
                jobFinished(jobParameters, false);
            }
        }.execute();

        //true : 작업이 아직 진행중
        //flase : 작압이 아직 완료되지 않음
        return true;
    }

    //작업이 중단될 필요가 있을 경우 호출
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mFetchWeatherTask != null) {
            mFetchWeatherTask.cancel(true);
        }

        //true : 향후에 다시 실행되어야 함을 의미
        //false : 향후에 다시 시작할 필요가 없음
        return true;
    }
}
