package com.example.usb;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class Databasehandler extends SQLiteOpenHelper {
    public static final String dbname="SavedThings_db";
    public static final String tbn1="savedcons";
    public static final String srn="Srno";
    public static final String contact="cons";

    public Databasehandler(Context context) {
        super(context,dbname,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+tbn1+"( " +srn + " Integer Primary Key Autoincrement," +contact +" text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+tbn1);
        onCreate(db);
    }
    public void add(String con,SQLiteDatabase db){
        ContentValues cv=new ContentValues();
        cv.put(contact,con);
        db.insert(tbn1,null,cv);
        }
        public void upgrade(int id,String con,SQLiteDatabase db){
        ContentValues cv=new ContentValues();
        cv.put(contact,con);
        String selection = srn+""+ id;
        db.update(tbn1,cv,selection,new String[] { con });
        }
}
