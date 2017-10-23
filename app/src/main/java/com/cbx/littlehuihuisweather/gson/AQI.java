package com.cbx.littlehuihuisweather.gson;

/**
 * 天气 空气质量指数
 * Created by lenovo on 2017/10/23.
 */

public class AQI {

    public AQICity aqiCity;

    public class AQICity {

        public String aqi;

        public String pm25;
    }
}
