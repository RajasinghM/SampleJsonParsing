package com.sample.jsonparsing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetSites extends AppCompatActivity {

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
        setContentView(R.layout.activity_getsites);

        mContext = GetSites.this;
        dbase = new SQLitehelper(mContext);
        mList = (ListView) findViewById(R.id.sitesList);
        mData = new ArrayList<String>();
        mId = new ArrayList<String>();
        mProgress = new ProgressDialog(mContext);
        mProgress.setCancelable(false);
        mProgress.setMessage("Loading. Please wait...");
        getPrefs = new GetPrefsValue(mContext);
        token = getPrefs.getPrefsValue("TOKEN");
        Log.d("TokenValue-->",token);
        getSitesList();

        mAdapter = new ListAdapter(mContext,mData,mId);
        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvId = (TextView) view.findViewById(R.id.tvId);
                String site_Id = tvId.getText().toString();
                showProgressDialog();
                getSubsitesJson(token, site_Id);
            }
        });
    }

    public ArrayList<SiteGetSet> getSitesList(){
        ArrayList<SiteGetSet> siteList = new ArrayList<SiteGetSet>();
        siteList = dbase.getSites();
        if(!siteList.isEmpty()){
            for (int i=0; i<siteList.size(); i++){
                SiteGetSet sitesGetset = siteList.get(i);
                mData.add(sitesGetset.getSiteName());
                mId.add(sitesGetset.getSiteId());
            }
        }
        return null;
    }


    private void getSubsitesJson(final String token, final String siteId){
        String urlSubsites = "http://128.199.65.229:1337/MobileSync/getSubSites";
        StringRequest strSubsites = new StringRequest(Request.Method.POST, urlSubsites, new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                Log.d("Subsite"+" Response-->",str);
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        Subsites subsite = gson.fromJson(jsonObject.toString(),Subsites.class);
                        dbase.addsubSites(subsite.getSubsiteId(),subsite.getSubsiteName(),subsite.getParentId(),
                                subsite.getDeviceId(),subsite.getInetProtocol(),subsite.getUserId());
                    }
                    hideProgressDialog();
                    Intent intent = new Intent(GetSites.this,GetSubsites.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    GetSites.this.finish();
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("APIKEY", "542f0190-ad6f-11e5-93d5-039280b0a9db");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", token);
                params.put("siteid", siteId);
                return params;
            }

        };

        RequestQueue strRequest = Volley.newRequestQueue(GetSites.this);
        strRequest.add(strSubsites);

    }

    private void showProgressDialog() {
        if (!mProgress.isShowing())
            mProgress.show();
    }

    private void hideProgressDialog() {
        if (mProgress.isShowing())
            mProgress.hide();
    }

}
