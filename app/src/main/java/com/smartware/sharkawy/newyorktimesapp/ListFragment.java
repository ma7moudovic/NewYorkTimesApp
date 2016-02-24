package com.smartware.sharkawy.newyorktimesapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

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
public class ListFragment extends Fragment {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String OFF_ITEMS = "Offline_Items";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<item> data_ret ;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_list, container, false);
        data_ret= new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.main_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
//        adapter = new RecyclerAdapter(getActivity(),getCart(getActivity()));
        adapter = new RecyclerAdapter(getActivity(),data_ret);

        recyclerView.setAdapter(adapter);
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

    public void setData(ArrayList<item> data){
        data_ret.clear();
        data_ret.addAll(data) ;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
