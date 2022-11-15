package com.vsb.kru13.sokoban;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    LevelsRecycledActivity parent;

    ArrayList level_id, level_title, level_moves, level_data;
    CustomAdapter(Context context, LevelsRecycledActivity activity, ArrayList<String> level_id,
                  ArrayList level_title, ArrayList level_moves, ArrayList level_data){
        this.context = context;
        this.parent = activity;
        this.level_id = level_id;
        this.level_title = level_title;
        this.level_moves = level_moves;
        this.level_data = level_data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView level_id_text, level_title_text, level_moves_text;
        public CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            level_id_text = itemView.findViewById(R.id.text_id);
            level_title_text = itemView.findViewById(R.id.text_title);
            level_moves_text = itemView.findViewById(R.id.text_moves);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.level_id_text.setText(String.valueOf(level_id.get(position)));
        holder.level_title_text.setText(String.valueOf(level_title.get(position)));
        holder.level_moves_text.setText(String.valueOf(level_moves.get(position)));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.Finish(String.valueOf(level_id.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return level_id.size();
    }
}
