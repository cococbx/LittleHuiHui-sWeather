package com.cbx.littlehuihuisweather.db;

import org.litepal.crud.DataSupport;

/**
 * 省份 数据库表
 * Created by lenovo on 2017/10/19.
 */

public class Province extends DataSupport {
    private int id;
    private String provinceName;//名字
    private int provinceCode;//省份代号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
