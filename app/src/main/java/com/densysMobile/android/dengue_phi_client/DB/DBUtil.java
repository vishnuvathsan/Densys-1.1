package com.densysMobile.android.dengue_phi_client.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by android on 3/1/17.
 */

public class DBUtil {

    private static final String DISTRICT_TABLE_NAME = "District";
    private static final String DB_NAME = "densys.db";

    private DBHelper localDb;

    public DBUtil(Context context) {
        localDb = new DBHelper(context, DB_NAME, null, 2);

    }

    public boolean insertDistrict(int id, String name) {
        SQLiteDatabase db = localDb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("id", id);
        db.insert(DISTRICT_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = localDb.getReadableDatabase();
        Cursor res = db.rawQuery("select * from District where id=" + id + "", null);
        return res;
    }

    public ArrayList<String> getAllDistricts() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = localDb.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DISTRICT_TABLE_NAME, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("name")));
            res.moveToNext();
        }
        System.out.println(array_list.size());
        res.close();
        return array_list;
    }

    public boolean deleteAllDistricts() {
        SQLiteDatabase db = localDb.getWritableDatabase();
        try {
            db.rawQuery("delete from " + DISTRICT_TABLE_NAME, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public String getDistrictID(String dpdhsDesc) {
        SQLiteDatabase db = localDb.getReadableDatabase();
        try {
            Cursor res = db.rawQuery("Select id from " + DISTRICT_TABLE_NAME + " Where name=\'" + dpdhsDesc + "\'", null);
            res.moveToFirst();
            return res.getString(res.getColumnIndex("id"));

        } catch (Exception e) {
            return "Not Found";

        }

    }

}
