# WeatherView

自定义View，实现天气折线图


### ScreenShots

<img src="https://github.com/cjhandroid/WeatherView/blob/master/app/src/main/assets/Screenshot_20161127-211920~2.png" />
<img src="https://github.com/cjhandroid/WeatherView/blob/master/app/src/main/assets/ezgif.com-video-to-gif%20(3).gif" />


### v1.0.0

实现天气折线图的基本功能，提供样式的更改，包括，背景颜色，字体大小，温度线的颜色等等一系列方便使用的属性和方法


### How to use

#### gradle

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

  ```
  dependencies {
  	        compile 'com.github.cjhandroid:WeatherView:v1.0.0'
  	}
  ```


### details

#### set datas

first step: create bean, must extends IBaseWeatherData

```
public interface IBaseWeatherData {

    int getHighDegree();

    int getLowDegree();
}

```

```
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

```

second step: call WeatherView's method -- setDatas

```
 /**
     * 设置数据    T extends IBaseWeatherData
     * issue: 1.必须设置最低和最高的温度值，以及要展示的数据的position
     * 2.如果你需要在展示数据的时候，需要设置其它属性，那么这个方法必须在最后才能调用，否则控件不会重绘
     * 3.如果，确实需要动态改变属性，可以继承WeatherView，然后重写属性方法, 在调用父类方法之后，在调用  postInvalidate() 或者 invalidate()
     *
     * @param datas
     * @param highestDegree
     * @param lowestDegree
     * @param position
     */
    public void setDatas(List<T> datas, int highestDegree, int lowestDegree, int position) {
        this.datas = datas;
        this.highestDegree = highestDegree;
        this.lowestDegree = lowestDegree;
        this.position = position;
        postInvalidate();
    }
```


### Create by xml

```
<cjh.weatherviewlibarary.WeatherView
                android:id="@+id/weatherView2"
                android:layout_width="80dp"
                android:layout_height="160dp"
                app:backgroundColor="@color/colorAccent"
                app:dotColor="@android:color/white"
                app:dotRadiu="5dp"
                app:highTxtColor="@color/colorPrimaryDark"
                app:istoday="true"
                app:lineColor="@color/colorPrimaryDark"
                app:lineStrokWidth="1dp"
                app:todayHighDotColor="#228989"
                app:todayHighTxtColor="@android:color/black"
                app:todayLowDotColor="#EAC100"
                app:todayLowTxtColor="@android:color/black"
                app:txtColor="@color/colorPrimary"
                app:txtSize="14sp"
                app:txtToBorder="10dp"
                app:txtToDot="9dp" />
```
              
              
### Create by code

```
 @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new WeatherView<MyWeatherData>(context));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (style == DEFAULT_WEATHERVIEW) {
            holder.weatherView.setHaveMiddleLine(true);
            holder.weatherView.setMiddleLineParams(1, Color.parseColor("#B8860B"));
        } else if (style == SETTING_WEATHERVIEW) {
            holder.weatherView.setWH(weatherViewW, weatherViewW * 2);
            holder.weatherView.setBackgroundColor(Color.parseColor("#22000000"));
            holder.weatherView.setDotParams(MainActivity.instance.dip2px(3), Color.parseColor("#FFFFFF"), Color.parseColor("#00FF00"));
            holder.weatherView.setTxtParams(MainActivity.instance.sp2px(12), Color.parseColor("#00ff00"), MainActivity.instance.dip2px(8), MainActivity.instance.dip2px(10));
            holder.weatherView.setLineParams(MainActivity.instance.dip2px(1), Color.YELLOW, Color.parseColor("#5F9EA0"));
            if (position == 1) {
                holder.weatherView.setToday(true);
                holder.weatherView.setTodayParams(MainActivity.instance.dip2px(3), Color.parseColor("#B8860B"), Color.parseColor("#8B1A1A"));
            } else
                holder.weatherView.setToday(false);
        }
        holder.weatherView.setDatas(datas, highestDegree, lowestDegree, position);
    }
```


### attrs

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="WeatherView">
        <attr name="txtSize" format="dimension"></attr> <!--degree text size -->

        <attr name="dotRadiu" format="dimension"></attr> <!--all dot radiu -->
        <attr name="todayDotRadiu" format="dimension"></attr> <!--today dot radiu -->

        <attr name="dotColor" format="color"></attr> <!--set all the dot color -->
        <attr name="highDotColor" format="color"></attr> <!--set high degree dot color -->
        <attr name="lowDotColor" format="color"></attr> <!--set low degree dot color -->
        <attr name="todayHighDotColor" format="color"></attr> <!--set today high degree dot color -->
        <attr name="todayLowDotColor" format="color"></attr> <!--set today low degree dot color -->


        <attr name="txtToBorder" format="dimension"></attr><!--set high and low degree texts margin to border -->
        <attr name="txtToDot" format="dimension"></attr><!--set high and low degree texts margin to dots -->

        <attr name="txtColor" format="color"></attr><!--set high and low degree texts colors -->
        <attr name="highTxtColor" format="color"></attr><!--set high degree texts colors -->
        <attr name="lowTxtColor" format="color"></attr><!--set low degree texts colors -->
        <attr name="todayHighTxtColor" format="color"></attr><!--set today high degree texts colors -->
        <attr name="todayLowTxtColor" format="color"></attr><!--set today low degree texts colors -->

        <attr name="lineColor" format="color"></attr><!--set all lines colors -->
        <attr name="highLineColor" format="color"></attr><!--set high lines colors -->
        <attr name="lowLineColor" format="color"></attr><!--set low lines colors -->
        <attr name="leftHighLineColor" format="color"></attr><!--set left high lines colors -->
        <attr name="rightHighLineColor" format="color"></attr><!--set right high lines colors -->
        <attr name="leftLowLineColor" format="color"></attr><!--set left low lines colors -->
        <attr name="rightLowLineColor" format="color"></attr><!--set right low lines colors -->
        <attr name="lineStrokWidth" format="dimension"></attr><!--set high and low degree lines paint stroke width -->

        <attr name="haveMiddleLine" format="boolean"></attr><!--set middle line show -->
        <attr name="middleLineColor" format="color"></attr><!--set middle line color -->
        <attr name="middleLineStrokeWidth" format="dimension"></attr><!--set middle line paint stroke width -->

        <attr name="width" format="dimension"></attr><!--set weaherview width -->
        <attr name="height" format="dimension"></attr><!--set weaherview hight -->

        <attr name="backgroundColor" format="color"></attr><!--set weaherview cavans color -->

        <attr name="istoday" format="boolean"></attr>
    </declare-styleable>
</resources>
```

# important
only setData() invalidates WeatherView, if you hope other methods can invalidate WeatherView.
You can create classes that extend WeatherView, and rewrite methods, all the WeatherView's methods are public and protected.
like this:

```
public void setWH(){
    super.setWH();
    postInvalidate();
}
```

https://blog.csdn.net/cjh_android/article/details/53365847
