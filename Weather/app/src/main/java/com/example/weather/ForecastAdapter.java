package com.example.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>{

    Context context;
    ArrayList<String> castTime, castDegrees, castDesc, castIcons;

    @SuppressWarnings("deprecation")
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){

        @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
        @Override
        public void handleMessage(@NonNull Message msg) {
            castTime = msg.getData().getStringArrayList("times");
            castDegrees = msg.getData().getStringArrayList("degrees");
            castDesc = msg.getData().getStringArrayList("descriptions");
            castIcons = msg.getData().getStringArrayList("icons");

            Log.d("FORECAST HANDLER", castTime.get(0));
            notifyDataSetChanged();
        }
    };

    ForecastAdapter(Context context){
        this.context = context;
        castTime = new ArrayList<>();
        castDegrees = new ArrayList<>();
        castDesc = new ArrayList<>();
        castIcons = new ArrayList<>();

        String cityName = MainActivity.cityName;
        ForecastConnector con = new ForecastConnector(handler, cityName);
        con.start();
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder{

        TextView castTime, castDegrees, castDesc;
        ImageView castIcon;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            castTime = itemView.findViewById(R.id.timeText);
            castDegrees = itemView.findViewById(R.id.degreesText);
            castDesc = itemView.findViewById(R.id.descriptionText);
            castIcon = itemView.findViewById(R.id.iconView);
        }
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.forecast_row, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        holder.castTime.setText(String.valueOf(castTime.get(position)));
        holder.castDegrees.setText(String.valueOf(castDegrees.get(position)));
        holder.castDesc.setText(String.valueOf(castDesc.get(position)));

        String icon = "icon_" + castIcons.get(position);
        int id = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());
        if(id > 0){
            if(holder.castIcon != null){
                holder.castIcon.setImageResource(id);
            }
            else{
                Log.d("HOLDER", String.valueOf(position));
            }
        }
    }

    @Override
    public int getItemCount() {
        return castTime.size();
    }
}
