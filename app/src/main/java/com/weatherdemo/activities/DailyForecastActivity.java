package com.weatherdemo.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nw.vollyjolly.DataInObject;
import com.nw.vollyjolly.NetworkCall;
import com.nw.vollyjolly.TaskListener;
import com.nw.vollyjolly.VolleyHelper;
import com.weatherdemo.utils.Constants;
import com.weatherdemo.models.Cordinate;
import com.weatherdemo.models.Main;
import com.weatherdemo.models.Weather;
import com.weatherdemo.models.Wind;
import com.weatherdemo.R;
import com.weatherdemo.utils.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DailyForecastActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //Declaration
    ProgressDialog progressDialog;
    ArrayList<Weather> weatherArrayList;
    CardView cvReport;
    TextView tvLocation, tvMoreDetails, tvWeatherDescription, tvTemperature, tvMinMaxTemprature, tvGeoCoordsLable, tvGeoCoordsValue;
    EditText edtSearch;
    ImageView ivSearch;
    Cordinate cordinate;
    Main main;
    Wind wind;
    private String TAG = "DailyForecastActivity";
    String cityName;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private double currentLatitude, currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        // initialise all views
        init();

        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }
        // set up toolbar
        initToolBar();

    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titleView = (TextView) findViewById(R.id.toolbar_title);
        titleView.setText(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    /**
     * all Weather report data will be set here
     */
    public void setWeatherInformation() {
        tvLocation.setText(edtSearch.getText().toString().trim().toUpperCase());
        edtSearch.setText("");
        tvWeatherDescription.setText(weatherArrayList.get(0).getDescription());
        tvTemperature.setText("" + Helper.kelvinToCelcius(main.getTemp()) + " \u2103");
        tvMinMaxTemprature.setText(Helper.kelvinToCelcius(main.getTemp_min()) + " \u2103" + " " + "-" + Helper.kelvinToCelcius(main.getTemp_max()) + " \u2103" + " wind "
                + wind.getSpeed() + "m/s");
        tvGeoCoordsValue.setText("[" + cordinate.getLat() + "," + cordinate.getLon() + "]");

    }

    /**
     * initialization of all views
     */
    private void init() {
        cvReport = (CardView) findViewById(R.id.cvReport);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvMoreDetails = (TextView) findViewById(R.id.tvMoreDetails);
        tvLocation.setPaintFlags(tvLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvWeatherDescription = (TextView) findViewById(R.id.tvWeatherDescription);
        tvTemperature = (TextView) findViewById(R.id.tvTemprature);
        tvMinMaxTemprature = (TextView) findViewById(R.id.tvMinMaxTemprature);
        tvGeoCoordsLable = (TextView) findViewById(R.id.tvGeoCoordsLable);
        tvGeoCoordsValue = (TextView) findViewById(R.id.tvGeoCoordsValue);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);

        ivSearch.setOnClickListener(this);
        cvReport.setOnClickListener(this);
        tvMoreDetails.setOnClickListener(this);

        if (!Helper.isGPSON(this)) {
            Helper.showOkDialog(this, getString(R.string.gps_off));
        }
    }

    private void callWeatherApi(String cityName) {

        if (VolleyHelper.isNetworkAvailable(DailyForecastActivity.this)) {
            progressDialog = ProgressDialog.show(DailyForecastActivity.this, "", getString(R.string.please_wait));
            progressDialog.setCancelable(false);

            DataInObject inObj = new DataInObject(Constants.WEB_URL + cityName + "&appid=" + Constants.API_KEY);
            NetworkCall webserviceCall = new NetworkCall(DailyForecastActivity.this);
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
                        if (!response.getString("cod").equalsIgnoreCase(getString(R.string.success_code))) {
                            edtSearch.setText("");
                            Helper.showOkDialog(DailyForecastActivity.this, getString(R.string.no_records));

                        }
                        try {

                            Gson gson = new GsonBuilder().create();
                            JSONObject cordinatejObject = response.optJSONObject(Constants.CORDINATE);
                            JSONObject mainjObject = response.optJSONObject(Constants.MAIN);
                            JSONObject windjObject = response.optJSONObject(Constants.WIND);
                            JSONArray weatherJsonArray = response.optJSONArray(Constants.WEATHER);
                            cordinate = gson.fromJson(cordinatejObject.toString(), Cordinate.class);
                            main = gson.fromJson(mainjObject.toString(), Main.class);
                            wind = gson.fromJson(windjObject.toString(), Wind.class);
                            weatherArrayList = gson.fromJson(weatherJsonArray.toString(), new TypeToken<ArrayList<Weather>>() {
                            }.getType());
                            if (weatherArrayList.size() == 0) {
                                Helper.showOkDialog(DailyForecastActivity.this, getString(R.string.no_records));
                                return;
                            }
                            if (cordinate != null && main != null && wind != null) {
                                cvReport.setVisibility(View.VISIBLE);
                                setWeatherInformation();
                            } else {
                                cvReport.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } else {
            Helper.showOkDialog(DailyForecastActivity.this, getString(R.string.intenet_not_available));

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSearch:
                Helper.hidekeybord(edtSearch);
                if (isValid()) {
                    callWeatherApi(cityName);
                }
                break;
            case R.id.tvMoreDetails:

                Intent intent = new Intent(DailyForecastActivity.this, ForecastWeatherActivity.class);
                intent.putExtra(Constants.CITY_NAME, cityName);
                startActivity(intent);

                break;
            case R.id.cvReport:
                tvMoreDetails.performClick();
                break;
            default:
                break;
        }

    }

    /**
     * validate the search view
     *
     * @return
     */
    private boolean isValid() {
        if (!edtSearch.getText().toString().isEmpty()) {
            cityName = edtSearch.getText().toString().trim();
            return true;
        } else {
            Helper.showOkDialog(DailyForecastActivity.this, getString(R.string.enter_city_name));
            return false;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
        //set local broadcast for GPS
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.location.PROVIDERS_CHANGED");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // find city using current lat long
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            currentLatitude = mLastLocation.getLatitude();
            currentLongitude = mLastLocation.getLongitude();

            Geocoder gcd = new Geocoder(DailyForecastActivity.this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(currentLatitude, currentLongitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null) {
                if (addresses.size() > 0) {
                    if (addresses.get(0).getLocality() != null)
                        callWeatherApi(addresses.get(0).getLocality());
                    else {
                        Helper.showOkDialog(this, getString(R.string.unable_to_find_city));
                    }
                } else {
                    Helper.showOkDialog(this, getString(R.string.unable_to_find_city));
                }

            } else {
                Helper.showOkDialog(this, getString(R.string.unable_to_find_city));
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, getString(R.string.onFailled) + " [" + connectionResult + "]");
    }

    /**
     * Creating location request object, call it if you needed locations as per interval
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Helper.showOkDialog(getApplicationContext(), getString(R.string.device_not_supported));
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    /**
     * BroadcastReceiver to receive GPS status change
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /**
             *  check is GPS ON or OFF
             */
            if (!Helper.isGPSON(context)&&cvReport.getVisibility()==View.GONE) {
                Helper.showOkDialog(DailyForecastActivity.this, getString(R.string.gps_off));
            }
        }
    };
}
