package com.example.barsik.gootax;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ResultActivity extends SherlockFragmentActivity
{
    SupportMapFragment supportMapFragment;
    GoogleMap resultMap;
    double fromLat;
    double fromLng;
    double toLat;
    double toLng;
    getResult result;

    /**Last Activity*/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);
        /**Get coordinates*/
        fromLat = getIntent().getExtras().getDouble("from_lat");
        fromLng = getIntent().getExtras().getDouble("from_lng");
        toLat = getIntent().getExtras().getDouble("to_lat");
        toLng = getIntent().getExtras().getDouble("to_lng");
        /**Settings map*/
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.resultMap);
        resultMap = supportMapFragment.getMap();
        resultMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        resultMap.getUiSettings().setCompassEnabled(true);
        resultMap.getUiSettings().setZoomControlsEnabled(true);
        resultMap.setMyLocationEnabled(true);

        /**From Marker*/
        MarkerOptions markerFrom = new MarkerOptions()
                .position(new LatLng(fromLat, fromLng));
        resultMap.addMarker(markerFrom);

        /**To Marker*/
        MarkerOptions markerTo = new MarkerOptions()
                .position(new LatLng(toLat, toLng));
        resultMap.addMarker(markerTo);
        result = new getResult();
        result.execute();
    }

    /**Second tab Activity*/
    public class getResult extends AsyncTask<Void, Void, Void>
    {
        JSONObject json;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        /**POST*/
        @Override
        protected Void doInBackground(Void... params)
        {
            String google = "http://maps.googleapis.com/maps/api/directions/json?sensor=true&";
            String from = "origin=";
            String fromC = fromLat+","+fromLng;
            String to = "&destination=";
            String toC = toLat+","+toLng;
            URL url;
            try
            {
                url = new URL(google+from+fromC+to+toC);
                System.out.println(url);
                URLConnection connection = url.openConnection();
                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null)
                {
                    builder.append(line);
                }
                json = new JSONObject(builder.toString());

            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        /**Paint*/
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            try
            {
                JSONArray routes = json.getJSONArray("routes");
                JSONObject route = routes.getJSONObject(0);
                JSONArray legs = route.getJSONArray("legs");
                JSONObject leg = legs.getJSONObject(0);
                JSONArray steps = leg.getJSONArray("steps");

                for(int i=0;i<steps.length();i++)
                {
                    JSONObject step = steps.getJSONObject(i);
                    JSONObject polyline = step.getJSONObject("polyline");
                    String points = polyline.getString("points");

                    List<LatLng> point = PolyUtil.decode(points);
                    PolylineOptions myPoly = new PolylineOptions()
                            .addAll(point)
                            .color(Color.BLACK)
                            .width(5);
                    resultMap.addPolyline(myPoly);

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(fromLat, fromLng))
                            .zoom(6)
                            .tilt(20)
                            .build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    resultMap.animateCamera(cameraUpdate);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
