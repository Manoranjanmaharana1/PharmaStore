package com.books.pharmacystore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PharmacyList extends AppCompatActivity implements LocationListener {
    private LocationManager locationManager;
    private final static String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private final static String KEY = "AIzaSyABrNG4CLIvRhDvzmZjHFmcUFVzQU81bkM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_list);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,

                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,

                            Manifest.permission.ACCESS_COARSE_LOCATION,
                    }, 123);
            return;

        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.e("Pharmacy",location.toString());
        getPharmacy(location);

    }

    private void getPharmacy(final Location location) {
        locationManager.removeUpdates(this);

        OkHttpClient okHttpClient = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();

        urlBuilder.addQueryParameter("location",Double.toString(location.getLatitude()) + ", " + Double.toString(location.getLongitude()));
        urlBuilder.addQueryParameter("radius", "5000" );
        urlBuilder.addQueryParameter("type","pharmacy");
        urlBuilder.addQueryParameter("key",KEY);

        final String Url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(Url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String s = response.body().string();
                PharmacyList.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            updateUI(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
//
            }
        });

    }

    private void updateUI(String response) throws JSONException{
        //System.out.print(response);
        ListView listView = findViewById(R.id.list);
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            String name = jsonObject1.getString("name");

            if(jsonObject1.has("opening_hours")) {
                JSONObject jsonObject2 = jsonObject1.getJSONObject("opening_hours");
//                Log.e("Pharma",jsonObject2.toString());
                String ss = jsonObject2.toString();
                int index = ss.indexOf(":");
                ss = ss.substring(index+1,ss.length()-1);
                Log.e("Pharma",ss);
                if (ss.compareTo("true") == 0) {
                    arrayList.add(name);
                    Log.e("Pharma",name);
                }
            }
        }


        ListAdapter listAdapter = new CustomAdapter(this, arrayList);
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
