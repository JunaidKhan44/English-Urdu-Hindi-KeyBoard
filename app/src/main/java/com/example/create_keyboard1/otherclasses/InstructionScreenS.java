package com.example.create_keyboard1.otherclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.create_keyboard1.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.NativeBannerAd;
import com.facebook.ads.NativeBannerAdView;
import com.google.firebase.analytics.FirebaseAnalytics;

public class InstructionScreenS extends AppCompatActivity {


    private FirebaseAnalytics mFirebaseAnalytics;
    TextView  loadingtxt;
    private NativeBannerAd mNativeBannerAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_screen_s);
        getSupportActionBar().hide();


        loadingtxt=findViewById(R.id.loadtxt);
        if (!AppPurchase.checkpurchases()) {
            bannerNative();
        }

        //analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "4");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Instructions");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }



    public  void bannerNative(){
        mNativeBannerAd = new NativeBannerAd(this, getResources().getString(R.string.FbBannerAd));
        //mNativeBannerAd = new NativeBannerAd(this, "#YOUR_PLACEMENT_ID");
        NativeAdListener nativeAdListener = new NativeAdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

                NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes()
                        .setBackgroundColor(getResources().getColor(R.color.backgroundapp))
                        .setTitleTextColor(getResources().getColor(R.color.buttoncolor))
                        .setDescriptionTextColor(getResources().getColor(R.color.black))
                        .setButtonColor(getResources().getColor(R.color.buttoncolor))
                        .setButtonTextColor(Color.WHITE);

                View adView = NativeBannerAdView.render(InstructionScreenS.this, mNativeBannerAd, NativeBannerAdView.Type.HEIGHT_50, viewAttributes);
                LinearLayout nativeBannerAdContainer = (LinearLayout) findViewById(R.id.banner_container);
                nativeBannerAdContainer.addView(adView);
                loadingtxt.setVisibility(View.GONE);

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
        mNativeBannerAd.loadAd(
                mNativeBannerAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());



    }
}