package com.example.create_keyboard1.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.create_keyboard1.Adapter.ImageAdapter;
import com.example.create_keyboard1.Adapter.MyAdapter;
import com.example.create_keyboard1.Model.ImageModel;
import com.example.create_keyboard1.Model.Model;
import com.example.create_keyboard1.R;

import java.util.ArrayList;


public class ImageFrag extends Fragment {


    public ImageAdapter myAdapter;
    public RecyclerView recyclerView;
    public static ArrayList<ImageModel> myimg=new ArrayList<ImageModel>();;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final  View view=inflater.inflate(R.layout.fragment_image, container, false);
        recyclerView=view.findViewById(R.id.recyclerview);
//        myimg=new ArrayList<ImageModel>();

        myimg.add(new ImageModel(R.drawable.img1,"Dessert"));
        myimg.add(new ImageModel(R.drawable.img2,"Sun Set"));
        myimg.add(new ImageModel(R.drawable.img3,"Rain"));
        myimg.add(new ImageModel(R.drawable.img4,"Water Drop"));
        myimg.add(new ImageModel(R.drawable.img5,"River View"));
        myimg.add(new ImageModel(R.drawable.img6,"Moon Night"));
        myimg.add(new ImageModel(R.drawable.img7,"Nature Evening"));
        myimg.add(new ImageModel(R.drawable.naturetiger,"Tiger in Night"));
        myimg.add(new ImageModel(R.drawable.naturenight1,"Full Moon"));
        myimg.add(new ImageModel(R.drawable.naturefire,"Fire in Night"));
        myimg.add(new ImageModel(R.drawable.naturefire2,"Fire"));

        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter=new ImageAdapter(getContext(),myimg);
        recyclerView.setAdapter(myAdapter);

        return view;
    }
}