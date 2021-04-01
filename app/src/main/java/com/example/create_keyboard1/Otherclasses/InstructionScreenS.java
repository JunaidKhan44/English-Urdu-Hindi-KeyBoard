package com.example.create_keyboard1.Otherclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.create_keyboard1.R;
import com.google.firebase.analytics.FirebaseAnalytics;

public class InstructionScreenS extends AppCompatActivity {


    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_screen_s);
        getSupportActionBar().hide();



        //analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "4");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Instructions");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}