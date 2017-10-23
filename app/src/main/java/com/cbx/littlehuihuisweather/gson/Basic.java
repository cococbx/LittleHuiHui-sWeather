package com.cbx.littlehuihuisweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 天气基本数据
 * Created by lenovo on 2017/10/23.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {

        @SerializedName("loc")
        public String updateTime;
    }
}
