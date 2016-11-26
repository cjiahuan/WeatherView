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

    private RecyclerView recyclerView, recyclerView1;
    DisplayMetrics dm;

    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int mScreenWidth = dm.widthPixels;

        //default WeatherView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<MyWeatherData> datas = new ArrayList<>();
        datas.add(new MyWeatherData(0, -8));
        datas.add(new MyWeatherData(3, -6));
        datas.add(new MyWeatherData(4, -6));
        datas.add(new MyWeatherData(4, -5));
        datas.add(new MyWeatherData(6, -4));
        datas.add(new MyWeatherData(6, -5));
        datas.add(new MyWeatherData(3, -3));
        datas.add(new MyWeatherData(8, -8));
        recyclerView.setAdapter(new MyAdapter(this, datas, 8, -8, mScreenWidth / 6, MyAdapter.DEFAULT_WEATHERVIEW));


        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerView1);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        recyclerView1.setAdapter(new MyAdapter(this, datas, 8, -8, mScreenWidth / 6, MyAdapter.SETTING_WEATHERVIEW));
    }


    public int dip2px(float dip) {
        return (int) (dip * dm.density + 0.5);
    }

    public int sp2px(float spValue) {
        return (int) (spValue * dm.scaledDensity + 0.5f);
    }
}
