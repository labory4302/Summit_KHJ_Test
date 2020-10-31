package com.summit_khj_test;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.summit_khj_test.data.WeatherContract;
import com.summit_khj_test.utilities.SunshineDateUtils;
import com.summit_khj_test.utilities.SunshineWeatherUtils;

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    public static final String[] WEATHER_DETAIL_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_HUMIDITY = 3;
    public static final int INDEX_WEATHER_PRESSURE = 4;
    public static final int INDEX_WEATHER_WIND_SPEED = 5;
    public static final int INDEX_WEATHER_DEGREES = 6;
    public static final int INDEX_WEATHER_CONDITION_ID = 7;

    private static final int ID_DETAIL_LOADER = 353;

    private String mForecastSummary;

    private Uri mUri;

    private TextView mDateView;
    private TextView mDescriptionView;
    private TextView mHighTemperatureView;
    private TextView mLowTemperatureView;
    private TextView mHumidityView;
    private TextView mWindView;
    private TextView mPressureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDateView = (TextView) findViewById(R.id.date);
        mDescriptionView = (TextView) findViewById(R.id.weather_description);
        mHighTemperatureView = (TextView) findViewById(R.id.high_temperature);
        mLowTemperatureView = (TextView) findViewById(R.id.low_temperature);
        mHumidityView = (TextView) findViewById(R.id.humidity);
        mWindView = (TextView) findViewById(R.id.wind);
        mPressureView = (TextView) findViewById(R.id.pressure);

        mUri = getIntent().getData();
        if (mUri == null)
            throw new NullPointerException("URI for DetailActivity cannot be null");

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
    }

    //공유 인텐트 반환
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        return shareIntent;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        switch (id) {
            case ID_DETAIL_LOADER:
                return new CursorLoader(this,
                        mUri,
                        WEATHER_DETAIL_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            return;
        }

        //날짜 정보 표시
        long localDateMidnightGmt = data.getLong(INDEX_WEATHER_DATE);
        String dateText = SunshineDateUtils.getFriendlyDateString(this, localDateMidnightGmt, true);
        mDateView.setText(dateText);

        //날씨 상태 표시
        int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
        String description = SunshineWeatherUtils.getStringForWeatherCondition(this, weatherId);
        mDescriptionView.setText(description);

        //최고 기온 표시
        double highInCelsius = data.getDouble(INDEX_WEATHER_MAX_TEMP);
        String highString = SunshineWeatherUtils.formatTemperature(this, highInCelsius);
        mHighTemperatureView.setText(highString);

        //최저 기온 표시
        double lowInCelsius = data.getDouble(INDEX_WEATHER_MIN_TEMP);
        String lowString = SunshineWeatherUtils.formatTemperature(this, lowInCelsius);
        mLowTemperatureView.setText(lowString);

        //습도 표시
        float humidity = data.getFloat(INDEX_WEATHER_HUMIDITY);
        String humidityString = getString(R.string.format_humidity, humidity);
        mHumidityView.setText(humidityString);

        //풍량 및 풍향 표시
        float windSpeed = data.getFloat(INDEX_WEATHER_WIND_SPEED);
        float windDirection = data.getFloat(INDEX_WEATHER_DEGREES);
        String windString = SunshineWeatherUtils.getFormattedWind(this, windSpeed, windDirection);
        mWindView.setText(windString);

        //기압 표시
        float pressure = data.getFloat(INDEX_WEATHER_PRESSURE);
        String pressureString = getString(R.string.format_pressure, pressure);
        mPressureView.setText(pressureString);

        mForecastSummary = String.format("%s - %s - %s/%s",
                dateText, description, highString, lowString);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
