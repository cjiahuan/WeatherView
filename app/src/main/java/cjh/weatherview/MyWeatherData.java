package cjh.weatherview;

import cjh.weatherviewlibarary.IBaseWeatherData;

/**
 * Created by cjh on 16-11-23.
 */

public class MyWeatherData implements IBaseWeatherData {

    public int highDegree;
    public int lowDegree;

    public MyWeatherData() {
    }

    public MyWeatherData(int highDegree, int lowDegree) {
        this.highDegree = highDegree;
        this.lowDegree = lowDegree;
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
