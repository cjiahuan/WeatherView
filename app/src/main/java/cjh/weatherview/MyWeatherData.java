package cjh.weatherview;

import cjh.weatherviewlibarary.IBaseWeatherData;

/**
 * Created by cjh on 16-11-23.
 */

public class MyWeatherData implements IBaseWeatherData {

    public int highDegree;
    public int lowDegree;
    public String date;

    public MyWeatherData() {
    }

    public MyWeatherData(int highDegree, int lowDegree, String date) {
        this.highDegree = highDegree;
        this.lowDegree = lowDegree;
        this.date = date;
    }

    @Override
    public int getHighDegree() {
        return highDegree;
    }

    @Override
    public int getLowDegree() {
        return lowDegree;
    }
}
