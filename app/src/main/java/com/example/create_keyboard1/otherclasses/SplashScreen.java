package com.example.create_keyboard1.otherclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.create_keyboard1.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdViewAttributes;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.example.create_keyboard1.otherclasses.PrivacyPolicy.PRIVACY_SHARED_PREF;
import static com.example.create_keyboard1.otherclasses.PrivacyPolicy.SHARED_PREF_PRIVACY;

public class SplashScreen extends AppCompatActivity {

    private NativeAd nativeAd;
    TextView  loading;
    ProgressBar progressBar;
    private InterstitialAd interstitialAd;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        loading=findViewById(R.id.loading);
        progressBar=findViewById(R.id.prograssbar);

        try {
            AppPurchase.runfirst(SplashScreen.this);
        } catch (Exception e) {
        }



        if (!AppPurchase.checkpurchases()) {
            NativeAds();
            callinterstitial();

        }



        Handler handler1=new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {

                findViewById(R.id.nextbtn).setVisibility(View.VISIBLE);

            }
        },6000);




    }

    public void nextactivity(View view) {
      //  startActivity(new Intent(getApplicationContext(),MainMenu.class));

        if (interstitialAd != null && interstitialAd.isAdLoaded()) {
            interstitialAd.show();
        } else {


            String val=yesno();
            if(TextUtils.equals(val,"yes")){
                Intent it = new Intent(SplashScreen.this, InAppPurchase.class);
                startActivity(it);
                finish();
            }
            else {
                Intent it = new Intent(SplashScreen.this, PrivacyPolicy.class);
                startActivity(it);
                finish();
            }

        }


    }


    public  String yesno(){
        sharedPreferences = getSharedPreferences(SHARED_PREF_PRIVACY, MODE_PRIVATE);
        String value = sharedPreferences.getString(PRIVACY_SHARED_PREF, "");
        return  value;
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




    public  void callinterstitial(){

        //ads
        AudienceNetworkAds.initialize(this);

        interstitialAd = new InterstitialAd(this, getResources().getString(R.string.FbInterstitialAd));
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

                // Interstitial ad displayed callback
                Log.e("mytag", "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

                // Interstitial dismissed callback
                Log.e("mytag", "Interstitial ad dismissed.");

                String val=yesno();
                if(TextUtils.equals(val,"yes")){
                    Intent it = new Intent(SplashScreen.this, InAppPurchase.class);
                    startActivity(it);
                    finish();
                }
                else {
                    Intent it = new Intent(SplashScreen.this, PrivacyPolicy.class);
                    startActivity(it);
                    finish();
                }
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e("mytag", "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("mytag", "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                //  interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d("mytag", "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d("mytag", "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());



    }
    @Override
    protected void onDestroy() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        super.onDestroy();
    }


}