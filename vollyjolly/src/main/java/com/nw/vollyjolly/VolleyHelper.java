package com.nw.vollyjolly;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.android.volley.BuildConfig;

/**
 * Created by Yashwant on 03-12-2015..
 */
public class VolleyHelper {

    public static void Log(String tag, String msg) {
        if (msg == null)
            return;

        if (BuildConfig.DEBUG)
            android.util.Log.v(tag, msg);

    }

    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null)
        {
            return false;
        }
        else
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
