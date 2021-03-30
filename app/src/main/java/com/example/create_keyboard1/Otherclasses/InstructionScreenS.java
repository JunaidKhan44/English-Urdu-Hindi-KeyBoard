package com.example.create_keyboard1.Otherclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.create_keyboard1.R;

public class InstructionScreenS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_screen_s);
        getSupportActionBar().hide();
    }
}