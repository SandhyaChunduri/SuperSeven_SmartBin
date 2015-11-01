package com.example.smartbin.dataproviders;

import android.os.AsyncTask;
import android.util.Log;

import com.example.smartbin.tabbedapplication.MainActivity;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Sandhya Chunduri on 10/16/2015.
 */

/**
 * Async task to load the data from server
 * **/
public class SearchResultsLoader extends AsyncTask<String, Void, List<String>> {

    private final MainActivity parentActivity;
    public SearchResultsLoader( MainActivity mainActivity) {
        parentActivity = mainActivity;

    }

    @Override
    protected List<String> doInBackground(String... params)
    {
        List<String> result = new ArrayList<String>();
        InputStream inputStream = null;
        String resultString = "";
        try {

            URL u = new URL(params[0]);
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
                //if(count > 20) count = 20;
                for (int i=0; i<count; i++) {
                    result.add(jsonArray.getString(i));
                }
            }
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        //if(result == null)

        return result;


    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    @Override
    protected void onPostExecute(List<String> values) {
        super.onPostExecute(values);
        parentActivity.setSearchValues(values);
    }
}