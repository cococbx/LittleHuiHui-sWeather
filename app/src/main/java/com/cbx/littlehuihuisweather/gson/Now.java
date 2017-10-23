package com.cbx.littlehuihuisweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 实况天气
 * Created by lenovo on 2017/10/23.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    /**
     * "cond": {
     "txt": "晴"
     },
     */
    @SerializedName("cond")//天气状况
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;
    }
}
