package com.example.create_keyboard1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.create_keyboard1.Adapter.MyAdapter;
import com.example.create_keyboard1.Model.Model;

import java.util.ArrayList;

public class GradientFrag extends Fragment {


    public MyAdapter myAdapter;
    public RecyclerView recyclerView;
    public ArrayList<Model> mydata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final  View view=inflater.inflate(R.layout.fragment_gradient, container, false);
        recyclerView=view.findViewById(R.id.recyclerview);
        mydata=new ArrayList<Model>();
        mydata.add(new Model(R.drawable.image,"Neon Rec"));
        mydata.add(new Model(R.drawable.picart,"D Ball"));
        mydata.add(new Model(R.drawable.second,"Neon Rings"));
        mydata.add(new Model(R.drawable.image,"Balls"));


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter=new MyAdapter(getContext(),mydata);
        recyclerView.setAdapter(myAdapter);

        return view;
    }
}