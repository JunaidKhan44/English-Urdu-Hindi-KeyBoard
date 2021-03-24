package com.example.create_keyboard1.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.create_keyboard1.Adapter.FlagAdapter;
import com.example.create_keyboard1.Adapter.ImageAdapter;
import com.example.create_keyboard1.Model.FlagModel;
import com.example.create_keyboard1.Model.ImageModel;
import com.example.create_keyboard1.R;

import java.util.ArrayList;

public class FlagsFrag extends Fragment {



    public FlagAdapter myAdapter;
    public RecyclerView recyclerView;
    public static  ArrayList<FlagModel> myflag=new ArrayList<FlagModel>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final  View view=inflater.inflate(R.layout.fragment_flags, container, false);
        recyclerView=view.findViewById(R.id.recyclerview);
//        myflag=new ArrayList<FlagModel>();

        myflag.add(new FlagModel(R.drawable.pakistan,"Pakistan"));
        myflag.add(new FlagModel(R.drawable.asia,"Pak Map"));
        myflag.add(new FlagModel(R.drawable.usa,"Usa"));
        myflag.add(new FlagModel(R.drawable.brazil,"Brazil"));
        myflag.add(new FlagModel(R.drawable.india,"India"));
        myflag.add(new FlagModel(R.drawable.mexico,"Mexico"));
        myflag.add(new FlagModel(R.drawable.peru,"Peru"));
        myflag.add(new FlagModel(R.drawable.barbados,"Barbados"));
        myflag.add(new FlagModel(R.drawable.cuba,"Cuba"));
        myflag.add(new FlagModel(R.drawable.uruguay,"Uruguay"));
        myflag.add(new FlagModel(R.drawable.flagrace,"Flag"));


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter=new FlagAdapter(getContext(),myflag);
        recyclerView.setAdapter(myAdapter);

        return view;
    }
}