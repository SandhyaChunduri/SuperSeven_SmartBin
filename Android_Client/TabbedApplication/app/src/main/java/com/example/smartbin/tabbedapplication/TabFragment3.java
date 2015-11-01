package com.example.smartbin.tabbedapplication;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.smartbin.adapters.Tab3Adapter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TabFragment3 extends ListFragment implements OnClickListener{

    private static Tab3Adapter adapter;
    private int day;
    private int month;
    private int year;
    private EditText et;
    private EditText et1;
    private String serverURL;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = savedInstanceState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<SmartBinView> mylist = new ArrayList<SmartBinView>();

        adapter = new Tab3Adapter(getContext(), mylist);
        setListAdapter(adapter);

        View fragment_view = inflater.inflate(R.layout.tab_fragment_3, container, false);

        ImageButton ib = (ImageButton) fragment_view.findViewById(R.id.imageButton);
        Calendar cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        et = (EditText) fragment_view.findViewById(R.id.editText);
        ib.setOnClickListener(this);

        et1 = (EditText) fragment_view.findViewById(R.id.editText1);
        ImageButton ib1 = (ImageButton) fragment_view.findViewById(R.id.imageButton1);
        ib1.setOnClickListener(this);

        Button myButton = (Button) fragment_view.findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AsyncReportViewLoader();
            }
        });

        return fragment_view;
    }

    public void setData(List<SmartBinView> values)
    {
        adapter.setItemList(values);
        adapter.notifyDataSetChanged();

    }

    public void setServerDetails(String serverDetails)
    {
        serverURL = serverDetails;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.imageButton) {
            DatePickerDialog d = new DatePickerDialog(getContext(), mDateSetListener, year, month, day);
            d.show();
        }
        else if(v.getId() == R.id.imageButton1) {
            DatePickerDialog d = new DatePickerDialog(getContext(), mDateSetListener1, year, month, day);
            d.show();
        }

    }
    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            et.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
        }

    };

    private final DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            et1.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
        }

    };

    private class AsyncReportViewLoader extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params)
        {
            try {
                URL u = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
                    return true;
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);

           /* Download complete. Lets update UI */
            if (result != null) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", serverURL + "/jasperserver/fillTrend.html");
                startActivity(intent);
            } else {
                System.out.println("data not received");
            }
        }

    }
}