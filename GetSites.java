package com.sample.jsonparsing;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class GetSites extends AppCompatActivity {

    List<String> mData;
    ListAdapter mAdapter;
    SQLitehelper dbase;
    Context mContext;
    ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getsites);

        mContext = GetSites.this;
        dbase = new SQLitehelper(mContext);
        mList = (ListView) findViewById(R.id.sitesList);
        mData = new ArrayList<String>();
        getSitesList();

        mAdapter = new ListAdapter(mContext,mData);
        mList.setAdapter(mAdapter);
    }

    public ArrayList<String> getSitesList(){
        ArrayList<String> siteList = new ArrayList<String>();
        siteList = dbase.getSites();
        if(!siteList.isEmpty()){
            for (int i=0; i<siteList.size(); i++){
                mData.add(siteList.get(i));
            }
        }
        return null;
    }

}
