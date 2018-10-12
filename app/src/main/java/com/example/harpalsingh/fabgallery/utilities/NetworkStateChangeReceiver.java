package com.example.harpalsingh.fabgallery.utilities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.example.harpalsingh.fabgallery.interfaces.KeyConfig;

public class NetworkStateChangeReceiver extends BroadcastReceiver {

    private static Snackbar SNACKBAR = null;
    private View parentView;

    public NetworkStateChangeReceiver() {
    }

    public NetworkStateChangeReceiver(View parentView) {
        this.parentView = parentView;
        setupSnackBar();
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context ctx, final Intent intent) {
        Context context;
        context = ctx;

        String status = NetworkUtilityClass.getConnectivityStatusString(context);

        if (status.contentEquals("No connection")) {
            SNACKBAR.show();
        } else {
            if (SNACKBAR.isShown()) {
                SNACKBAR.dismiss();
            }
        }
    }

    private void setupSnackBar() {
        SNACKBAR = Snackbar.make(parentView, KeyConfig.NO_INTERNET_CONNECTION_PLEASE_TRY_AGAIN, Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SNACKBAR.dismiss();
                    }
                });
        SNACKBAR.setActionTextColor(Color.RED);
        View view = SNACKBAR.getView();
        TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
    }
}