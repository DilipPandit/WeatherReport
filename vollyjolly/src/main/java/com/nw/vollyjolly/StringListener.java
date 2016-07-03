package com.nw.vollyjolly;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Yashwant on 03-12-2015..
 */
public abstract class StringListener implements Response.Listener<String>,Response.ErrorListener {

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        VolleyHelper.Log("Error",volleyError.toString());
    }

    @Override
    public void onResponse(String s) {
        VolleyHelper.Log("Response",s.toString());
    }
}
