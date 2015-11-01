package com.example.smartbin.tabbedapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import java.util.List;
class PagerAdapter extends FragmentStatePagerAdapter {
    private final int mNumOfTabs;
    private TabFragment1 tab1 = null;
    private TabFragment2 tab2 = null;
    private TabFragment3 tab3 = null;
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                tab1 = new TabFragment1();
                return tab1;
            case 1:
                tab2 = new TabFragment2();
                return tab2;
            case 2:
                tab3 = new TabFragment3();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public void setTab1Data(List<SmartBinView> values)
    {
        if(tab1 != null)
        {
            tab1.setAdapterData(values);
        }
    }

    public void setTab2Data(List<SmartBinView> values, double latitude, double longitude)
    {
        if(tab2 != null)
        {
            tab2.setData(values, latitude, longitude);
        }
    }
    public View getTab1View()
    {
        if(tab1 != null)
        {
            return tab1.getView();
        }
        return null;
    }

    public void setTab3Data(List<SmartBinView> values, String baseURL)
    {
        if(tab3 != null)
        {
            tab3.setData(values);
            tab3.setServerDetails(baseURL);
        }
    }
}