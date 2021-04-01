package com.example.create_keyboard1.Otherclasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.create_keyboard1.Adapter.FlagAdapter;
import com.example.create_keyboard1.Adapter.KeyThemeAdapter;
import com.example.create_keyboard1.Model.FlagModel;
import com.example.create_keyboard1.Model.KeyThemeModel;
import com.example.create_keyboard1.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class KeyboardThemeActivity extends AppCompatActivity {


    public KeyThemeAdapter myAdapter;
    public RecyclerView recyclerView;
    public static ArrayList<KeyThemeModel> myflag=new ArrayList<KeyThemeModel>();
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_theme);



        //analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "3");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Themes");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        recyclerView=findViewById(R.id.keyboardthemekey);

        myflag.add(new KeyThemeModel(R.drawable.theme1,"Theme: 1"));
        myflag.add(new KeyThemeModel(R.drawable.theme2,"Theme: 2"));
        myflag.add(new KeyThemeModel(R.drawable.theme3,"Theme: 3"));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter=new KeyThemeAdapter(getApplicationContext(),myflag);
        recyclerView.setAdapter(myAdapter);


    }

}