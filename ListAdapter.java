package com.sample.jsonparsing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rajasingh on 4/28/2016.
 */
public class ListAdapter extends BaseAdapter {

    Context mContext;
    List<String> mData;
    LayoutInflater layoutInflater;

    public ListAdapter(Context mContext, List<String> mData){
        this.mContext = mContext;
        this.mData = mData;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = convertView;
        if(mView == null){
            mView = layoutInflater.inflate(R.layout.listitem,parent,false);
        }

        TextView tvList = (TextView) mView.findViewById(R.id.tvList);
        tvList.setText(mData.get(position));

        return mView;
    }
}
