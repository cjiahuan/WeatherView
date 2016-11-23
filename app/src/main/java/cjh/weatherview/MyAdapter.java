package cjh.weatherview;

import android.content.Context;
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

    private int weatherViewW;

    public MyAdapter(Context context, List<MyWeatherData> datas, int highestDegree, int lowestDegree, int weatherViewW) {
        this.context = context;
        this.datas = datas;
        this.highestDegree = highestDegree;
        this.lowestDegree = lowestDegree;
        this.weatherViewW = weatherViewW;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new WeatherView<MyWeatherData>(context));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.weatherView.setW(weatherViewW);
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