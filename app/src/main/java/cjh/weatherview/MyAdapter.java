package cjh.weatherview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cjh.weatherviewlibarary.WeatherView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<MyWeatherData> datas = new ArrayList<>();

    private int highestDegree, lowestDegree;

    private Context context;

    public static final int DEFAULT_WEATHERVIEW = 0;

    public static final int SETTING_WEATHERVIEW = 1;

    private int style;

    private int weatherViewW;

    public MyAdapter(Context context, List<MyWeatherData> datas, int highestDegree, int lowestDegree, int weatherViewW, int style) {
        this.context = context;
        this.datas = datas;
        this.highestDegree = highestDegree;
        this.lowestDegree = lowestDegree;
        this.weatherViewW = weatherViewW;
        this.style = style;
    }

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
            holder.weatherView.setTxtParams(MainActivity.instance.sp2px(12), Color.parseColor("#00ff00"), MainActivity.instance.dip2px(8), MainActivity.instance.dip2px(5));
            holder.weatherView.setLineParams(MainActivity.instance.dip2px(1), Color.YELLOW, Color.parseColor("#5F9EA0"));
            if (position == 1) {
                holder.weatherView.setToday(true);
                holder.weatherView.setTodayParams(MainActivity.instance.dip2px(3), Color.parseColor("#B8860B"), Color.parseColor("#8B1A1A"));
            } else
                holder.weatherView.setToday(false);
        }
        holder.weatherView.setDatas(datas, highestDegree, lowestDegree, position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public WeatherView weatherView;

        public ViewHolder(View itemView) {
            super(itemView);
            weatherView = (WeatherView) itemView;
        }
    }
}