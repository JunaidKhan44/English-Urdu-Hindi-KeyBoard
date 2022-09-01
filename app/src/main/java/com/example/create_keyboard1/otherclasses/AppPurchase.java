package com.example.create_keyboard1.otherclasses;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

public class AppPurchase {

    public static final String PRODUCT_IDW = "freeads";  //no capital word and number included
    public static final String PRODUCT_ID2M = "freeads";  //no capital word and number included
    public static final String PRODUCT_ID3M = "freeads";  //no capital word and number included
    public static final String PRODUCT_IDL = "freeads";  //no capital word and number included
    public static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjJx6GWVx4C9iDOR3Of5wmIK7G4NHsApsIQjJKSM2mqN54488du";
    public static final String MERCHANT_ID = "14275035111230084880";
    public static BillingProcessor bp;
    static boolean purchase = false;
    public static boolean readyToPurchase = false;
    private static final String LOG_TAG = "iabv3";

    public static boolean checkpurchases() {
        try {
            if (bp.isSubscribed(PRODUCT_IDW)||bp.isSubscribed(PRODUCT_ID2M)||bp.isSubscribed(PRODUCT_ID3M)||bp.isPurchased(PRODUCT_IDL)) {
                purchase = true;
            }
            else {
              // purchase = false;
                purchase = true;
            }
        } catch (Exception e) {
            Log.d("checkk", "checkpurchases: " + e.toString());
        }
        return purchase;
    }

    public static void runfirst(final Context mContext) {
        try {

            if (!BillingProcessor.isIabServiceAvailable(mContext)) {
                showToast("In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16", mContext);
            }

            bp = new BillingProcessor(mContext, LICENSE_KEY, MERCHANT_ID, new BillingProcessor.IBillingHandler() {
                @Override
                public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {

//                updateTextViews();
                }

                @Override
                public void onBillingError(int errorCode, @Nullable Throwable error) {
                    showToast("Something went wrong..", mContext);
                }

                @Override
                public void onBillingInitialized() {
//                showToast("onBillingInitialized");
                    readyToPurchase = true;
//                updateTextViews();
                }


                @Override
                public void onPurchaseHistoryRestored() {
//                showToast("onPurchaseHistoryRestored");
                    for (String sku : bp.listOwnedProducts())
                        Log.d(LOG_TAG, "Owned Managed Product: " + sku);
                    for (String sku : bp.listOwnedSubscriptions())
                        Log.d(LOG_TAG, "Owned Subscription: " + sku);
//                updateTextViews();
                }

            });
        } catch (Exception er) {
        }
    }

    public static void showToast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }



}









