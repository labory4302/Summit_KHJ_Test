package com.summit_khj_test.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.text.format.DateUtils;

import com.summit_khj_test.data.SunshinePreferences;
import com.summit_khj_test.data.WeatherContract;
import com.summit_khj_test.utilities.NetworkUtils;
import com.summit_khj_test.utilities.NotificationUtils;
import com.summit_khj_test.utilities.OpenWeatherJsonUtils;

import java.net.URL;

//유효한 데이터가 들어오면 이전 데이터를 삭제하고 새로운 데이터를 삽입
public class SunshineSyncTask {

    //synchronized multi-thread로 동시접근되는것을 막는다 (단순한 개념)
    synchronized public static void syncWeather(Context context) {
        try {
            URL weatherRequestUrl = NetworkUtils.getUrl(context);

            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

            ContentValues[] weatherValues = OpenWeatherJsonUtils
                    .getWeatherContentValuesFromJson(context, jsonWeatherResponse);

            //만약 가져온 데이터에 오류가 없다면
            if (weatherValues != null && weatherValues.length != 0) {
                ContentResolver sunshineContentResolver = context.getContentResolver();

                //과거 데이터 삭제
                sunshineContentResolver.delete(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        null,
                        null);

                //새로운 데이터 삽입
                sunshineContentResolver.bulkInsert(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        weatherValues);

                //푸시 알림 설정 여부 판단
                boolean notificationsEnabled = SunshinePreferences.areNotificationsEnabled(context);
                long timeSinceLastNotification = SunshinePreferences
                        .getEllapsedTimeSinceLastNotification(context);

                boolean oneDayPassedSinceLastNotification = false;

                if (timeSinceLastNotification >= DateUtils.DAY_IN_MILLIS) {
                    oneDayPassedSinceLastNotification = true;
                }

                if (notificationsEnabled && oneDayPassedSinceLastNotification) {
                    NotificationUtils.notifyUserOfNewWeather(context);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
