package com.example.mobile_programming_map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Bookmark.db";
    public static final String BOOKMARK_TABLE_NAME = "locations";
    public static final String BOOKMARK_COLUMN_ID = "id";
    public static final String BOOKMARK_COLUMN_LAT = "lat";
    public static final String BOOKMARK_COLUMN_LONG = "lang";
    public static final String BOOKMARK_COLUMN_NAME = "name";
    private HashMap hp;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table locations " +
                        "(id integer primary key, name text,lat text,lang text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS locations");
        onCreate(db);
    }

    public boolean insertLocation(String name, String lat, String longt) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("delete from locations");
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("lat", lat);
        contentValues.put("lang", longt);
        Log.i("inset", "insertLocation: " + name + lat + longt + "to " + BOOKMARK_TABLE_NAME);
        long res = db.insert("locations", null, contentValues);
        if (res == -1){
            Log.i("inset", "insertLocation: " + "did not");
            return false;
        }
        else {
            Log.i("inset", "insertLocation: " + "did not");
        }
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from locations where id="+id+"", null );
        return res;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from locations", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, BOOKMARK_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String lat, String longt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("lat", lat);
        contentValues.put("lang", longt);
        db.update("locations", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteLocation (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("locations",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }


    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from locations", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(BOOKMARK_COLUMN_NAME)));
            res.moveToNext();
        }
        Log.i("arraylist", "onClick: " + array_list);
        return array_list;
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from locations");
    }
}
