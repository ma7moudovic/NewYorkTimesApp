package com.smartware.sharkawy.newyorktimesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartware.sharkawy.newyorktimesapp.R;
import com.smartware.sharkawy.newyorktimesapp.model.item;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by T on 2/17/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private final Context pContext;
//    private final LayoutInflater pInflater;

    private final item pLock = new item();
    private List<item> pObjects;

    public RecyclerAdapter(Context pContext, List<item> pObjects) {
        this.pContext = pContext;
        this.pObjects = pObjects;
    }

    public Context getpContext() {
        return pContext;
    }

    public void add(item object) {
        synchronized (pLock) {
            pObjects.add(object);
        }
        notifyDataSetChanged();
    }
    public void clear() {
        synchronized (pLock) {
            pObjects.clear();
        }
        notifyDataSetChanged();
    }
    public void setData(List<item> data) {
        clear();
        for (item product : data) {
            add(product);
        }
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, final int position) {

        String pic_url = pObjects.get(position).getImage_url();
//        holder.textView.setText(pObjects.get(position).getTitle());

        Picasso.with(getpContext()).load(pic_url).into(holder.imageView);
//        holder.imageView.setImageResource(pics[position]);
        holder.titleView.setText(pObjects.get(position).getTitle());
        try {
            holder.priceView.setText(FormatDate(pObjects.get(position).getPublished_date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(),pObjects.get(position).getPrice(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private String FormatDate(String dummyDate)throws ParseException{

        String date;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        date = DateUtils.formatDateTime(getpContext(),
                    formatter.parse(dummyDate).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);

        return date ;

    }

    @Override
    public int getItemCount() {
        if(pObjects!=null){

            return pObjects.size();
        }
        return 0;    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

//        protected TextView textView;
        protected ImageView imageView;
        protected TextView titleView;
        protected TextView priceView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id._imageView);
            titleView = (TextView) itemView.findViewById(R.id._item);
            priceView = (TextView) itemView.findViewById(R.id._date);
        }
    }
}

