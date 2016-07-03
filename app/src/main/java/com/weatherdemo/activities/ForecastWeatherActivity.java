package com.weatherdemo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nw.vollyjolly.DataInObject;
import com.nw.vollyjolly.NetworkCall;
import com.nw.vollyjolly.TaskListener;
import com.nw.vollyjolly.VolleyHelper;
import com.weatherdemo.utils.Constants;
import com.weatherdemo.models.NextDaysTemp;
import com.weatherdemo.R;
import com.weatherdemo.adapters.NextDaysWeatherAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ForecastWeatherActivity extends AppCompatActivity {

    private static final String TAG = "ForecastWeatherActivity";
    String cityName;
    RecyclerView rvNextDaysWeather;
    ProgressDialog progressDialog;
    ArrayList<NextDaysTemp> nextDaysTempArrayList;
    NextDaysWeatherAdapter nextDaysWeatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_weather);
        // initialise all views
        findview();

        // get city name entered by user
        Intent intent = getIntent();
        if (intent != null) {
            cityName = intent.getStringExtra(Constants.CITY_NAME);
            callNextDaysWeatherAPI();
        }
        // set up toolbar
        initToolBar();

    }

    private void callNextDaysWeatherAPI() {

        if (VolleyHelper.isNetworkAvailable(ForecastWeatherActivity.this)) {
            progressDialog = ProgressDialog.show(ForecastWeatherActivity.this, "", getString(R.string.please_wait));
            progressDialog.setCancelable(false);
            DataInObject inObj = new DataInObject(Constants.WEB_URL_NEXT_DAYS + cityName + "&mode=json&units=metric&cnt=14&appid=" + Constants.API_KEY);
            NetworkCall webserviceCall = new NetworkCall(ForecastWeatherActivity.this);
            webserviceCall.jsonObjectGetRequestWithHeaders(inObj, new TaskListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d(TAG, getString(R.string.onerror) + " [" + error + "]");
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }

                @Override
                public void onResponse(JSONObject response) {

                    Log.d(TAG, getString(R.string.onResponse) + " [" + response + "]");
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    try {
                        Gson gson = new GsonBuilder().create();
                        // Get 14 days weather in a json array
                        JSONArray weatherJsonArray = response.optJSONArray(Constants.LIST);
                        nextDaysTempArrayList = gson.fromJson(weatherJsonArray.toString(), new TypeToken<ArrayList<NextDaysTemp>>() {
                        }.getType());

                        // set data to recycler view
                        if (nextDaysTempArrayList != null) {
                            setRecyclerview();
                        }

                    } catch (Exception e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        e.printStackTrace();
                    }

                }
            });
        } else {
            Toast.makeText(ForecastWeatherActivity.this, getString(R.string.intenet_not_available), Toast.LENGTH_SHORT).show();

        }
    }

    private void findview() {
        rvNextDaysWeather = (RecyclerView) findViewById(R.id.rvNextDaysWeather);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titleView = (TextView) findViewById(R.id.toolbar_title);
        if (cityName != null)
            titleView.setText(getString(R.string.next_forecast) + " " + cityName + " " + getString(R.string.city));
        else
            titleView.setText(getString(R.string.next_forecast_new));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void setRecyclerview() {
        rvNextDaysWeather.setLayoutManager(new StaggeredGridLayoutManager(1, 1));
        rvNextDaysWeather.setNestedScrollingEnabled(true);
        rvNextDaysWeather.setHasFixedSize(false);
        nextDaysWeatherAdapter = new NextDaysWeatherAdapter(ForecastWeatherActivity.this, nextDaysTempArrayList);
        rvNextDaysWeather.setAdapter(nextDaysWeatherAdapter);

    }

}
