package com.example.create_keyboard1.otherclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.create_keyboard1.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;


public class CamGalleryBackground extends AppCompatActivity {


    private static final int PERMISSION_CODE = 1;
    ImageView choose, setbackground, imageView;
    public static Uri uri;
    public static final String CUSTOM_MODE = "custom_mode";
    public static final String CUSTOM_KEY = "custom_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_gallery_background);

        choose = findViewById(R.id.choose);
        setbackground = findViewById(R.id.setbackground);
        imageView = findViewById(R.id.imageid);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();

            }
        });
        setbackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap=null;

                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O){
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), CamGalleryBackground.uri);
                    try {
                        bitmap = ImageDecoder.decodeBitmap(source);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), CamGalleryBackground.uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(bitmap!=null){
                SharedPreferences sharedPreferences1 = getSharedPreferences(CUSTOM_MODE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString(CUSTOM_KEY,encodeTobase64(bitmap));
                editor.commit();
                editor.apply();
                Toast.makeText(CamGalleryBackground.this, "Bitmap saved", Toast.LENGTH_SHORT).show();
                setSharedValue();
            }
            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();
                imageView.setImageURI(uri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    // method for bitmap to base64
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }


    //choose img and pick gallery fun
    private void chooseimage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);
            } else {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        } else {

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }


    }

    public void saveToGallery(Bitmap b) {

        FileOutputStream fileOutputStream = null;

        String filename = String.format("%d.jpg", System.currentTimeMillis());
        File outfile = new File(createdir("English-Urdu-Hindi", "Picture") + "/", filename);

        Toast.makeText(this, "Image Saved successfully..", Toast.LENGTH_SHORT).show();


        try {
            fileOutputStream = new FileOutputStream(outfile);
            b.compress(Bitmap.CompressFormat.JPEG
                    , 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outfile));
            sendBroadcast(intent);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public File createdir(String dirName, String subDir) {
        File file;
        if (subDir != null) {
            file = new File(getExternalFilesDirs(null) + "/" + dirName + "/" + subDir);
            Toast.makeText(this, "if case", Toast.LENGTH_SHORT).show();
        } else {
            file = new File(getExternalFilesDirs(null) + "/" + dirName);

        }
        if (!file.exists()) {
            file.mkdir();
        }
        Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_SHORT).show();

        return file;

    }

    private void dummymethod(Bitmap b) {

        File storedirir = new File(Environment.getExternalStorageDirectory(), "FbWADownloader/WaImage");
        File myDir = new File(String.valueOf(storedirir));
        myDir.mkdirs();


//        File myDir = new File(Environment.getExternalStorageDirectory().getPath() + "/My AppMaker");
////        String filename = String.format("%d.jpg", System.currentTimeMillis());
////        File outfile = new File(myDir, filename);
//        if (!myDir.exists()) {
//            myDir.mkdirs();
//        }
//        String filename = String.format("%d.jpg", System.currentTimeMillis());
//        File outfile = new File(myDir, filename);
//
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(outfile);
//            b.compress(Bitmap.CompressFormat.JPEG
//                    , 100, fileOutputStream);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            intent.setData(Uri.fromFile(outfile));
//            sendBroadcast(intent);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    private void saveImageToGallery(Bitmap saveBitmap) {
        OutputStream fos;
        Log.d("saved", "calll  ..");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContentResolver contentResolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + ".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "TestFolder");
                Uri imageuri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = contentResolver.openOutputStream(imageuri);
                saveBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Objects.requireNonNull(fos);
                Toast.makeText(this, "image saved", Toast.LENGTH_SHORT).show();
                Log.d("saved", "if   ..");

            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void setSharedValue(){
        SharedPreferences.Editor editor = getSharedPreferences("Mutual", MODE_PRIVATE).edit();
        editor.putInt("Mutual_no", 5);
        editor.apply();
    }

}