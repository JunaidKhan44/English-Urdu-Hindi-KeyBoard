package com.example.create_keyboard1.Otherclasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.create_keyboard1.R;

import static com.example.create_keyboard1.Otherclasses.SimpleIME.kv;

public class SettingActivity extends AppCompatActivity {

  public   CheckBox  chkpreview,chkprediction,chkvibrate;
  public CardView   cardbackground,cardenable,cardset,carddisable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle("Keyboard Settings");
        
        initialize();

        chkprediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkprediction.isChecked())
                    Toast.makeText(SettingActivity.this, "prediction is on", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SettingActivity.this, "prediction is off", Toast.LENGTH_SHORT).show();

            }


        });
        chkvibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkvibrate.isChecked())
                    Toast.makeText(SettingActivity.this, "vibrate is on", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SettingActivity.this, "vibrate is off", Toast.LENGTH_SHORT).show();
            }
        });
        chkpreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkpreview.isChecked()){
                    kv.setPreviewEnabled(true);
                    Toast.makeText(SettingActivity.this, "Preview Enable", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(SettingActivity.this, "Preview Disable", Toast.LENGTH_SHORT).show();
                    kv.setPreviewEnabled(false);
                }

            }
        });

        cardbackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Background.class));

            }
        });
        cardenable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent enableIntent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                enableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(enableIntent);
            }
        });
        cardset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMydialog();
            }
        });
        carddisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent enableIntent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                enableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(enableIntent);
            }
        });


        
    }

    private void initialize() {
        
        chkpreview=findViewById(R.id.checked1);
        chkprediction=findViewById(R.id.checked2);
        chkvibrate=findViewById(R.id.checked3);
        cardbackground=findViewById(R.id.t4);
        cardenable=findViewById(R.id.t5);
        cardset=findViewById(R.id.t6);
        carddisable=findViewById(R.id.t7);
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




}