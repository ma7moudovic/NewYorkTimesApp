package com.smartware.sharkawy.newyorktimesapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.smartware.sharkawy.newyorktimesapp.app.AppController;
import com.smartware.sharkawy.newyorktimesapp.model.item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    GridFragment gridFragment ;
    ListFragment listFragment ;

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String OFF_ITEMS = "Offline_Items";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    ProgressDialog pDialog ;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Spinner sp ;
    String section_selected ;
    private ArrayList<item> Items = null;

    ViewPagerAdapter viewPagerAdapter ;

    CoordinatorLayout coordinatorLayout ;
    private static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading..");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        sp = (Spinner) findViewById(R.id.sections);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sections_array, android.R.layout.simple_list_item_1);
        sp.setAdapter(adapter);

        makeJsonObjectRequest(sp.getSelectedItem().toString());


        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                makeJsonObjectRequest(sp.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        listFragment = new ListFragment();
        gridFragment = new GridFragment();

        viewPagerAdapter.addFragment(listFragment, "List");
        viewPagerAdapter.addFragment(gridFragment, "Grid");
        viewPager.setAdapter(viewPagerAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void makeJsonObjectRequest(String setcion) {

        showpDialog();
        String URL_F= "http://api.nytimes.com/svc/topstories/v1/"+setcion+".json?api-key=799cdaa2fc9e374178d3c5d20dfac79d:2:74492644";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL_F, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    JSONArray feedsArray = response.getJSONArray("results");

                    ArrayList<item> results = new ArrayList<>();

                    for(int i = 0; i < feedsArray.length(); i++) {
                        JSONObject item = feedsArray.getJSONObject(i);
                        item itemModel = new item(item);
                        results.add(itemModel);
                    }

                    listFragment.setData(results);
                    gridFragment.setData(results);

                    saveToShared(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
                if(getAppData()!=null){
                    try {
                        // Parsing json object response
                        JSONArray feedsArray = getAppData().getJSONArray("results");

                        ArrayList<item> results = new ArrayList<>();

                        for(int i = 0; i < feedsArray.length(); i++) {
                            JSONObject item = feedsArray.getJSONObject(i);
                            item itemModel = new item(item);
                            results.add(itemModel);
                        }

                        listFragment.setData(results);
                        gridFragment.setData(results);
                        Snackbar.make(coordinatorLayout, "Can't Fetch News", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Snackbar.make(coordinatorLayout, "Can't Fetch News", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }else{
                    Snackbar.make(coordinatorLayout, "Can't Fetch News", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void saveToShared(JSONObject jsonObject) {

        String str = jsonObject.toString();
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor prefEditor = getSharedPreferences( "appData", Context.MODE_WORLD_WRITEABLE ).edit();
        prefEditor.clear();
        prefEditor.commit();
        prefEditor.putString("json", str);
        prefEditor.commit();

    }

    private JSONObject getAppData(){
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor prefEditor = getSharedPreferences( "appData", Context.MODE_WORLD_WRITEABLE ).edit();
        JSONObject obj =null;
        if(sharedPref.contains("json")){

            String JsonString = sharedPref.getString("json", null);
            try {
                obj = new JSONObject(JsonString) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return obj ;
    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
