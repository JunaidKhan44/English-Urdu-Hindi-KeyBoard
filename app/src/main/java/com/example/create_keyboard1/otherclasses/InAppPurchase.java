package com.example.create_keyboard1.otherclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.example.create_keyboard1.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import static com.example.create_keyboard1.otherclasses.AppPurchase.LICENSE_KEY;
import static com.example.create_keyboard1.otherclasses.AppPurchase.MERCHANT_ID;
import static com.example.create_keyboard1.otherclasses.AppPurchase.PRODUCT_ID2M;
import static com.example.create_keyboard1.otherclasses.AppPurchase.PRODUCT_ID3M;
import static com.example.create_keyboard1.otherclasses.AppPurchase.PRODUCT_IDL;
import static com.example.create_keyboard1.otherclasses.AppPurchase.PRODUCT_IDW;

public class InAppPurchase extends AppCompatActivity implements  BillingProcessor.IBillingHandler {

    private InterstitialAd interstitialAd;
    BillingProcessor bp;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_purchase);
        getSupportActionBar().hide();

        bp=new BillingProcessor(this, LICENSE_KEY, MERCHANT_ID,this);

        if (!AppPurchase.checkpurchases()) {
            callinterstitial();

        }


    }

    public void movetomain(View view) {

        if (interstitialAd != null && interstitialAd.isAdLoaded()) {
            interstitialAd.show();
        } else {
            Log.e("mytag", "Interstitial ad dismissed.");
            startActivity(new Intent(getApplicationContext(), MainMenu.class));
            this.finish();
        }
    }


    void callinterstitial() {
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
                startActivity(new Intent(getApplicationContext(), MainMenu.class));
                finish();

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


        //

    }

    @Override
    protected void onDestroy() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        finish();
        super.onDestroy();
    }


    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Toast.makeText(this, "initialize ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    public void oneweeksub(View view) {
        value =PRODUCT_IDW;
        bp.subscribe(InAppPurchase.this, value);
    }

    public void onemonthsub(View view) {
        value =PRODUCT_ID2M;
        bp.subscribe(InAppPurchase.this, value);
    }

    public void threemonthsub(View view) {
        value =PRODUCT_ID3M;
        bp.subscribe(InAppPurchase.this, value);
    }

    public void lifetimesub(View view) {
        value =PRODUCT_IDL;
        bp.subscribe(InAppPurchase.this, value);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



}