package com.weatherdemo.utils;

import android.app.Dialog;
import android.content.Context;
import android.location.LocationManager;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.weatherdemo.R;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Dilip on 27-06-2016.
 * <p>
 * Purpose :- This class used to keep comman utility method
 */
public class Helper {

    /**
     * function used to convert temperature from kelvin to celsius
     *
     * @param kelvin
     * @return
     */
    public static String kelvinToCelcius(double kelvin) {

        double celsius = kelvin - 273.0;
        NumberFormat nf = NumberFormat
                .getNumberInstance(Locale.US);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(1);
        return nf.format(celsius);
    }

    /**
     * function used to convert timestamps to date
     *
     * @param time
     * @return
     */
    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd MMM", cal).toString();
        return date;
    }

    public static void showOkDialog(Context context, String message) {
        TextView tvMsg, tvOk;
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_ok_dialog);
        tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        tvOk = (TextView) dialog.findViewById(R.id.tvOk);
        tvMsg.setText(message);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public static boolean isGPSON(Context context)
    {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return statusOfGPS;
    }
    public static void hidekeybord(EditText view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
