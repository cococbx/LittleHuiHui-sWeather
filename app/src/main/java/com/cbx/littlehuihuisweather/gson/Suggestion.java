package com.cbx.littlehuihuisweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 天气 生活指数
 * Created by lenovo on 2017/10/23.
 */

public class Suggestion {

    public Air air;//空气指数

    @SerializedName("comf")
    public Comfort comfort;//舒适度指数

    @SerializedName("cw")
    public CarWash carWash;//洗车指数

    @SerializedName("drsg")
    public Dress dress;//穿衣指数

    @SerializedName("flu")
    public Influenza influenza;//感冒指数

    public Sport sport;//	运动指数

    @SerializedName("trav")
    public Travel travel;//旅游指数

    public Uv uv;//紫外线指数


    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;
    }

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public class Air {
        @SerializedName("txt")
        public String info;
    }

    public class Dress {
        @SerializedName("txt")
        public String info;
    }

    public class Influenza {
        @SerializedName("txt")
        public String info;
    }

    public class Travel {
        @SerializedName("txt")
        public String info;
    }

    public class Uv {
        @SerializedName("txt")
        public String info;
    }
}
