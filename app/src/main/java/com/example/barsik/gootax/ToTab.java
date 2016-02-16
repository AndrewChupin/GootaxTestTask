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
import com.example.barsik.gootax.adapter.ToTabAdapter;

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

public class ToTab extends SherlockFragment
{
    ArrayList<ListItem> toListItems = new ArrayList<>();
    SupportMapFragment toMapFragment;
    EditText toEditText;
    Button toGo;
    ListView buttonGoTo;
    GoogleMap toMap;
    String toSearch;
    getResult result;
    ToTabAdapter fromDestTabAdapter;

    /**Second tab Activity*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.to_layout, container, false);

        buttonGoTo = (ListView) v.findViewById(R.id.toLV);
        fromDestTabAdapter = new ToTabAdapter(getSherlockActivity(), R.layout.to_adapter_layout, toListItems);
        buttonGoTo.setAdapter(fromDestTabAdapter);
        buttonGoTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toMap.clear();
                double lat = toListItems.get(position).getLat();
                double lng = toListItems.get(position).getLng();
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lat, lng))
                        .zoom(5)
                        .tilt(20)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                toMap.animateCamera(cameraUpdate);
                MarkerOptions mMarker = new MarkerOptions()
                        .position(new LatLng(lat, lng));
                toMap.addMarker(mMarker);
                ((MainActivity) getActivity()).setToLat(lat);
                ((MainActivity) getActivity()).setToLng(lng);
            }
        });

        toMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.toMap);
        toMap = toMapFragment.getMap();
        toMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        toMap.setMyLocationEnabled(true);
        toMap.getUiSettings().setZoomControlsEnabled(true);
        toMap.getUiSettings().setCompassEnabled(true);

        toEditText = (EditText) v.findViewById(R.id.toET);

        toGo = (Button) v.findViewById(R.id.btnGoTo);
        toGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSearch = toEditText.getText().toString();
                toSearch = Uri.encode(toSearch);
                fromDestTabAdapter.clear();
                result = new getResult();
                result.execute();
                toMap.clear();
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
                url = new URL(google + URLEncoder.encode(toSearch, charset));
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
                    fromDestTabAdapter.add(item);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}