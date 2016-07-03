package com.nw.vollyjolly;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;

/**
 * Created by Yashwant on 03-12-2015.
 */
public abstract class ArrayListener implements Response.Listener<JSONArray>, Response.ErrorListener{

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        VolleyHelper.Log("Error", volleyError.toString());
    }

    @Override
    public void onResponse(JSONArray jsonArray) {
        VolleyHelper.Log("Response", jsonArray.toString());
    }
}
