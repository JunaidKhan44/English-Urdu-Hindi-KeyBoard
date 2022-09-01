package com.example.create_keyboard1.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.create_keyboard1.adapter.SportAdapter;
import com.example.create_keyboard1.model.SportModel;
import com.example.create_keyboard1.R;

import java.util.ArrayList;


public class SportsFrag extends Fragment {



    public SportAdapter myAdapter;
    public RecyclerView recyclerView;
    public static ArrayList<SportModel> mysport=new ArrayList<SportModel>();;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final  View view=inflater.inflate(R.layout.fragment_sports, container, false);
        recyclerView=view.findViewById(R.id.recyclerview);
//        mysport=new ArrayList<SportModel>();

        mysport.add(new SportModel(R.drawable.spt1,"Cricket"));
        mysport.add(new SportModel(R.drawable.spt2,"Badminton"));
        mysport.add(new SportModel(R.drawable.spt3,"Bike Race"));
        mysport.add(new SportModel(R.drawable.spt4,"Race"));
        mysport.add(new SportModel(R.drawable.spt5,"Archery"));
        mysport.add(new SportModel(R.drawable.spt6,"Boat Race"));
        mysport.add(new SportModel(R.drawable.spt7,"Foot Ball"));
        mysport.add(new SportModel(R.drawable.spthorserace,"Horse race"));
        mysport.add(new SportModel(R.drawable.sptscnooker,"Snooker"));
        mysport.add(new SportModel(R.drawable.flagrace,"Car Race"));



        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter=new SportAdapter(getContext(),mysport);
        recyclerView.setAdapter(myAdapter);

        return view;

    }
}