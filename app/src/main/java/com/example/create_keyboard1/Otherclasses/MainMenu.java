package com.example.create_keyboard1.Otherclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.create_keyboard1.R;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();
    }

    public void enablingKeyboard(View view) {

        Intent enableIntent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
        enableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(enableIntent);
    }

    public void setkeyboarddialog(View view) {

        showMydialog();

    }

    private void showMydialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Alert.!")
                .setMessage("Make sure to enable first to see keyboard in the list.If you don't see the keyboard in the list enable it first.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        InputMethodManager imeManager = (InputMethodManager)
                                getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
                        imeManager.showInputMethodPicker();
                        finish();
                    }
                })
                .show();
    }

    public void disablekeyboard(View view) {
        Intent enableIntent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
        enableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(enableIntent);
    }

    public void showbackground(View view) {
        startActivity(new Intent(getApplicationContext(),Background.class));
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(),ExitScreen.class));
        finish();
    }
}