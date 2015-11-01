package com.example.smartbin.tabbedapplication;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.smartbin.adapters.MobileArrayAdapter;

import java.util.ArrayList;
import java.util.List;



public class TabFragment1 extends ListFragment
{

    private static MobileArrayAdapter adapter;
    private View fragment_view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = savedInstanceState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<SmartBinView> mylist = new ArrayList<SmartBinView>();
        adapter = new MobileArrayAdapter(getContext(), mylist);
        setListAdapter(adapter);

        fragment_view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        return inflater.inflate(R.layout.tab_fragment_1, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        adapter.onClick(adapter.getItem(position), v);
    }


    public void setAdapterData(List<SmartBinView> values)
    {
        adapter.setItemList(values);
        adapter.notifyDataSetChanged();

    }


}