package com.example.create_keyboard1.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.create_keyboard1.Adapter.MyAdapter;
import com.example.create_keyboard1.Model.Model;
import com.example.create_keyboard1.R;

import java.util.ArrayList;

public class GradientFrag extends Fragment {


    public MyAdapter myAdapter;
    public RecyclerView recyclerView;
    public static ArrayList<Model> mygradient=new ArrayList<Model>();;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final  View view=inflater.inflate(R.layout.fragment_gradient, container, false);
        recyclerView=view.findViewById(R.id.recyclerview);
//        mygradient=new ArrayList<Model>();
        mygradient.add(new Model(R.drawable.gradient_0,"One"));
        mygradient.add(new Model(R.drawable.gradient_1,"Two"));
        mygradient.add(new Model(R.drawable.gradient_2,"Three"));
        mygradient.add(new Model(R.drawable.gradient_3,"Four"));
        mygradient.add(new Model(R.drawable.gradient_4,"Five"));
        mygradient.add(new Model(R.drawable.gradient_5,"Six"));
        mygradient.add(new Model(R.drawable.gradient_6,"Seven"));
        mygradient.add(new Model(R.drawable.gradient_7,"Eight"));
        mygradient.add(new Model(R.drawable.gradient_8,"Nine"));




        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter=new MyAdapter(getContext(),mygradient);
        recyclerView.setAdapter(myAdapter);

        return view;
    }
}