package com.muaranauli.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.muaranauli.utils.Info;

import java.util.ArrayList;


/**
 * Created by JOHANESG1508 on 9/29/2015.
 */

public class DBImageAdapter {

    private static final String DATABASE_NAME = "image.db";
    private static final String TABLE_NAME = "imagetable";
    public static final String KEY_RID = "_id";
    public static final String KEY_NAME = "Name";
    public static final String KEY_IMAGE = "Image";
    public static final String[] ALL_KEYS = {KEY_RID,KEY_NAME,KEY_IMAGE};
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + " ("
            + KEY_RID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " VARCHAR(255), "
            + KEY_IMAGE + " BLOB NOT NULL" +
            ");";
    private static final String DROP_TABlE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private DBImageHelper helper;
    private SQLiteDatabase db;


    //Constructor
    public DBImageAdapter(Context context) {
        Info.notify(context,"DBImageAdapter.constructor() Called");
        helper=new DBImageHelper(context);
    }

    //open db method
    public DBImageAdapter open() {
        db=helper.getWritableDatabase();
        return this;
    }

    //close db method
    public void close() {
        db.close();
    }

    //insert method
    public long insertImage(ImageInstance image) {
        //SQLiteDatabase db=helper.getWritableDatabase();
        long id=-1;

        if(image!=null && image.imageBlob!=null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_NAME, image.getImageName());
            contentValues.put(KEY_IMAGE, image.getImageBlob());
            id= db.insert(TABLE_NAME, null, contentValues);
        }

        return id;
    }

    public int deleteTable() {
        SQLiteDatabase db=helper.getWritableDatabase();
        int deletedRows=db.delete(TABLE_NAME, "1", null);
        String table = "sqlite_sequence";
        String whereClause = KEY_NAME + "=?";
        String[] whereArgs = new String[] {TABLE_NAME};
        db.delete(table, whereClause, whereArgs);
        db.execSQL("VACUUM");
        return deletedRows;
    }

    // retrieve all rows from database and return the cursor
    public Cursor getAllRows() {
        String where=null;
        Cursor c=db.query(true, TABLE_NAME, ALL_KEYS,
                where, null, null, null, null, null);
        if(c!=null){
            c.moveToFirst();
        }
        return c;
    }

    // retrieve a rows by its id and return the cursor
    public Cursor getRow(long rowId) {
        String where=KEY_RID+"="+rowId;
        Cursor c=db.query(true, TABLE_NAME, ALL_KEYS,
                where, null, null, null, null, null);
        if(c!=null){
            c.moveToFirst();
        }
        return c;
    }




    public ImageInstance getImage(String imageName) {
        SQLiteDatabase db=helper.getWritableDatabase();
        int index1,index2;
        String name=null;
        byte[] blob=null;

        //SELECT _id,Name from usertable
        String[] columns={KEY_RID,KEY_NAME};
        Cursor cursor=db.query(TABLE_NAME, columns, KEY_NAME +" ='"+imageName+"'",
                null, null, null, null);

        while (cursor.moveToNext()) {
            index1=cursor.getColumnIndex(KEY_NAME);
            name=cursor.getString(index1);
            index2=cursor.getColumnIndex(KEY_RID);
            blob=cursor.getBlob(index2);
        }

        cursor.close();

        return new ImageInstance(name,blob);
    }

    static class DBImageHelper extends SQLiteOpenHelper {

        private Context context;

        //constructor
        DBImageHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context=context;
            Info.notify(context,"DBImageHelper.constructor() Called");
        }

        //create table and initial data
        @Override
        public void onCreate(SQLiteDatabase db) {
            Info.notify(context,"DBImageHelper.onCreate() Called");
            try {
                db.execSQL(CREATE_TABLE);
                Info.notify(context,"Table "+TABLE_NAME+" created successfully.");
            } catch (SQLiteException e) {
                //e.printStackTrace();
                Info.notify(context, "Exception: "+e.toString());
            }
        }

        //upgrade or perform update
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Info.notify(context,"DBImageHelper.onUpgrade() Called");
            try {
                Info.notify(context,"Table "+TABLE_NAME+" upgraded successfully.");
                db.execSQL(DROP_TABlE);
                onCreate(db);
            } catch (SQLiteException e) {
                //e.printStackTrace();
                Info.notify(context, "Exception: " + e.toString());
            }
        }

    }

}
