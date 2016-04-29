package com.sample.jsonparsing;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class GetSubsites extends AppCompatActivity {

    List<String> mData;
    List<String> mId;
    ListAdapter mAdapter;
    SQLitehelper dbase;
    Context mContext;
    ListView mList;
    String token;
    GetPrefsValue getPrefs;
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getsubsites);

        mContext = GetSubsites.this;
        dbase = new SQLitehelper(mContext);
        mList = (ListView) findViewById(R.id.subsiteList);
        mData = new ArrayList<String>();
        mId = new ArrayList<String>();
        mProgress = new ProgressDialog(mContext);
        mProgress.setCancelable(false);
        mProgress.setMessage("Loading. Please wait...");
        getSubsiteList();

        mAdapter = new ListAdapter(mContext,mData,mId);
        mList.setAdapter(mAdapter);

    }

    public ArrayList<Subsites> getSubsiteList(){
        ArrayList<Subsites> subsiteList = new ArrayList<Subsites>();
        subsiteList = dbase.getSubsites();
        if(!subsiteList.isEmpty()){
            for (int i=0; i<subsiteList.size(); i++){
                Subsites sitesGetset = subsiteList.get(i);
                mData.add(sitesGetset.getSubsiteName());
                mId.add(sitesGetset.getSubsiteId());
            }
        }
        return null;
    }

}
