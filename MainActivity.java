package com.sample.jsonparsing;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUsename, etPassword;
    String uName, pWord;
    Button btLogin;
    String loginUrl = "http://128.199.65.229:1337/MobileSync/Login";
    String siteUrl = "http://128.199.65.229:1337/MobileSync/getSites";
    SQLitehelper dbase;
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsename = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(this);
        dbase = new SQLitehelper(MainActivity.this);
        mProgress = new ProgressDialog(MainActivity.this);
        mProgress.setCancelable(false);
        mProgress.setMessage("Loading. Please wait...");

    }

    public void getJsonResponse(){
        showProgressDialog();
        uName = etUsename.getText().toString().trim();
        pWord = etPassword.getText().toString().trim();
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", uName);
        params.put("password", pWord);

        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, loginUrl, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String cusId = jsonObject.getString("customerid");
                    String udid = jsonObject.getString("UDID");
                    String siteId = jsonObject.getString("siteID");
                    String userName = jsonObject.getString("username");
                    String passWord = jsonObject.getString("password");
                    String token = jsonObject.getString("token");
                    String status = jsonObject.getString("status");
                    if(status.equalsIgnoreCase("Active")){
                        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor sEdit = sPrefs.edit();
                        sEdit.putString("TOKEN",token);
                        sEdit.commit();
                        getStringResponse(token,cusId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("JsonResponse",""+jsonObject);
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
        };

        RequestQueue reqQueue = Volley.newRequestQueue(MainActivity.this);
        reqQueue.add(jsonObject);
    }

    @Override
    public void onClick(View v) {
        if(v == btLogin){
            getJsonResponse();
        }
    }

    private void getStringResponse(final String token, final String custId){
        StringRequest strRequest = new StringRequest(Request.Method.POST, siteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                Log.d("JsonArray", ""+str);
                try{
                    JSONArray jArray = new JSONArray(str);
                    for(int i=0; i<jArray.length(); i++){
                        JSONObject jObject = jArray.getJSONObject(i);
                        Gson gson = new Gson();
                        Sites sites = gson.fromJson(jObject.toString(),Sites.class);
                        dbase.addSites(sites.getSiteId(), sites.getUserId(), sites.getSiteName(), sites.getDeviceId(),
                                sites.getmIp(), sites.getIsSubsites(), sites.getClientId(), sites.getCustomerId());
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                hideProgressDialog();
                Intent intent = new Intent(MainActivity.this,GetSites.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                MainActivity.this.finish();

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
                params.put("customerid", custId);
                return params;
            }
        };

        RequestQueue requeue = Volley.newRequestQueue(MainActivity.this);
        requeue.add(strRequest);
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
