package com.example.create_keyboard1.Otherclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.create_keyboard1.R;

public class ExitScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_screen);
        getSupportActionBar().hide();
    }

    public void exitFun(View view) {


        System.exit(0);
        finishAffinity();
    }

    public void retainFun(View view) {

        startActivity(new Intent(getApplicationContext(),MainMenu.class));
        finish();
    }
}