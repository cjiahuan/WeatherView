package cjh.weatherview;

import cjh.weatherviewlibarary.BaseWeatherData;

/**
 * Created by cjh on 16-11-23.
 */

public class MyWeatherData extends BaseWeatherData {

    public MyWeatherData() {
    }

    public MyWeatherData(int highDegree, int lowDegree) {
        this.highDegree = highDegree;
        this.lowDegree = lowDegree;
    }

}
