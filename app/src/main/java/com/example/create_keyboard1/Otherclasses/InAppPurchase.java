package com.example.create_keyboard1.Otherclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.create_keyboard1.R;

public class InAppPurchase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_purchase);
        getSupportActionBar().hide();
    }

    public void movetomain(View view) {

        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        finish();
    }
}