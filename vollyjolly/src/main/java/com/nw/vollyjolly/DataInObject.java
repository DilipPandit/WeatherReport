package com.nw.vollyjolly;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Yashwant on 03-12-2015..
 */
public class DataInObject {

    String url;
    JSONObject jsonObject;

    public DataInObject(JSONObject jsonObject, String url) {
        this.jsonObject = jsonObject;
        this.url = url;
    }

    public DataInObject(String url) {
        this.url = url;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
