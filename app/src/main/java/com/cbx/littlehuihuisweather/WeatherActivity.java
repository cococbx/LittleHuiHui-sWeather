package com.cbx.littlehuihuisweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    }
}
