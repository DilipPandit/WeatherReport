package com.nw.vollyjolly;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yashwant on 03-12-2015..
 */
public class NetworkCall {

    Context mContext;
    public NetworkCall(Context context){
        mContext= context;
    }

    public void jsonObjectPostRequest(DataInObject inObject, TaskListener listener){
        HttpsTrustManager.allowAllSSL();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                inObject.getUrl(), inObject.getJsonObject(), listener, listener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("content-Type", "application/json; charset=utf-8");

                return headers;
            }
        };
        if(inObject.getUrl()!=null)
            VolleyHelper.Log("url", inObject.getUrl());
        if(inObject.getJsonObject()!=null)
            VolleyHelper.Log("request", inObject.getJsonObject().toString());
        ((VollyJollyController)mContext.getApplicationContext()).addToRequestQueue(jsonObjReq, "");
    }


    public void jsonObjectPostRequestWithHeaders(DataInObject inObject,TaskListener listener){
        HttpsTrustManager.allowAllSSL();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                inObject.getUrl(), inObject.getJsonObject(), listener, listener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        if(inObject.getUrl()!=null)
            VolleyHelper.Log("url", inObject.getUrl());
        if(inObject.getJsonObject()!=null)
            VolleyHelper.Log("request", inObject.getJsonObject().toString());

        ((VollyJollyController)mContext.getApplicationContext()).addToRequestQueue(jsonObjReq, "");
    }

   public void jsonObjectGetRequest(DataInObject inObject,TaskListener listener){
       HttpsTrustManager.allowAllSSL();
       JsonObjectRequest jsonObjReq = new JsonObjectRequest(
               Request.Method.GET,inObject.getUrl(), null,
               listener, listener) {
       };
       if(inObject.getUrl()!=null)
            VolleyHelper.Log("url", inObject.getUrl());

       ((VollyJollyController)mContext.getApplicationContext()).addToRequestQueue(jsonObjReq, "");
   }

    public void jsonObjectGetRequestWithHeaders(DataInObject inObject, TaskListener listener){
        HttpsTrustManager.allowAllSSL();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,inObject.getUrl(), null,  listener, listener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("content-Type", "application/json; charset=utf-8");
                return params;
            }
        };
        if(inObject.getUrl()!=null)
            VolleyHelper.Log("url", inObject.getUrl());
        // Adding request to request queue
        ((VollyJollyController)mContext.getApplicationContext()).addToRequestQueue(jsonObjReq, "");
    }
}
