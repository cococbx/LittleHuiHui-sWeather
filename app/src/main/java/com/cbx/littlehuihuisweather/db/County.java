package com.cbx.littlehuihuisweather.db;

import org.litepal.crud.DataSupport;

/**
 * 区、县 数据库表
 * Created by lenovo on 2017/10/19.
 */

public class County extends DataSupport {
    private int id;
    private String countyName;
    private String weatherId;//天气id
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
