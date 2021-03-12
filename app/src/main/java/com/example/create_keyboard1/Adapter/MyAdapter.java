package com.example.create_keyboard1.Adapter;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.create_keyboard1.Model.Model;
import com.example.create_keyboard1.R;
import com.example.create_keyboard1.SimpleIME;

import java.util.ArrayList;

public class MyAdapter extends  RecyclerView.Adapter<MyAdapter.MyHolder> {

    public Context context;
    ArrayList<Model>   arrayList;

    public MyAdapter(Context context, ArrayList<Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.theme_name.setText(arrayList.get(position).theme_name);
        holder.imgview.setImageResource(arrayList.get(position).getTheme_image());

        holder.dapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleIME.kv.setBackgroundResource(arrayList.get(position).getTheme_image());
                Toast.makeText(context, "Theme Applied..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imgview;
        TextView theme_name;
        Button  dapply;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            this.imgview = itemView.findViewById(R.id.theme_image);
            this.theme_name = itemView.findViewById(R.id.theme_name);
            this.dapply = itemView.findViewById(R.id.download_btn);
        }
    }

}
