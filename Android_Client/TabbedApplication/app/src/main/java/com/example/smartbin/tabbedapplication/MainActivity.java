package com.example.smartbin.tabbedapplication;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;

import com.example.smartbin.Helper.LocationServices;
import com.example.smartbin.dataproviders.RefreshLoader;
import com.example.smartbin.dataproviders.SearchResultsLoader;
import com.example.smartbin.dataproviders.AsyncListViewLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private double longitude, latitude;

    private final int RESULT_SETTINGS = 1;
    private int radius; // dropdown
    private String baseURL;

    private List<String> searchValues = null;
    private SimpleCursorAdapter mSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Fill Levels"));
        tabLayout.addTab(tabLayout.newTab().setText("Maps"));
        tabLayout.addTab(tabLayout.newTab().setText("Analytics"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                System.out.println("I am selected: " + tab.getPosition());
                refreshData(null);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                System.out.println("I am unselected: " + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                System.out.println("I am reselected: " + tab.getPosition());
            }
        });
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        loadUserSettings();
        getCurrentLocation();
        refreshData(null);

        viewPager.setCurrentItem(0);
        final String[] from = new String[] {"LocationName"};
        final int[] to = new int[] {android.R.id.text1};
        mSearchAdapter = new SimpleCursorAdapter(this,
                R.layout.li_query_suggestion,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        refreshSearchData();
        refreshTab3Data();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));
        searchView.setSuggestionsAdapter(mSearchAdapter);
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(false);
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        //searchAutoComplete.setHintTextColor(Color.DKGRAY);
        searchAutoComplete.setTextColor(Color.GREEN);
        searchAutoComplete.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.abc_popup_background_mtrl_mult));

        // Getting selected (clicked) item suggestion

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                // Your code here
                String selectedItem = getSuggestion(position);
                searchView.setQuery(selectedItem, false);
                EditText  searchText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
                searchText.setSelection(0);
                searchView.clearFocus();
                refreshDataForSearch(selectedItem);
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                String selectedItem = getSuggestion(position);
                return true;
            }

            private String getSuggestion(int position) {
                Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(
                        position);
                return cursor.getString(cursor
                        .getColumnIndex("LocationName"));
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                populateAdapter(s);
                return false;
            }

        });
        return true;
    }

    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "LocationName" });
        if(searchValues==null) return;
        if(query.contentEquals(""))
            for (int i=0; i<searchValues.size(); i++) {
                c.addRow(new Object[] {i, searchValues.get(i)});
            }
        else
            for (int i=0; i<searchValues.size(); i++) {
                if (searchValues.get(i).toLowerCase().contains(query.toLowerCase()))
                    c.addRow(new Object[] {i, searchValues.get(i)});
            }

        mSearchAdapter.changeCursor(c);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getApplicationContext().getSystemService(SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(getApplicationContext(), SampleSearchActivity.class)));
        //searchView.setSubmitButtonEnabled(true);

        return super.onPrepareOptionsMenu(menu) | true;

    }

    /**
     * On selecting action bar icons
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent i = new Intent(this, UserSettingsActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;
            case R.id.action_search:
                getCurrentLocation();
                MenuItem refreshMenuItem = item;
                refreshData(item);
                return true;
            case R.id.action_export:
                exportDocument();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void setValues(List<SmartBinView> results) {
        List<SmartBinView> values = results;
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter adapter = (PagerAdapter) viewPager.getAdapter();
        adapter.setTab1Data(results);

        adapter.setTab2Data(results, latitude, longitude);

    }
    public void setTab3Values(List<SmartBinView> results) {
        List<SmartBinView> values = results;
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter adapter = (PagerAdapter) viewPager.getAdapter();
        adapter.setTab3Data(results,baseURL);

    }

    public void setSearchValues(List<String> results) {

        searchValues = results;
        populateAdapter("");

    }

    private void getCurrentLocation() {
        /* get location - code start */
        LocationServices location = new LocationServices(this);
        if (location.canGetLocation()) {
            latitude = location.getLatitude(); // returns latitude
            longitude = location.getLongitude(); // returns longitude
        } else {
            location.showSettingsAlert();
            if (location.canGetLocation()) {
                latitude = location.getLatitude(); // returns latitude
                longitude = location.getLongitude(); // returns longitude
            } else {
                System.out.println("Hardcoding current location values");
                latitude = 17.3660;
                longitude = 78.4760;
            }
        }
/* get location - code end */

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                loadUserSettings();
                refreshData(null);
                break;

        }

    }

    private void loadUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        System.out.println("Radius: " + sharedPrefs.getString("prefRadius", "10"));
        System.out.println("Base URL: " + sharedPrefs.getString("prefBaseURL", "http://10.134.5.158:8080/SmartBin/"));
        System.out.println("Export Email Address: " + sharedPrefs.getString("prefExportEmail", "NULL"));
        System.out.println("Export Format: " + sharedPrefs.getString("prefExportFormat", "PDF"));

        radius = Integer.parseInt(sharedPrefs.getString("prefRadius", "10")); // dropdown
        baseURL = sharedPrefs.getString("prefBaseURL", "http://10.134.5.158:8080/SmartBin");

        String exportEmail = sharedPrefs.getString("prefExportEmail", "admin@smartbin.com");
        String exportFormat = sharedPrefs.getString("prefExportFormat", "PDF");

    }

    private void refreshData(MenuItem item) {
        String url = prepareURL();
        new RefreshLoader(item, this).execute(url);
    }

    private String prepareURL() {
        return baseURL + "/SmartBin/getLocalityBins/" + +latitude + "/" + longitude + "/" + radius;
    }

    private String prepareURLforSearch() {
        return baseURL + "/SmartBin/getAllLocations";
    }

    private void refreshSearchData() {
        String url = prepareURLforSearch();
        new SearchResultsLoader(this).execute(url);
    }

    private void refreshTab3Data() {
        String url = prepareURLforTab3();
        new AsyncListViewLoader(this).execute(url);
    }

    private String prepareURLforTab3() {
        return baseURL + "/SmartBin/getAllBins";
    }

    private void refreshDataForSearch(String searchLocation)
    {
        Pattern r = Pattern.compile(".*,(\\d*.\\d*),(\\d*.\\d*)$");
        String latitudeString=null, longitudeString=null;
        // Now create matcher object.
        Matcher m = r.matcher(searchLocation);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(2) );
            latitudeString = m.group(1);
            longitudeString = m.group(2);
        } else {
            System.out.println("NO MATCH");
        }

        String url = baseURL + "/SmartBin/getLocalityBins/" + latitudeString + "/" + longitudeString + "/" + radius;
        new RefreshLoader(null, this).execute(url);
    }

    private void shareDocument(Uri uri) {
        Intent mShareIntent = new Intent();
        mShareIntent.setAction(Intent.ACTION_SEND);
        mShareIntent.setType("application/pdf");
        // Assuming it may go via eMail:
        mShareIntent.putExtra(Intent.EXTRA_SUBJECT, "Here is a PDF from PdfSend");
        // Attach the PDf as a Uri, since Android can't take it as bytes yet.
        mShareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(mShareIntent);
    }
    private void exportDocument() {
        PdfDocument document = new PdfDocument();

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter adapter = (PagerAdapter) viewPager.getAdapter();
        View local_view = adapter.getTab1View();

        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(local_view.getWidth(), local_view.getHeight(), 1).create();

        PdfDocument.Page page = document.startPage(pageInfo);

        // start a page

        // draw something on the page

        local_view.draw(page.getCanvas());

        // finish the page
        document.finishPage(page);
        // add more pages
        // write the document content

        // close the document


        try {

            File pdfDirPath = new File(Environment.getExternalStorageDirectory(), "pdfs");
            if (pdfDirPath.mkdirs())
                System.out.println("Folder is created successfully");
            if (pdfDirPath.exists()) {
                System.out.println("Folder exists");
            }
            File file = new File(pdfDirPath, "pdfsend.pdf");
            Uri contentUri = Uri.fromFile(file);

            FileOutputStream os = new FileOutputStream(file);
            document.writeTo(os);
            document.close();
            os.close();

            shareDocument(contentUri);
        } catch (IOException e) {
            throw new RuntimeException("Error generating file", e);
        }
    }
}