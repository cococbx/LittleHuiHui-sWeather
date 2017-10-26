package com.cbx.littlehuihuisweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbx.littlehuihuisweather.gson.Forecast;
import com.cbx.littlehuihuisweather.gson.Weather;
import com.cbx.littlehuihuisweather.utils.HttpUtil;
import com.cbx.littlehuihuisweather.utils.Utility;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    @BindView(R.id.title_city)
    TextView titleCity;//城市名
    @BindView(R.id.title_update_time)
    TextView titleUpdateTime;//数据更新时间
    @BindView(R.id.degree_text)
    TextView degreeText;//当前气温
    @BindView(R.id.weather_info_text)
    TextView weatherInfoText;//天气概况
    @BindView(R.id.forecast_layout)
    LinearLayout forecastLayout;//天气预报信息布局
    @BindView(R.id.air_quality)
    TextView airQuality;//空气质量
    @BindView(R.id.aqi_text)
    TextView aqiText;//空气质量指数
    @BindView(R.id.pm25_text)
    TextView pm25Text;//pm2.5指数
    @BindView(R.id.air_text)
    TextView airText;//空气指数
    @BindView(R.id.uv_text)
    TextView uvText;//紫外线指数
    @BindView(R.id.comfort_text)
    TextView comfortText;//舒适度指数
    @BindView(R.id.dress_text)
    TextView dressText;//穿衣指数
    @BindView(R.id.influenza_text)
    TextView influenzaText;//感冒指数
    @BindView(R.id.sport_text)
    TextView sportText;//	运动指数
    @BindView(R.id.travel_text)
    TextView travelText;//旅游指数
    @BindView(R.id.car_wash_text)
    TextView carWashText;//洗车指数
    @BindView(R.id.weather_layout)
    ScrollView weatherLayout;//天气界面滑动布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = sharedPreferences.getString("weather", null);//存储的天气json
        if (weatherString != null) {
            /*有数据缓存，直接解析天气数据*/
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            /*无数据缓存，去服务器请求天气数据*/
            String weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
    }

    public void requestWeather(final String weatherId) {

        String weatherUrl = getString(R.string.weatherUrl_partOne) + weatherId +
                getString(R.string.weatherUrl_partTwo) + getString(R.string.weather_key);

        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                //通过runOnUiThread()方法回到主线程处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, getString(R.string.get_weather_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, getString(R.string.get_weather_failed), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showWeatherInfo(Weather weather) {

        if (weather.basic != null) {
            titleCity.setText(weather.basic.cityName);//城市名
            titleUpdateTime.setText(weather.basic.update.updateTime);//数据更新时间
        }

        if (weather.now != null) {
            String tmp = weather.now.temperature + getString(R.string.centigrade);
            degreeText.setText(tmp);//当前气温
            weatherInfoText.setText(weather.now.more.info);//天气概况
        }

        forecastLayout.removeAllViews();//天气预报信息布局
        if (weather.forecastList != null && weather.forecastList.size() > 0) {
            for (Forecast forecast : weather.forecastList) {
                View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
                TextView dateText = view.findViewById(R.id.date_text);
                TextView infoText = view.findViewById(R.id.info_text);
                TextView maxText = view.findViewById(R.id.max_text);
                TextView minText = view.findViewById(R.id.min_text);

                dateText.setText(forecast.date);
                infoText.setText(forecast.forecastMore.info);
                String max = forecast.temperature.max + getString(R.string.centigrade);
                String min = forecast.temperature.min + getString(R.string.centigrade);
                maxText.setText(max);
                minText.setText(min);

                forecastLayout.addView(view);
            }
        }

        if (weather.aqi != null) {
            airQuality.setText(weather.aqi.aqiCity.quality);
            aqiText.setText(weather.aqi.aqiCity.aqi);//空气质量指数
            pm25Text.setText(weather.aqi.aqiCity.pm25);//pm2.5指数
        }

        if (weather.suggestion != null) {
            String airQua = getString(R.string.air_qulty) + weather.suggestion.air.info;
            airText.setText(airQua);//空气指数

            String uv = getString(R.string.uv) + weather.suggestion.uv.info;
            uvText.setText(uv);//紫外线指数

            String comf = getString(R.string.comf) + weather.suggestion.comfort.info;
            comfortText.setText(comf);//舒适度指数

            String dress = getString(R.string.drsg) + weather.suggestion.dress.info;
            dressText.setText(dress);//穿衣指数

            String influenza = getString(R.string.flu) + weather.suggestion.influenza.info;
            influenzaText.setText(influenza);//感冒指数

            String sport = getString(R.string.sport) + weather.suggestion.sport.info;
            sportText.setText(sport);//	运动指数

            String travel = getString(R.string.trav) + weather.suggestion.travel.info;
            travelText.setText(travel);//旅游指数

            String wash = getString(R.string.cw) + weather.suggestion.carWash.info;
            carWashText.setText(wash);//洗车指数

            weatherLayout.setVisibility(View.VISIBLE);//天气界面滑动布局
        }
    }

}
