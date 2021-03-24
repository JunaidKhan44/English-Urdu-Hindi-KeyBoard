package com.example.create_keyboard1.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
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
import com.example.create_keyboard1.Otherclasses.SimpleIME;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MyAdapter extends  RecyclerView.Adapter<MyAdapter.MyHolder> {

    public Context context;
    ArrayList<Model>   arrayList;


    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String GROUPSNAME_SHARED_PREF = "groupname";
    public static final String POSITION_AD="pos";


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

               // Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),arrayList.get(position).getTheme_image());

//                Drawable drawable = context.getResources().getDrawable(arrayList.get(position).getTheme_image());
//                Bitmap bmp = ((BitmapDrawable)drawable).getBitmap();
//
//                SharedPreferences sharedPreferences1 = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences1.edit();
//                editor.putString(GROUPSNAME_SHARED_PREF, encodeTobase64(bmp));
//                editor.commit();
//                editor.apply();

                SharedPreferences sharedPreferences1 = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString(GROUPSNAME_SHARED_PREF, "gradient");
                editor.putInt(POSITION_AD,position);
                editor.commit();
                editor.apply();
                Toast.makeText(context,"Background Selected..", Toast.LENGTH_SHORT).show();
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


    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }


}
