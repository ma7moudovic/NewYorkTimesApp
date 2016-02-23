package com.smartware.sharkawy.newyorktimesapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.gson.Gson;
import com.smartware.sharkawy.newyorktimesapp.adapter.GridAdapter;
import com.smartware.sharkawy.newyorktimesapp.model.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GridFragment extends Fragment {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String OFF_ITEMS = "Offline_Items";
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    GridView gridView ;
    GridAdapter gridAdapter ;
    public GridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_grid, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridview_);
        gridAdapter = new GridAdapter(getActivity(),getCart(getActivity()));
        gridView.setAdapter(gridAdapter);

        return rootView ;
    }
    public ArrayList<item> getCart(Context context) {
        SharedPreferences settings;
        List<item> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(OFF_ITEMS)) {
            String jsonFavorites = settings.getString(OFF_ITEMS, null);
            Gson gson = new Gson();
            item[] favoriteItems = gson.fromJson(jsonFavorites,
                    item[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<item>(favorites);
        } else
            return null;
        return (ArrayList<item>) favorites;
    }
}
