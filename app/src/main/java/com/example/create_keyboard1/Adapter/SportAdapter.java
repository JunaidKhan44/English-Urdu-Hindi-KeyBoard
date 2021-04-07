package com.example.create_keyboard1.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.create_keyboard1.Model.SportModel;
import com.example.create_keyboard1.R;
import com.example.create_keyboard1.Otherclasses.SimpleIME;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SportAdapter extends  RecyclerView.Adapter<SportAdapter.MyHolder> {

    public Context context;
    ArrayList<SportModel> arrayList;



    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String GROUPSNAME_SHARED_PREF = "groupname";
    public static final String GROUPSNAME_THEME = "themename";
    public static final String POSITION_AD="pos";
    private SharedPreferences preferencesmine;

    public SportAdapter(Context context, ArrayList<SportModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public SportAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_item, null);
        return new SportAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SportAdapter.MyHolder holder, int position) {

        holder.theme_name.setText(arrayList.get(position).theme_name);
        holder.imgview.setImageResource(arrayList.get(position).getTheme_image());

        holder.dapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences1 = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString(GROUPSNAME_SHARED_PREF, "sport");
                editor.putString(GROUPSNAME_THEME,arrayList.get(position).theme_name);
                editor.putInt(POSITION_AD,position);
                editor.commit();
                editor.apply();
                Toast.makeText(context,"Background Selected..", Toast.LENGTH_SHORT).show();
            }
        });

        if(loadsharedString().equalsIgnoreCase("sport")){
            if(loadshared().equalsIgnoreCase(arrayList.get(position).theme_name)){
                holder.dapply.setBackgroundColor(Color.GRAY);}
            else{
                holder.dapply.setBackgroundColor(context.getResources().getColor(R.color.buttoncolor));
            }
        }
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
            this.imgview = itemView.findViewById(R.id.theme_image);
            this.theme_name = itemView.findViewById(R.id.theme_name);
            this.dapply = itemView.findViewById(R.id.download_btn);
        }
    }

    private String loadshared() {
        preferencesmine = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String val;
        val = preferencesmine.getString(GROUPSNAME_THEME, "");
        return  val;
    }
    private String loadsharedString() {
        preferencesmine = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String val;
        val = preferencesmine.getString(GROUPSNAME_SHARED_PREF, "");
        if(!val.isEmpty()){
            return  val;
        }
        else {
            return "";
        }
    }

}
