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
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.NativeBannerAd;
import com.facebook.ads.NativeBannerAdView;

import org.w3c.dom.Text;

import static com.example.create_keyboard1.Otherclasses.ScreenUtil.MoreApp;
import static com.example.create_keyboard1.Otherclasses.ScreenUtil.Rateus;
import static com.example.create_keyboard1.Otherclasses.ScreenUtil.Share;
import static java.security.AccessController.getContext;

public class MainMenu extends AppCompatActivity {

    public ImageView  imgpop;
    private NativeBannerAd mNativeBannerAd;
    TextView  loadingtxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();



        loadingtxt=findViewById(R.id.loadtxt);
        imgpop=findViewById(R.id.popupmenu);
        bannerNative();
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
//                            case  R.id.exit_app:
//                                startActivity(new Intent(getApplicationContext(),ExitScreen.class));
//                                break;
                            default:

                                break;



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
        startActivity(new Intent(getApplicationContext(),Background.class));
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(),ExitScreen.class));
        finish();
    }

    public void openthemeactivity(View view) {

        startActivity(new Intent(getApplicationContext(),KeyboardThemeActivity.class));
    }

    public void instructionscreenfun(View view) {
        startActivity(new Intent(getApplicationContext(),InstructionScreenS.class));
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
                        .setTitleTextColor(Color.WHITE)
                        .setDescriptionTextColor(Color.LTGRAY)
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





}