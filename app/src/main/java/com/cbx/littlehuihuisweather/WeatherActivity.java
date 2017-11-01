package com.cbx.littlehuihuisweather;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cbx.littlehuihuisweather.db.Province;
import com.cbx.littlehuihuisweather.fragment.ChooseAreaFragment;
import com.cbx.littlehuihuisweather.gson.Forecast;
import com.cbx.littlehuihuisweather.gson.Weather;
import com.cbx.littlehuihuisweather.service.AutoUpdateService;
import com.cbx.littlehuihuisweather.utils.HttpUtil;
import com.cbx.littlehuihuisweather.utils.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.lang.reflect.Field;

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

    @BindView(R.id.bing_pic_img)
    ImageView bingImageView;//背景图
    @BindView(R.id.swipe_refresh)
    public SwipeRefreshLayout swipeRefresh;//下拉刷新控件

    @BindView(R.id.home_button)
    Button homeButton;//选择城市按钮
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;//滑动菜单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 沉浸式状态栏
         */
        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT != Build.VERSION_CODES.M) {
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            View decorView = getWindow().getDecorView();//拿到布局view
            //布局显示填充到状态栏位置，状态栏压在布局上面
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);//状态栏设置为透明
        }
        /**
         * android6.0沉浸式状态栏蒙灰问题
         */
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();//拿到布局view
            //布局显示填充到状态栏位置，状态栏压在布局上面
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);//状态栏设置为透明
        }
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = sharedPreferences.getString("weather", null);//存储的天气json
        final String weatherId;
        if (weatherString != null) {
            /*有数据缓存，直接解析天气数据*/
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            /*无数据缓存，去服务器请求天气数据*/
            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }

        String bingPic = sharedPreferences.getString("bing_pic", null);//存储的背景图
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingImageView);
        } else {
            loadBingPic();
        }

        //下拉刷新
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                String weatherID = sharedPreferences.getString("weatherId", null);
                if (weatherID != null) {
                    requestWeather(weatherID);
                } else {
                    requestWeather(weatherId);
                }
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    /**
     * 从服务器请求天气数据
     *
     * @param weatherId
     */
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
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, getString(R.string.get_weather_failed), Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 拉取并加载bing每日一图
     */
    private void loadBingPic() {
        String bingPicUrl = getString(R.string.bing_pic);
        HttpUtil.sendOkHttpRequest(bingPicUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                if (bingPic != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
//                            editor.putString("bing_pic", getString(R.string.bingUrl));
//                            editor.apply();
//                            Glide.with(WeatherActivity.this).load(getString(R.string.bingUrl)).into(bingImageView);
                            Glide.with(WeatherActivity.this).load(bingPic).into(bingImageView);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 加载、显示天气数据
     *
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        if (weather != null && "ok".equals(weather.status)) {
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
            //开启后天服务
            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);
        } else {
            Toast.makeText(this, getString(R.string.get_weather_failed), Toast.LENGTH_SHORT).show();
        }
    }

}
