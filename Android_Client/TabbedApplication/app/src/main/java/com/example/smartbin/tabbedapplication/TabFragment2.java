package com.example.smartbin.tabbedapplication;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smartbin.Connection.HttpConnection;
import com.example.smartbin.Connection.PathJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabFragment2 extends Fragment {


    private MapView mMapView = null;
    private GoogleMap googleMap;
    private ArrayList<SmartBinView> markersList = null;
    private HashMap<String, SmartBinView> markersMap = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = savedInstanceState;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        markersList = new ArrayList<SmartBinView>();
        markersMap = new HashMap<String, SmartBinView>();
        // Getting status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext());

        // Showing status
        if(status==ConnectionResult.SUCCESS)
        {

            //Google Play Services are available
            System.out.println("Google play services available");
            try {
                MapsInitializer.initialize(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Google play services not available");
            return null;
            //Google Play Services are not available
        }// Getting status

        View view = inflater.inflate(R.layout.tab_fragment_2, container,
                false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        googleMap.setMyLocationEnabled(true);

        // latitude and longitude
        double latitude =  17.4968;
        double longitude = 78.3614;

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


        // Setting a custom info window adapter for the google map
        googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {

                LatLng latLng = arg0.getPosition();

                String mapString = arg0.getTitle();
                SmartBinView markerDetails = markersMap.get(mapString);
                View v = inflater.inflate(R.layout.map_marker_info, null);
                if(markerDetails == null)
                    return v;

                TextView location = (TextView) v.findViewById(R.id.Location);

                location.setText(markerDetails.getLocationName());
                // Getting the position from the marker

                TextView binId = (TextView) v.findViewById(R.id.BinId);

                binId.setText("" + markerDetails.getBinId());

                // Getting reference to the TextView to set latitude
                TextView fillLevel = (TextView) v.findViewById(R.id.FillLevel);

                // Getting reference to the TextView to set longitude
                TextView humidity = (TextView) v.findViewById(R.id.Humidity);

                // Setting the latitude
                fillLevel.setText("" + markerDetails.getFillLevel());

                // Setting the longitude
                humidity.setText("" + markerDetails.getHumidity());

                // Returning the view containing InfoWindow contents
                return v;

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mMapView != null)
            mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mMapView != null)
            mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMapView != null)
            mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(mMapView != null)
            mMapView.onLowMemory();
    }


    private String getMapsApiDirectionsUrl(List<SmartBinView> result,double latitude, double longitude) {


        String waypoints = "origin="+latitude+"," + longitude + "&destination="+latitude+"," + longitude+"&waypoints=optimize:true|"+latitude+"," + longitude;

        for(int count = 0; count < result.size(); count ++) {
            SmartBinView currentBin = result.get(count);
            System.out.println("Creating marker for location: " + currentBin.getLocationName());
            if (currentBin.getLocationName() == null) continue;
            // Changing marker icon
            if (currentBin.getFillLevel().equals("Full"))
            {
                waypoints = waypoints + "|" + currentBin.getLatitude() + "," + currentBin.getLongitude();
            }
        }

        waypoints = waypoints + "|" + latitude+"," + longitude ;

        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
    }

    public void setData(List<SmartBinView> result, double latitude, double longitude)
    {
        System.out.println("Creating markers");
        if(result != null) {
            String url = getMapsApiDirectionsUrl(result, latitude, longitude);
            ReadTaskPoints downloadTask = new ReadTaskPoints();
            downloadTask.execute(url);
        }
        /* Download complete. Lets update UI */
        if (result != null && googleMap != null) {
            // create marker
            for(int count = 0; count < result.size(); count ++) {
                SmartBinView currentBin = result.get(count);
                System.out.println("Creating marker for location: " + currentBin.getLocationName());
                if(currentBin.getLocationName() ==null) continue;
                LatLng latlng = new LatLng(currentBin.getLatitude(), currentBin.getLongitude());
                MarkerOptions marker = new MarkerOptions().position(latlng).title("Hello Maps");

                // Changing marker icon
                if(currentBin.getFillLevel().equals("Full"))
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                if(currentBin.getFillLevel().equals("Half"))
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                if(currentBin.getFillLevel().equals("Empty"))
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                // adding marker
                Marker markerAdded = googleMap.addMarker(marker);
                markerAdded.setTitle(""+currentBin.getBinId());
                markersMap.put(""+currentBin.getBinId(),result.get(count));

            }
        } else {
            System.out.println("data not received");
        }

        float zoomLevel =(float) 12.5;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(zoomLevel).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

    }

    private class ReadTaskPoints extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;
            if(routes == null || routes.isEmpty())
                return;
            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(4);
                polyLineOptions.color(Color.DKGRAY);
            }

            googleMap.addPolyline(polyLineOptions);
        }
    }


}
