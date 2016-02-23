package com.smartware.sharkawy.newyorktimesapp.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by T on 2/22/2016.
 */
public class item  {

    private String title;
    private String published_date;
    private String image_url;

    public item(String title, String published_date, String image_url) {
        this.title = title;
        this.published_date = published_date;
        this.image_url = image_url;
    }

    public item() {
    }

    public item (JSONObject itemObject) throws JSONException {
        this.title = itemObject.getString("title");
        this.published_date =  itemObject.getString("published_date");
//        JSONArray multimedia = itemObject.getJSONArray("multimedia");
        String x = itemObject.getString("multimedia");


        try {
            JSONArray multi = new JSONArray(x);
            this.image_url =  multi.getJSONObject(1).getString("url");
        }catch (Exception e){
            Log.i("Multimedia",e.getMessage());
        }
//        this.image_url =  x;
//
    }
    public String getTitle() {
        return title;
    }

    public String getPublished_date() {
        return published_date;
    }

    public String getImage_url() {
        return image_url;
    }
}
