package com.example.create_keyboard1.Adapter;

import android.content.Context;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.create_keyboard1.Model.KeyThemeModel;
import com.example.create_keyboard1.R;

import java.util.ArrayList;

public class KeyThemeAdapter extends RecyclerView.Adapter<KeyThemeAdapter.MyHolder> {



    public static final String SHARED_PREF_NAME1 = "themerelated";
    public static final String GROUPSNAME_SHARED_PREF1 = "grouptheme";
    public static final String POSITION_AD1="pos";


    public Context context;
    ArrayList<KeyThemeModel> arrayList;


    public KeyThemeAdapter(Context context, ArrayList<KeyThemeModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.keytheme_items, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.theme_name.setText(arrayList.get(position).theme_name);
        holder.imgview.setImageResource(arrayList.get(position).getTheme_image());

        holder.dapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences1 = context.getSharedPreferences(SHARED_PREF_NAME1, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString(GROUPSNAME_SHARED_PREF1, "theme");
                editor.putInt(POSITION_AD1,position);
                editor.commit();
                editor.apply();
                Toast.makeText(context,"Theme Applied..", Toast.LENGTH_SHORT).show();
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
        Button dapply;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            this.imgview = itemView.findViewById(R.id.theme_image1);
            this.theme_name = itemView.findViewById(R.id.theme_name1);
            this.dapply = itemView.findViewById(R.id.download_btn1);
        }
    }
}
