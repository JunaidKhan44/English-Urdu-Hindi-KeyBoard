package com.example.create_keyboard1.Otherclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.create_keyboard1.R;


public class CamGalleryBackground extends AppCompatActivity {


    ImageView  choose,setbackground,imageView;
    private static final int SELECT_PICTURE = 1;
    public  Uri selectedImageUri;
    public static final String SHARED_PREF_CUSTOMBACK = "mycustombackground";
    public static final String SHARED_PREF_BACKID = "custback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_gallery_background);

        choose=findViewById(R.id.choose);
        setbackground=findViewById(R.id.setbackground);
        imageView=findViewById(R.id.imageid);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });


        setbackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  val= String.valueOf(selectedImageUri);
                if(val!=null) {
                    SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences(SHARED_PREF_CUSTOMBACK, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    editor.putString(SHARED_PREF_BACKID, String.valueOf(selectedImageUri));
                    editor.commit();
                    editor.apply();
                    Toast.makeText(CamGalleryBackground.this, "Background set...", Toast.LENGTH_SHORT).show();
                    finish();
                }


            }
        });
        
        
        
        
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                 selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
              //  selectedImagePath = getPath(selectedImageUri);


            }
        }
    }




}