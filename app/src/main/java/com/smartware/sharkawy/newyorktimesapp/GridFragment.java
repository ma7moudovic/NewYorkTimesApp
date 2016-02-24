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

    ArrayList<item> data_ret ;

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
        data_ret = new ArrayList<>();

        gridView = (GridView) rootView.findViewById(R.id.gridview_);
        gridAdapter = new GridAdapter(getActivity(),data_ret);
        gridView.setAdapter(gridAdapter);
        return rootView ;
    }


    @Override
    public void onResume() {
        super.onResume();
        gridAdapter.notifyDataSetChanged();
    }
    public void setData(ArrayList<item> data){
        data_ret.clear();
        data_ret.addAll(data);
        gridAdapter.notifyDataSetChanged();
    }
}
