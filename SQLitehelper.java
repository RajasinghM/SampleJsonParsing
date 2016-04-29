package com.sample.jsonparsing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajasingh on 4/28/2016.
 */
public class SQLitehelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "atlona";
    public static final int DATABASE_VERSION = 1;

    public SQLitehelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_SITES_TABLE = "CREATE TABLE sitestable ("
                +"id INTEGER PRIMARY KEY AUTOINCREMENT, "+"siteid TEXT UNIQUE, "+"userid TEXT, "+"sitename TEXT, "
                +"deviceid TEXT, "+"ip TEXT, "+"issubsites TEXT, "+"clientid TEXT, "+"customerid TEXT )";

        String CREATE_SUBSITES_TABLE = "CREATE TABLE subsites ("
                +"id INTEGER PRIMARY KEY AUTOINCREMENT, "+"subsiteid TEXT UNIQUE, "+"subsitename TEXT, "
                +"parentid TEXT, "+"deviceid TEXT, "+"ip TEXT, "+"userid TEXT )";


        db.execSQL(CREATE_SITES_TABLE);
        db.execSQL(CREATE_SUBSITES_TABLE);
    }

    public static final String TABLE_SITES = "sitestable";
    public static final String KEY_SITEID = "siteid";
    public static final String KEY_USERID = "userid";
    public static final String KEY_SITENAME = "sitename";
    public static final String KEY_DEVICEID = "deviceid";
    public static final String KEY_IP = "ip";
    public static final String KEY_ISSUBSITES = "issubsites";
    public static final String KEY_CLIENTID = "clientid";
    public static final String KEY_CUSTOMERID = "customerid";

    public static final String TABLE_SUBSITES = "subsites";
    public static final String KEY_SUBSITEID = "subsiteid";
    public static final String KEY_SUBSITENAME = "subsitename";
    public static final String KEY_SUBPARENTID = "parentid";
    public static final String KEY_SUBDEVICEID = "deviceid";
    public static final String KEY_SUBIP = "ip";
    public static final String KEY_SUBUSERID = "userid";


    public void addSites(String siteid, String userId, String siteName, String deviceId, String ip, String isSubsites, String clientId, String customerId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SITEID,siteid);
        values.put(KEY_USERID,userId);
        values.put(KEY_SITENAME,siteName);
        values.put(KEY_DEVICEID,deviceId);
        values.put(KEY_IP,ip);
        values.put(KEY_ISSUBSITES,isSubsites);
        values.put(KEY_CLIENTID,clientId);
        values.put(KEY_CUSTOMERID,customerId);
        db.insertWithOnConflict(TABLE_SITES,KEY_SITEID,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addsubSites(String ssiteid, String ssitename, String sparentid, String sdeviceid, String sip, String suserid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SUBSITEID,ssiteid);
        values.put(KEY_SUBSITENAME,ssitename);
        values.put(KEY_SUBPARENTID,sparentid);
        values.put(KEY_SUBDEVICEID,sdeviceid);
        values.put(KEY_SUBIP,sip);
        values.put(KEY_SUBUSERID,suserid);
        db.insertWithOnConflict(TABLE_SUBSITES, KEY_SUBSITEID, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<SiteGetSet> getSites(){
        ArrayList<SiteGetSet> gSites = new ArrayList<SiteGetSet>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlQuery = "SELECT "+KEY_SITEID+","+KEY_SITENAME+" FROM "+TABLE_SITES;
        Cursor cursor = db.rawQuery(sqlQuery,null);
        if(cursor != null){
            cursor.moveToFirst();
            do{
                String siteId = cursor.getString(cursor.getColumnIndex(KEY_SITEID));
                String siteName = cursor.getString(cursor.getColumnIndex(KEY_SITENAME));
                if(!gSites.contains(siteId)){
                    gSites.add(new SiteGetSet(siteName,siteId));
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return gSites;
    }

    public ArrayList<Subsites> getSubsites(){
        ArrayList<Subsites> gsSites = new ArrayList<Subsites>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlQuery = "SELECT "+KEY_SUBSITEID+","+KEY_SUBSITENAME+" FROM "+TABLE_SUBSITES;
        Cursor cursor = db.rawQuery(sqlQuery,null);
        if(cursor != null){
            cursor.moveToFirst();
            do{
                String siteId = cursor.getString(cursor.getColumnIndex(KEY_SUBSITEID));
                String siteName = cursor.getString(cursor.getColumnIndex(KEY_SUBSITENAME));
                if(!gsSites.contains(siteId)){
                    gsSites.add(new Subsites(siteId,siteName));
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return gsSites;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS sitestable");
        db.execSQL("DROP TABLE IF EXISTS subsites");
    }
}
