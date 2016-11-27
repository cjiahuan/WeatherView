package cjh.weatherview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cjh.weatherviewlibarary.WeatherView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView2, recyclerView, recyclerView1;
    DisplayMetrics dm;

    private WeatherView<MyWeatherData> weatherView1, weatherView2, weatherView3;

    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int mScreenWidth = dm.widthPixels;

        List<MyWeatherData> datas = new ArrayList<>();
        datas.add(new MyWeatherData(0, -8, "昨天"));
        datas.add(new MyWeatherData(3, -6, "今天"));
        datas.add(new MyWeatherData(4, -6, "星期一"));
        datas.add(new MyWeatherData(4, -5, "星期二"));
        datas.add(new MyWeatherData(6, -4, "星期三"));
        datas.add(new MyWeatherData(0, -8, "星期四"));
        datas.add(new MyWeatherData(3, -6, "星期五"));
        datas.add(new MyWeatherData(4, -6, "星期六"));
        datas.add(new MyWeatherData(4, -5, "星期日"));
        datas.add(new MyWeatherData(6, -5, "星期一"));
        datas.add(new MyWeatherData(3, -3, "星期二"));
        datas.add(new MyWeatherData(8, -8, "星期三"));
        datas.add(new MyWeatherData(0, -8, "星期四"));
        datas.add(new MyWeatherData(3, -6, "星期五"));
        datas.add(new MyWeatherData(4, -6, "星期六"));

        recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView2);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(linearLayoutManager2);
        recyclerView2.setAdapter(new CompleteShowAdapter(this, datas, 8, -8, mScreenWidth / 6));


        //default WeatherView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(new MyAdapter(this, datas, 8, -8, mScreenWidth / 6, MyAdapter.DEFAULT_WEATHERVIEW));


        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerView1);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        recyclerView1.setAdapter(new MyAdapter(this, datas, 8, -8, mScreenWidth / 6, MyAdapter.SETTING_WEATHERVIEW));

        weatherView1 = (WeatherView<MyWeatherData>) findViewById(R.id.weatherView1);
        weatherView2 = (WeatherView<MyWeatherData>) findViewById(R.id.weatherView2);
        weatherView3 = (WeatherView<MyWeatherData>) findViewById(R.id.weatherView3);
        weatherView1.setDatas(datas, 8, -8, 0);
        weatherView2.setDatas(datas, 8, -8, 1);
        weatherView3.setDatas(datas, 8, -8, 2);
    }


    public int dip2px(float dip) {
        return (int) (dip * dm.density + 0.5);
    }

    public int sp2px(float spValue) {
        return (int) (spValue * dm.scaledDensity + 0.5f);
    }
}
