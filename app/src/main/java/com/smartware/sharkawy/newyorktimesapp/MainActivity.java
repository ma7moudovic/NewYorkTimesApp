package com.smartware.sharkawy.newyorktimesapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.google.gson.Gson;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        sp = (Spinner) findViewById(R.id.sections);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sections_array, android.R.layout.simple_list_item_1);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        section_selected = sp.getSelectedItem().toString() ;
        FetchFeeds req = new FetchFeeds(this);
        req.execute();

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                section_selected = sp.getSelectedItem().toString() ;
                new FetchFeeds(MainActivity.this).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ListFragment(), "List");
        adapter.addFragment(new GridFragment(), "Grid");
//        adapter.addFragment(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
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

    public class FetchFeeds extends AsyncTask<String, Void, List<item>> {
        private Activity context;

        public FetchFeeds(Activity context) {
            this.context = context;
        }
        private final String LOG_TAG = FetchFeeds.class.getSimpleName();

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            try {

                pDialog = new ProgressDialog(context);

            }catch (Exception e){
                Log.e(LOG_TAG, e.toString());
                e.printStackTrace();
            }
            pDialog.setMessage("Loading...");

            pDialog.show();
        }

        private List<item> getMoviesDataFromJson(String jsonStr) throws JSONException {

            JSONObject feedsJson = new JSONObject(jsonStr);
            JSONArray feedsArray = feedsJson.getJSONArray("results");

            List<item> results = new ArrayList<>();

            for(int i = 0; i < feedsArray.length(); i++) {
                JSONObject item = feedsArray.getJSONObject(i);
                item itemModel = new item(item);
                results.add(itemModel);
            }

            return results;
        }

        @Override
        protected List<item> doInBackground(String... params) {

//            if (params.length == 0) {
//                return null;
//            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {

                URL url = new URL("http://api.nytimes.com/svc/topstories/v1/"+section_selected+".json?api-key=799cdaa2fc9e374178d3c5d20dfac79d:2:74492644");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(List<item> newsFeed) {
            pDialog.dismiss();
            if (newsFeed != null) {

                Items = new ArrayList<>();
                Items.addAll(newsFeed);

                settings = context.getSharedPreferences(PREFS_NAME,
                        Context.MODE_PRIVATE);
                editor = settings.edit();

                Gson gson = new Gson();
                String jsonFeedsOffline = gson.toJson(Items);

                editor.putString(OFF_ITEMS, jsonFeedsOffline);
                editor.commit();
                setupViewPager(viewPager);
                tabLayout.setupWithViewPager(viewPager);

            }else{
                Toast.makeText(context, "can't fetch news Feed ", Toast.LENGTH_SHORT).show();

            }
            pDialog.hide();
        }
    }

}
