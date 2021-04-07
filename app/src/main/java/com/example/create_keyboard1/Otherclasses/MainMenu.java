package com.example.create_keyboard1.Otherclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.create_keyboard1.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.NativeBannerAd;
import com.facebook.ads.NativeBannerAdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OneSignal;



public class MainMenu extends AppCompatActivity {

    public ImageView  imgpop;
    private NativeBannerAd mNativeBannerAd;
    TextView  loadingtxt;
    private InterstitialAd interstitialAd;
    private InterstitialAd interstitialAdtheme;
    private InterstitialAd interstitialAdinstruction;
    private InterstitialAd interstitialAdback;


    //one signal id
    private static final String ONESIGNAL_APP_ID = "2bf001bd-f827-4168-a82c-23db658f5415";


    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();


        //analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MainMenu");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);





        //one signal
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        //one signal


        loadingtxt=findViewById(R.id.loadtxt);
        imgpop=findViewById(R.id.popupmenu);


        if (!AppPurchase.checkpurchases()) {
            bannerNative();
            callinterstitial();
            callinterstitialtheme();
            callinterstitialinstruction();
            callinterstitialbackpressed();
        }


        imgpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), imgpop);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_1, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.menu_rate:
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ar.all.fast.dnloader.video")));
                                } catch (Exception r) {
                                }
                                break;
                            case R.id.menu_share:
                                try {
                                    Intent i = new Intent(Intent.ACTION_SEND);
                                    i.setType("text/plain");
                                    i.putExtra(Intent.EXTRA_SUBJECT, "Facebook Video Downloader");
                                    String sAux = "Do you want to Download Facebook Videos ? .Install this App , Its Amazing :). \n\n";
                                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.ar.all.fast.doloader.video";
                                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                                    startActivity(Intent.createChooser(i, "Share this App"));
                                } catch (Exception e) {
                                    //e.toString();
                                }
                                break;
                            case R.id.more_app:
                                try {
                                    Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:MFA Application Studios"));
                                    startActivity(rateIntent);
                                } catch (Exception er) {

                                }
                                break;
                            default:

                        }
                        return false;
                    }

                });
                popup.show();
            }
        });




    }

    public void enablingKeyboard(View view) {

        Intent enableIntent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
        enableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(enableIntent);
    }

    public void setkeyboarddialog(View view) {

        showMydialog();

    }

    private void showMydialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Alert.!")
                .setMessage("Make sure to enable first to see keyboard in the list.If you don't see the keyboard in the list enable it first.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        InputMethodManager imeManager = (InputMethodManager)
                                getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
                        imeManager.showInputMethodPicker();
                        finish();
                    }
                })
                .show();
    }

    public void disablekeyboard(View view) {
        Intent enableIntent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
        enableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(enableIntent);
    }

    public void showbackground(View view) {

        if (interstitialAd != null && interstitialAd.isAdLoaded()) {
            interstitialAd.show();
        } else {
            Log.e("mytag", "Interstitial ad dismissed.");
            startActivity(new Intent(getApplicationContext(),Background.class));
        }
    }

    @Override
    public void onBackPressed() {

        if (interstitialAdback != null && interstitialAdback.isAdLoaded()) {
            interstitialAdback.show();
        } else {
            Log.e("mytag", "Interstitial ad dismissed.");
            startActivity(new Intent(getApplicationContext(),ExitScreen.class));
            finish();
        }

    }

    public void openthemeactivity(View view) {


        if (interstitialAdtheme != null && interstitialAdtheme.isAdLoaded()) {
            interstitialAdtheme.show();
        } else {
            Log.e("mytag", "Interstitial ad dismissed.");
            startActivity(new Intent(getApplicationContext(),KeyboardThemeActivity.class));
        }

    }

    public void instructionscreenfun(View view) {

        if (interstitialAdinstruction != null && interstitialAdinstruction.isAdLoaded()) {
            interstitialAdinstruction.show();
        } else {
            Log.e("mytag", "Interstitial ad dismissed.");
            startActivity(new Intent(getApplicationContext(),InstructionScreenS.class));
        }

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

                View adView = NativeBannerAdView.render(MainMenu.this, mNativeBannerAd, NativeBannerAdView.Type.HEIGHT_50, viewAttributes);
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



    void callinterstitial(){

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
                Intent it = new Intent(MainMenu.this, Background.class);
                startActivity(it);


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
    void callinterstitialtheme() {
        //ads
        AudienceNetworkAds.initialize(this);
        //interstitialAd = new InterstitialAd(this, "#YOUR_PLACEMENT_ID");
        interstitialAdtheme = new InterstitialAd(this, getResources().getString(R.string.FbInterstitialAd));
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener2 = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

                // Interstitial ad displayed callback
                Log.e("mytag", "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

                // Interstitial dismissed callback
                Log.e("mytag", "Interstitial ad dismissed.");
                startActivity(new Intent(getApplicationContext(),KeyboardThemeActivity.class));


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
        interstitialAdtheme.loadAd(
                interstitialAdtheme.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener2)
                        .build());

    }
    void callinterstitialinstruction() {
        //ads
        AudienceNetworkAds.initialize(this);

        interstitialAdinstruction = new InterstitialAd(this, getResources().getString(R.string.FbInterstitialAd));
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener2 = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

                // Interstitial ad displayed callback
                Log.e("mytag", "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

                // Interstitial dismissed callback
                Log.e("mytag", "Interstitial ad dismissed.");
                startActivity(new Intent(getApplicationContext(),InstructionScreenS.class));


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
        interstitialAdinstruction.loadAd(
                interstitialAdinstruction.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener2)
                        .build());

    }
    void callinterstitialbackpressed() {
        //ads
        AudienceNetworkAds.initialize(this);

        interstitialAdback = new InterstitialAd(this, getResources().getString(R.string.FbInterstitialAd));
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener3 = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

                // Interstitial ad displayed callback
                Log.e("mytag", "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

                // Interstitial dismissed callback
                Log.e("mytag", "Interstitial ad dismissed.");
                startActivity(new Intent(getApplicationContext(),ExitScreen.class));


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
        interstitialAdback.loadAd(
                interstitialAdback.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener3)
                        .build());

    }



    @Override
    protected void onDestroy() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        if (interstitialAdtheme != null) {
            interstitialAdtheme.destroy();
        }
        if (interstitialAdinstruction != null) {
            interstitialAdinstruction.destroy();
        }
        if(interstitialAdback!=null){
            interstitialAdback.destroy();
        }
        super.onDestroy();
    }


}