package com.example.create_keyboard1.otherclasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.create_keyboard1.adapter.PageAdapter;
import com.example.create_keyboard1.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.NativeBannerAd;
import com.facebook.ads.NativeBannerAdView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;


public class Background extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem tabgradient;
    TabItem tabimage;
    TabItem tabsport;
    TabItem tabflag;
    TextView loadingtxt;

    private FirebaseAnalytics mFirebaseAnalytics;
    private NativeBannerAd mNativeBannerAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        getSupportActionBar().hide();

        loadingtxt=findViewById(R.id.loadtxt);


        if (!AppPurchase.checkpurchases()) {
            bannerNative();
        }


        //analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "2");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Background");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        tabLayout = findViewById(R.id.tablayout);
        tabgradient = findViewById(R.id.gradient);
        tabimage = findViewById(R.id.imagef);
        tabsport = findViewById(R.id.sport);
        tabflag = findViewById(R.id.flags);
        viewPager = findViewById(R.id.viewPager);


        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {


                } else if (tab.getPosition() == 2) {

                }
                else {


                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }
        });


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

                View adView = NativeBannerAdView.render(Background.this, mNativeBannerAd, NativeBannerAdView.Type.HEIGHT_50, viewAttributes);
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