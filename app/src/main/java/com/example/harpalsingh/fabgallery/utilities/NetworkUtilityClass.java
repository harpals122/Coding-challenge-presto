package com.example.harpalsingh.fabgallery.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.example.harpalsingh.fabgallery.interfaces.KeyConfig;
import java.util.Objects;

class NetworkUtilityClass {
    private static final int TYPE_WIFI = 1;
    private static final int TYPE_MOBILE = 2;
    private static final int TYPE_NOT_CONNECTED = 0;

    private static int getConnectivityStatus(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int connectivityStatus = NetworkUtilityClass.getConnectivityStatus(context);
        String status = null;
        switch (connectivityStatus) {
            case NetworkUtilityClass.TYPE_WIFI:
                status = KeyConfig.WIFI_ENABLED;
                break;
            case NetworkUtilityClass.TYPE_MOBILE:
                status = KeyConfig.MOBILE_DATA_ENABLED;
                break;
            case NetworkUtilityClass.TYPE_NOT_CONNECTED:
                status = KeyConfig.NO_CONNECTION;
                break;
        }
        return status;
    }
}