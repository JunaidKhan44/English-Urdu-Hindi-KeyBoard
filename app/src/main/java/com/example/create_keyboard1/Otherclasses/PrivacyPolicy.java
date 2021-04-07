package com.example.create_keyboard1.Otherclasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.create_keyboard1.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdViewAttributes;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class PrivacyPolicy extends AppCompatActivity {


    TextView  loading;
    ProgressBar  progressBar;
    TextView  txt5;
    private NativeAd nativeAd;

    //Shared Preference
    public static final String SHARED_PREF_PRIVACY = "policy";
    public static final String PRIVACY_SHARED_PREF = "privacy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        getSupportActionBar().hide();


        loading=findViewById(R.id.loading);
        progressBar=findViewById(R.id.prograssbar);
        txt5 = findViewById(R.id.txt5);

        if (!AppPurchase.checkpurchases()) {
        NativeAds();
        }



        SpannableString ss = new SpannableString("terms and conditions");
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@androidx.annotation.NonNull View widget) {

                try {

                    try {
                        Uri uri
                                = Uri.parse("https://thinktechappstudios.wordpress.com/");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.buttoncolor));
            }
        };
        ss.setSpan(clickableSpan1, 0, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt5.setText(ss);
        txt5.setMovementMethod(LinkMovementMethod.getInstance());


    }

    public void movetomain(View view) {
//        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        SharedPreferences sharedPreferences1 = this.getSharedPreferences(SHARED_PREF_PRIVACY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putString(PRIVACY_SHARED_PREF, "yes");
        editor.commit();
        editor.apply();
        startActivity(new Intent(getApplicationContext(), InAppPurchase.class));
        finish();
    }



    public void NativeAds(){

        AudienceNetworkAds.initialize(this);
        nativeAd = new NativeAd(this, getResources().getString(R.string.FbNativeAd));
        //nativeAd = new NativeAd(this, "#YOUR_PLACEMENT_ID");
        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Set the Native Ad attributes
                NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes()
                        .setBackgroundColor(getResources().getColor(R.color.backgroundapp))
                        .setTitleTextColor(getResources().getColor(R.color.buttoncolor))
                        .setDescriptionTextColor(getResources().getColor(R.color.black))
                        .setButtonColor(getResources().getColor(R.color.buttoncolor))
                        .setButtonTextColor(Color.WHITE);

                View adView = NativeAdView.render(PrivacyPolicy.this, nativeAd,viewAttributes);
                LinearLayout nativeAdContainer = (LinearLayout) findViewById(R.id.native_ad_container);
                nativeAdContainer.addView(adView, new LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT));
                loading.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onMediaDownloaded(Ad ad) {

            }
        };
        nativeAd.loadAd(
                nativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .withMediaCacheFlag(NativeAdBase.MediaCacheFlag.ALL)
                        .build());

    }

}