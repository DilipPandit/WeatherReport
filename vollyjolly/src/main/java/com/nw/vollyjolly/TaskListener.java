package com.nw.vollyjolly;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Yashwant on 03-12-2015..
 */
public abstract class TaskListener implements Response.Listener<JSONObject>,Response.ErrorListener {

    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyHelper.Log("Error",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        VolleyHelper.Log("Response",response.toString());
    }


}
