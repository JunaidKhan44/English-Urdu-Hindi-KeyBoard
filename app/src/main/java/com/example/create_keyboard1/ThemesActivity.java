package com.example.create_keyboard1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Display;

import com.example.create_keyboard1.Adapter.MyAdapter;
import com.example.create_keyboard1.Model.Model;

import java.util.ArrayList;

public class ThemesActivity extends AppCompatActivity {


    MyAdapter  myAdapter;
    RecyclerView  recyclerView;
    ArrayList<Model> mydata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        getSupportActionBar().hide();

        recyclerView=findViewById(R.id.recyclerview);
        mydata=new ArrayList<Model>();
        mydata.add(new Model(R.drawable.image,"Neon Rec"));
        mydata.add(new Model(R.drawable.picart,"D Ball"));
        mydata.add(new Model(R.drawable.second,"Neon Rings"));
        mydata.add(new Model(R.drawable.image,"Balls"));


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter=new MyAdapter(this,mydata);
        recyclerView.setAdapter(myAdapter);

    }
}