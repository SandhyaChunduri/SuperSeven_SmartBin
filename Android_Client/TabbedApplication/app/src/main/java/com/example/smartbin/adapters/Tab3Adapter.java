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

public class Tab3Adapter extends ArrayAdapter<SmartBinView> {

    private Context context = null;
    private static List<SmartBinView> values;

    public Tab3Adapter(Context context, List<SmartBinView> values) {

        super(context, R.layout.tab_fragment_3_row_layout, values);

        Tab3Adapter.values = values;
        this.context = context;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(values == null)
            return null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.tab_fragment_3_row_layout, parent, false);


        SmartBinView currentBin = values.get(position);

        if(currentBin.getFillLevel().equals("Full"))
            rowView.setBackgroundColor(Color.rgb(246, 34, 23)); // ruby red

        if(currentBin.getFillLevel().equals("Half"))
            rowView.setBackgroundColor(Color.rgb(255,194,0)); // amber color

        if(currentBin.getFillLevel().equals("Empty"))
            rowView.setBackgroundColor(Color.rgb(65, 163, 23)); // Lime Green

        TextView textViewLabel1 = (TextView) rowView.findViewById(R.id.location);

        //textViewLabel1.setText(currentBin.getLocationName() + ", " );
        textViewLabel1.setText(currentBin.getLocationName() + ", Bin " + currentBin.getBinId() + ", ");
        TextView textViewLabel2 = (TextView) rowView.findViewById(R.id.BinId);

        textViewLabel2.setText(currentBin.getBinId() + "");

        TextView textViewBinId = (TextView) rowView.findViewById(R.id.FillLevel);

        textViewLabel2.setText(currentBin.getFillLevel() + "");

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

        return v;
    }

}
