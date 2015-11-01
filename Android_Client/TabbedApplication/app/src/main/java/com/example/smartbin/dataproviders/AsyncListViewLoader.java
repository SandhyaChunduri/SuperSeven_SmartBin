package com.example.smartbin.dataproviders;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.smartbin.adapters.Tab3Adapter;
import com.example.smartbin.tabbedapplication.MainActivity;
import com.example.smartbin.tabbedapplication.SmartBinView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandhya Chunduri on 9/23/2015.
 */
public class AsyncListViewLoader extends AsyncTask<String, Void, List<SmartBinView>> {

    //private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
    private MainActivity parentActivity;

    public AsyncListViewLoader (MainActivity mainActivity)
    {
        parentActivity = mainActivity;
    }


    @Override
    protected List<SmartBinView> doInBackground(String... params)
    {
        List<SmartBinView> result = new ArrayList<SmartBinView>();
        InputStream inputStream = null;
        String resultString = "";
        try {

            URL u = new URL(params[0]);
            if( u != null )
                throw new Exception("Dummy");

            HttpURLConnection conn = (HttpURLConnection) u.openConnection();


            conn.setRequestMethod("GET");
            conn.connect();
            inputStream = conn.getInputStream();

            // convert inputstream to string
            if(inputStream != null)
                resultString = convertInputStreamToString(inputStream);
            else
                resultString = null;

            if(resultString != null)
            {
                JSONArray jsonArray = new JSONArray(resultString);
                int count = jsonArray.length();
                if(count > 20) count = 20;
                for (int i=0; i<count; i++) {
                    result.add(convertSmartBin(jsonArray.getJSONObject(i)));
                }
            }
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;


    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private SmartBinView convertSmartBin(JSONObject obj)
    {
        try {
            int binId = obj.getInt("binId");
            Double longitude = obj.getDouble("longitude");
            Double latitude = obj.getDouble("latitude");
            int fillLevel = obj.getInt("fillLevel");
            int temperature = obj.getInt("temperature");
            int humidity = obj.getInt("humidity");
            String locationName = obj.getString("sublocality") + ", " +obj.getString("locality"); // obj.getString("name");
            return new SmartBinView( binId,  longitude,  latitude,  fillLevel,  temperature,  humidity, locationName);
        }
        catch (Exception e)
        {
            return null;
        }

    }


    @Override
    protected void onPostExecute(List<SmartBinView> result) {

        super.onPostExecute(result);

        /* Download complete. Lets update UI */
        if (result != null) {
            parentActivity.setTab3Values(result);
        } else {
            System.out.println("data not received");
        }
    }

}