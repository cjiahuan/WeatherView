package cjh.weatherview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cjh.weatherviewlibarary.WeatherView;

/**
 * Created by cjh on 16-11-27.
 */

public class CompleteShowAdapter extends RecyclerView.Adapter<CompleteShowAdapter.ViewHolder> {

    private List<MyWeatherData> datas = new ArrayList<>();

    private int highestDegree, lowestDegree;

    private Context context;
    private int weatherViewW;

    public CompleteShowAdapter(Context context, List<MyWeatherData> datas, int highestDegree, int lowestDegree, int weatherViewW) {
        this.context = context;
        this.datas = datas;
        this.highestDegree = highestDegree;
        this.lowestDegree = lowestDegree;
        this.weatherViewW = weatherViewW;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.beta_weather, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyWeatherData myWeatherData = datas.get(position);
        ((TextView) (holder.view.findViewById(R.id.date1))).setText(myWeatherData.date);
        ((WeatherView<MyWeatherData>) (holder.view.findViewById(R.id.weatherView))).setWH(weatherViewW, weatherViewW * 2);
        ((WeatherView<MyWeatherData>) (holder.view.findViewById(R.id.weatherView))).setDatas(datas, highestDegree, lowestDegree, position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }

}
