package com.weatherdemo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weatherdemo.utils.Constants;
import com.weatherdemo.models.NextDaysTemp;
import com.weatherdemo.R;
import com.weatherdemo.utils.Helper;

import java.util.ArrayList;

/**
 * Created by Dilip on 27-06-2016.
 */
public class NextDaysWeatherAdapter extends RecyclerView.Adapter<NextDaysWeatherAdapter.ViewHolder> {

    ArrayList<NextDaysTemp> nextDaysTempArrayList;
    Context mContext;

    public NextDaysWeatherAdapter(Context context, ArrayList<NextDaysTemp> nextDaysTempArrayList) {
        this.mContext = context;
        this.nextDaysTempArrayList = nextDaysTempArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvWeatherDay.setText("" + Helper.getDate(Long.parseLong(nextDaysTempArrayList.get(position).getDt()) * 1000));
        holder.tvDay.setText("" + nextDaysTempArrayList.get(position).getTemp().getDay() + " \u2103");
        holder.tvNight.setText("" + nextDaysTempArrayList.get(position).getTemp().getNight() + " \u2103");
        holder.tvWeatherCondition.setText("" + nextDaysTempArrayList.get(position).getWeather().get(0).getDescription());
        holder.tvWind.setText(nextDaysTempArrayList.get(position).getSpeed() + " m/s");
        holder.tvCoud.setText("clouds " + nextDaysTempArrayList.get(position).getClouds() + " %" + ", " + nextDaysTempArrayList.get(position).getPressure() + " hpa");

        if (nextDaysTempArrayList.get(position).getWeather().get(0).getDescription().equalsIgnoreCase(Constants.SKY_IS_CLEAR)) {
            holder.ivWeatherImage.setImageResource(R.mipmap.sky_clear);
        } else if (nextDaysTempArrayList.get(position).getWeather().get(0).getDescription().equalsIgnoreCase(Constants.LIGHT_RAIN)) {
            holder.ivWeatherImage.setImageResource(R.mipmap.light_rain);
        } else if (nextDaysTempArrayList.get(position).getWeather().get(0).getDescription().equalsIgnoreCase(Constants.MODERATE_RAIN)) {
            holder.ivWeatherImage.setImageResource(R.mipmap.light_rain);
        } else if (nextDaysTempArrayList.get(position).getWeather().get(0).getDescription().equalsIgnoreCase(Constants.HEAVY_RAIN)) {
            holder.ivWeatherImage.setImageResource(R.mipmap.light_rain);
        } else if (nextDaysTempArrayList.get(position).getWeather().get(0).getDescription().equalsIgnoreCase(Constants.SCATTERED_CLOUD)) {
            holder.ivWeatherImage.setImageResource(R.mipmap.scatter);
        } else if (nextDaysTempArrayList.get(position).getWeather().get(0).getDescription().equalsIgnoreCase(Constants.VERY_HEAVY_RAIN)) {
            holder.ivWeatherImage.setImageResource(R.mipmap.light_rain);
        }

    }

    @Override
    public int getItemCount() {
        return nextDaysTempArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvWeatherDay, tvWeatherMonth, tvDay, tvNight, tvWeatherCondition, tvWind, tvCoud;
        ImageView ivWeatherImage;

        public ViewHolder(View itemView) {
            super(itemView);

            tvWeatherDay = (TextView) itemView.findViewById(R.id.tvWeatherDay);
            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            tvNight = (TextView) itemView.findViewById(R.id.tvNight);
            tvWeatherCondition = (TextView) itemView.findViewById(R.id.tvWeatherCondition);
            tvWind = (TextView) itemView.findViewById(R.id.tvWind);
            tvCoud = (TextView) itemView.findViewById(R.id.tvCoud);
            ivWeatherImage = (ImageView) itemView.findViewById(R.id.ivWeatherImage);

        }

    }
}
