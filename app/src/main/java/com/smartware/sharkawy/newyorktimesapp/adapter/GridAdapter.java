package com.smartware.sharkawy.newyorktimesapp.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartware.sharkawy.newyorktimesapp.R;
import com.smartware.sharkawy.newyorktimesapp.model.item;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Mahmoud on 1/26/2016.
 */
public class GridAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final item iLock = new item();
    private List<item> iObjects;

    public GridAdapter(Context context, List<item> objects) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        iObjects = objects;
    }

    public Context getmContext() {
        return mContext;
    }
    public void add(item object) {

        synchronized (iLock) {
            iObjects.add(object);
        }
        notifyDataSetChanged();
    }
    public void clear() {
        synchronized (iLock) {
            iObjects.clear();
        }
        notifyDataSetChanged();
    }
    public void setData(List<item> data) {
        clear();
        for (item item : data) {
            add(item);
        }
    }

    @Override
    public int getCount() {
        if(iObjects!=null){

            return iObjects.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return iObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;


        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.grid_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }
        final item item = (item) getItem(position);

        viewHolder = (ViewHolder) convertView.getTag();

        Picasso.with(getmContext()).load(item.getImage_url()).error(R.mipmap.ic_launcher).into(viewHolder.imageView);
        viewHolder.titleView.setText(item.getTitle());
        try {
            viewHolder.publish_date.setText(FormatDate(item.getPublished_date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private String FormatDate(String dummyDate)throws ParseException {

        String date;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        date = DateUtils.formatDateTime(getmContext(),
                formatter.parse(dummyDate).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);

        return date ;

    }
    public static class ViewHolder {
        public final ImageView imageView;
        public final TextView titleView;
        public final TextView publish_date ;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id._item_image);
            titleView = (TextView) view.findViewById(R.id._item_title);
            publish_date = (TextView) view.findViewById(R.id._item_publish_date);
        }
    }
}
