package com.example.smartbin.dataproviders;

/**
 * Created by Sandhya Chunduri on 10/19/2015.
 */

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import com.example.smartbin.tabbedapplication.SmartBinView;

import java.util.ArrayList;
import java.util.List;

public class SearchSuggestionsProvider extends SearchRecentSuggestionsProvider {
    static final String TAG = SearchSuggestionsProvider.class.getSimpleName();
    private static final String AUTHORITY = SearchSuggestionsProvider.class
            .getName();
    private static final int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;
    private static final String[] COLUMNS = {
            "_id", // must include this column
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_TEXT_2,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA,
            SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
            SearchManager.SUGGEST_COLUMN_SHORTCUT_ID };

    public SearchSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        String query = selectionArgs[0];
        if (query == null || query.length() == 0) {
            return null;
        }

        MatrixCursor cursor = new MatrixCursor(COLUMNS);

        try {
            List<SmartBinView> list = createList(query);
            int n = 0;
            for (SmartBinView obj : list) {
                cursor.addRow(createRow(new Integer(n), query, obj.getLocationName(),
                        obj.getBinId()+""));
                n++;
            }
        } catch (Exception e) {
            //Log.e(TAG, "Failed to lookup " + query, e);
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    private Object[] createRow(Integer id, String text1, String text2,
                               String name) {
        return new Object[] { id, // _id
                text1, // text1
                text2, // text2
                text1, "android.intent.action.SEARCH", // action
                SearchManager.SUGGEST_NEVER_MAKE_SHORTCUT };
    }

    private List<SmartBinView> createList(String query)
    {
        List<SmartBinView> binViewList = new ArrayList<SmartBinView>();
        binViewList.add(new SmartBinView(1, 78.3444, 17.4372, 2, 31, 21, "Gachibowli"));
        binViewList.add(new SmartBinView(2, 78.3614, 17.4968,  1, 32, 22, "Miyapur"));
        binViewList.add(new SmartBinView(2, 78.3500, 17.4167,  0, 32, 22, "Nanakramguda"));
        return binViewList;
    }
}