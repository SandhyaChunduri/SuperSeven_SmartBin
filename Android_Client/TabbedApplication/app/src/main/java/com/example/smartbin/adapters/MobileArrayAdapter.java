package com.example.smartbin.adapters;

/**
 * Created by Sandhya Chunduri on 9/22/2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.smartbin.tabbedapplication.R;
import com.example.smartbin.tabbedapplication.SmartBinView;

import java.util.List;

public class MobileArrayAdapter extends ArrayAdapter<SmartBinView> {

    private Context context = null;
    private static List<SmartBinView> values;

    public MobileArrayAdapter(Context context, List<SmartBinView> values) {

        super(context, R.layout.tab_fragment_1_row_layout, values);

        MobileArrayAdapter.values = values;
        this.context = context;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(values == null)
            return null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.tab_fragment_1_row_layout, parent, false);


        SmartBinView currentBin = values.get(position);
        if(currentBin.getFillLevel().equals("Full"))
            rowView.setBackgroundColor(Color.rgb(153, 0, 51)); // ruby red

        if(currentBin.getFillLevel().equals("Half"))
            rowView.setBackgroundColor(Color.rgb(255,140,0)); // amber color

        if(currentBin.getFillLevel().equals("Empty"))
            rowView.setBackgroundColor(Color.rgb(65, 163, 23)); // Lime Green


        TextView textViewLabel1 = (TextView) rowView.findViewById(R.id.label1);

        textViewLabel1.setText(currentBin.getLocationName() + ", Bin " + currentBin.getBinId());

        TextView textViewLabel2 = (TextView) rowView.findViewById(R.id.label2);

        textViewLabel2.setText(currentBin.getFillLevel() + "");

        TextView textViewBinId = (TextView) rowView.findViewById(R.id.BinId);

        textViewBinId.setVisibility(View.INVISIBLE);

        TextView textViewHumidity = (TextView) rowView.findViewById(R.id.Humidity);

        textViewHumidity.setVisibility(View.INVISIBLE);

        TextView textViewTemperature = (TextView) rowView.findViewById(R.id.Temperature);

        textViewTemperature.setVisibility(View.INVISIBLE);

        rowView.setMinimumHeight(rowView.getMinimumHeight());

        ViewGroup.LayoutParams params = rowView.getLayoutParams();
        params.height = rowView.getMinimumHeight();

        rowView.setLayoutParams(params);
        rowView.requestLayout();

        // change the icon for Windows and iPhone
        return rowView;
    }

    public String requestContent(String urlString) {
        return "result";
    }

    public List<SmartBinView> getItemList() { return values; }

    public void setItemList(List<SmartBinView> itemList) { values = itemList; }

    public int getCount() { if (values != null) return values.size(); return 0; }

    public SmartBinView getItem(int position) { if (values != null) return values.get(position); return null; }

    public long getItemId(int position) { if (values != null) return values.get(position).hashCode(); return 0; }

    public View onClick(SmartBinView currentBin, View v) {

        int height = v.getHeight();

        TextView textViewBinId = (TextView) v.findViewById(R.id.BinId);
        TextView textViewHumidity = (TextView) v.findViewById(R.id.Humidity);
        TextView textViewTemperature = (TextView) v.findViewById(R.id.Temperature);
        if(textViewBinId.isShown()) {
            textViewBinId.setVisibility(View.INVISIBLE);
            textViewHumidity.setVisibility(View.INVISIBLE);
            textViewTemperature.setVisibility(View.INVISIBLE);
            v.setMinimumHeight(v.getMinimumHeight());
            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.height = v.getMinimumHeight();
            v.setLayoutParams(params);
            v.requestLayout();
        }
        else {
            textViewBinId.setText("Bin ID :" + currentBin.getBinId());
            textViewHumidity.setText("Humidity :" + currentBin.getHumidity());
            textViewTemperature.setText("Temperature :" + currentBin.getTemperature());
            textViewBinId.setVisibility(View.VISIBLE);
            textViewHumidity.setVisibility(View.VISIBLE);
            textViewTemperature.setVisibility(View.VISIBLE);
            if(currentBin.getFillLevel().equals("Full"))
                v.setBackgroundColor(Color.rgb(153, 0, 51)); // red

            if(currentBin.getFillLevel().equals("Half"))
                v.setBackgroundColor(Color.rgb(255,140,0)); // Orange color

            if(currentBin.getFillLevel().equals("Empty"))
                v.setBackgroundColor(Color.GREEN);

            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.height = 200;
            v.setLayoutParams(params);
            v.requestLayout();
        }
        return v;
    }

}
