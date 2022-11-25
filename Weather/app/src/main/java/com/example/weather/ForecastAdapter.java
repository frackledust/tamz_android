package com.example.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
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

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    Context context;
    ArrayList<String> castTime, castDegrees, castDesc, castIcons;
    Handler handler;

    ForecastAdapter(Context context, Handler handler, ArrayList<String> castTime, ArrayList<String> castDegrees,
                    ArrayList<String> castDesc, ArrayList<String> castIcons) {
        this.context = context;
        this.castTime = castTime;
        this.castDegrees = castDegrees;
        this.castDesc = castDesc;
        this.castIcons = castIcons;
        this.handler = handler;
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {

        TextView castTime, castDegrees, castDesc;
        ImageView castIcon;
        CardView cardView;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            castTime = itemView.findViewById(R.id.timeText);
            castDegrees = itemView.findViewById(R.id.degreesText);
            castDesc = itemView.findViewById(R.id.descriptionText);
            castIcon = itemView.findViewById(R.id.icon_in_row);
            cardView = itemView.findViewById(R.id.card_view);
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
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.castTime.setText(String.valueOf(castTime.get(position)));
        holder.castDegrees.setText(String.valueOf(castDegrees.get(position)));
        holder.castDesc.setText(String.valueOf(castDesc.get(position)));

        String icon = "icon_" + castIcons.get(position);
        @SuppressLint("DiscouragedApi")
        int id = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());
        if (id > 0) {
            if (holder.castIcon != null) {
                holder.castIcon.setImageResource(id);
            } else {
                Log.d("HOLDER ", "Error on icon: " + position
                );
            }
        }

        holder.cardView.setOnClickListener(v -> {
            Log.d("CLICK", "Clicked on card: " + position);
            Bundle b = new Bundle();
            b.putString("temperature", castDegrees.get(position));
            b.putString("description", castDesc.get(position));
            b.putString("icon", castIcons.get(position));

            Message m = handler.obtainMessage();
            m.setData(b);
            m.sendToTarget();
        });
    }

    @Override
    public int getItemCount() {
        return castTime.size();
    }
}
