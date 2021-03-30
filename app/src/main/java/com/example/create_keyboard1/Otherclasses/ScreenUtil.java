package com.example.create_keyboard1.Otherclasses;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ScreenUtil {

    public static void Rateus(Context context){
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ar.all.fast.dnloader.video")));
        } catch (Exception r) {
        }
    }

    public static void Share(Context context) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Facebook Video Downloader");
            String sAux = "Do you want to Download Facebook Videos ? .Install this App , Its Amazing :). \n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.ar.all.fast.doloader.video";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            context.startActivity(Intent.createChooser(i, "Share this App"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    public static void Policy(Context context){
        try {
            Uri uri = Uri.parse("https://privacypolicychristmas.blogspot.com/2020/11/this-pri-policy-applies-to-your-use.html"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (Exception z) {
        }
    }

    public static void MoreApp(Context context) {
        try {
            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:MFA Application Studios"));
            context.startActivity(rateIntent);
        } catch (Exception er) {

        }

    }
}