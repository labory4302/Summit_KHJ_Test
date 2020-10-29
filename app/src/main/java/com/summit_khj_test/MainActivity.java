package com.summit_khj_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);

        //임시 날씨 데이터
        String[] tmpWhetherData = {
                "10/29 맑음 10°C  18°C",
                "10/30 구름조금 9°C  16°C",
                "10/31 흐림 4°C  11°C",
                "11/01 눈 1°C  8°C",
                "11/02 눈 2°C  10°C",
                "11/03 맑음 8°C  19°C",
                "11/04 눈 2°C  11°C",
                "11/05 눈 1°C  8°C",
                "11/06 눈 1°C  7°C",
                "11/07 맑음 5°C  11°C",
                "11/08 눈 2°C  14°C",
                "11/09 눈 3°C  11°C",
                "11/10 눈 3°C  10°C",
                "11/11 맑음 2°C  8°C",
                "11/12 눈 1°C  8°C"
        };

        for (String howWhether : tmpWhetherData) {
            mWeatherTextView.append(howWhether + "\n\n");
        }
    }
}