package com.summit_khj_test.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String DYNAMIC_WEATHER_URL =
            "https://andfun-weather.udacity.com/weather";

    private static final String STATIC_WEATHER_URL =
            "https://andfun-weather.udacity.com/staticweather";

    private static final String FORECAST_BASE_URL = STATIC_WEATHER_URL;

    //API가 반환할 format
    private static final String format = "json";
    //API가 반환할 units
    private static final String units = "metric";
    //API가 반환할 day의 수
    private static final int numDays = 14;

    //API 요청 URL parameter
    final static String QUERY_PARAM = "q";
    final static String LAT_PARAM = "lat";
    final static String LON_PARAM = "lon";
    final static String FORMAT_PARAM = "mode";
    final static String UNITS_PARAM = "units";
    final static String DAYS_PARAM = "cnt";

    //API 요청 동적 URL 생성 메소드
    public static URL buildUrl(String locationQuery) {
        URL url = null;

        Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, locationQuery)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v("TAG", "만들어진 URI 확인 : " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        //URL연결객체 생성 및 연결
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            //연결객체로부터 받을 입력스트림
            InputStream in = urlConnection.getInputStream();

            //스트림을 토큰화하는 scanner 생성
            Scanner scanner = new Scanner(in);

            //scanner가 다음 토큰스트림까지의 모든 내용을 읽어오게 함
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
