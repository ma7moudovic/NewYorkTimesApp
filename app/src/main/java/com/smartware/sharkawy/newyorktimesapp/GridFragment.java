package com.smartware.sharkawy.newyorktimesapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.gson.Gson;
import com.smartware.sharkawy.newyorktimesapp.adapter.GridAdapter;
import com.smartware.sharkawy.newyorktimesapp.adapter.RecyclerAdapter;
import com.smartware.sharkawy.newyorktimesapp.model.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GridFragment extends Fragment {

    ArrayList<item> data_ret ;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public GridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_grid, container, false);
        data_ret = new ArrayList<>();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.main_recycler_view_g);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(getActivity(),data_ret,1);

        recyclerView.setAdapter(adapter);

        return rootView ;
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
    public void setData(ArrayList<item> data){
        data_ret.clear();
        data_ret.addAll(data);
        adapter.notifyDataSetChanged();
    }
}
