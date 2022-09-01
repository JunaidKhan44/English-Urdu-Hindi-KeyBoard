package com.example.create_keyboard1.otherclasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.create_keyboard1.adapter.KeyThemeAdapter;
import com.example.create_keyboard1.model.KeyThemeModel;
import com.example.create_keyboard1.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.NativeBannerAd;
import com.facebook.ads.NativeBannerAdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class KeyboardThemeActivity extends AppCompatActivity {


    public KeyThemeAdapter myAdapter;
    public RecyclerView recyclerView;
    public static ArrayList<KeyThemeModel> myflag=new ArrayList<KeyThemeModel>();
    private FirebaseAnalytics mFirebaseAnalytics;
    TextView loadingtxt;
    private NativeBannerAd mNativeBannerAd;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_theme);


        loadingtxt=findViewById(R.id.loadtxt);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(KeyboardThemeActivity.this,
                    R.color.buttondark));
        }
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.buttoncolor));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099CD")));


        if (!AppPurchase.checkpurchases()) {
            bannerNative();
        }

        //analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "3");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Themes");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        recyclerView=findViewById(R.id.keyboardthemekey);

        myflag.add(new KeyThemeModel(R.drawable.theme1,"Theme: 1"));
        myflag.add(new KeyThemeModel(R.drawable.theme2,"Theme: 2"));
        myflag.add(new KeyThemeModel(R.drawable.theme3,"Theme: 3"));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter=new KeyThemeAdapter(getApplicationContext(),myflag);
        recyclerView.setAdapter(myAdapter);
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

                View adView = NativeBannerAdView.render(KeyboardThemeActivity.this, mNativeBannerAd, NativeBannerAdView.Type.HEIGHT_50, viewAttributes);
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