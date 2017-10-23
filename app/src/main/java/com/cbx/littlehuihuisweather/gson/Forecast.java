package com.cbx.littlehuihuisweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 天气预测
 * Created by lenovo on 2017/10/23.
 */

public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")//天气状况
    public ForecastMore forecastMore;


    public class Temperature {
        public String max;
        public String min;
    }

    public class ForecastMore {
        @SerializedName("txt_d")
        public String info;
    }
}
