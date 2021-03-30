package com.example.create_keyboard1.Otherclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class SplashScreen extends AppCompatActivity {

    private NativeAd nativeAd;
    TextView  loading;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        loading=findViewById(R.id.loading);
        progressBar=findViewById(R.id.prograssbar);
        NativeAds();
    }

    public void nextactivity(View view) {
      //  startActivity(new Intent(getApplicationContext(),MainMenu.class));
        startActivity(new Intent(getApplicationContext(),PrivacyPolicy.class));
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
                        .setTitleTextColor(Color.WHITE)
                        .setDescriptionTextColor(Color.LTGRAY)
                        .setButtonColor(getResources().getColor(R.color.buttoncolor))
                        .setButtonTextColor(Color.WHITE);

                View adView = NativeAdView.render(SplashScreen.this, nativeAd,viewAttributes);
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