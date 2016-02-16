package com.example.barsik.gootax;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.barsik.gootax.adapter.FromTabAdapter;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class FromTab extends SherlockFragment
{
    ArrayList<ListItem> formListItems = new ArrayList<>();
    SupportMapFragment formMapFragment;
    EditText fromEditText;
    Button buttonGoFrom;
    ListView fromListView;
    GoogleMap fromMap;
    String fromSearch;
    getResult result;
    FromTabAdapter fromTabAdapter;

    /**First tab Activity*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.from_layout, container, false);

        fromListView = (ListView) v.findViewById(R.id.fromLV);
        fromTabAdapter = new FromTabAdapter(getSherlockActivity(), R.layout.from_adapter_layout, formListItems);
        fromListView.setAdapter(fromTabAdapter);
        fromListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fromMap.clear();
                double lat = formListItems.get(position).getLat();
                double lng = formListItems.get(position).getLng();
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lat, lng))
                        .zoom(5)
                        .tilt(20)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                fromMap.animateCamera(cameraUpdate);
                MarkerOptions mMarker = new MarkerOptions()
                        .position(new LatLng(lat, lng));
                fromMap.addMarker(mMarker);
                ((MainActivity) getActivity()).setFromLat(lat);


                ((MainActivity) getActivity()).setFromLng(lng);
            }
        });

        formMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fromMap);
        fromMap = formMapFragment.getMap();
        fromMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        fromMap.setMyLocationEnabled(true);
        fromMap.getUiSettings().setZoomControlsEnabled(true);
        fromMap.getUiSettings().setCompassEnabled(true);

        fromEditText = (EditText) v.findViewById(R.id.fromET);

        buttonGoFrom = (Button) v.findViewById(R.id.btnGoFrom);
        buttonGoFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromSearch = fromEditText.getText().toString();
                fromSearch = Uri.encode(fromSearch);
                fromTabAdapter.clear();
                result = new getResult();
                result.execute();
                fromMap.clear();
            }
        });
        return v;
    }

    public class getResult extends AsyncTask<Void, Void, Void>
    {
        JSONObject json;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            String google = "http://maps.googleapis.com/maps/api/geocode/json?address=";
            String charset = "UTF-8";
            URL url;
            try
            {
                url = new URL(google + URLEncoder.encode(fromSearch, charset));
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

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            try
            {
                for(int i=0;i<7;i++)
                {
                    JSONObject location = json.getJSONArray("results").getJSONObject(i);
                    String address = location.getString("formatted_address");
                    location = location.getJSONObject("geometry").getJSONObject("location");
                    double lng = location.getDouble("lng");
                    double lat = location.getDouble("lat");
                    ListItem item = new ListItem();
                    item.setAddress(address);
                    item.setLat(lat);
                    item.setLng(lng);
                    fromTabAdapter.add(item);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}