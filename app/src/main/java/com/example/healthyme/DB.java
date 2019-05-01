package com.example.healthyme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    public static String MyDB = "fitness.db";
    public static String MyTable = "usertable";
    public static String COL1 = "currentdate",COL2 = "steps",COL3 = "target";

    public DB(Context context) {
        super(context, MyDB ,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ MyTable +"(currentdate TEXT PRIMARY KEY, steps INT,target INT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+MyTable);
        onCreate(db);
    }
    public boolean InsertData (String cdate, int steps, int target)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL1,cdate);
        cv.put(COL2,steps);
        cv.put(COL3,target);
        Long res = db.insert(MyTable,null,cv);

        if(res==-1)
        {
            return false;

        }
        else
        {
            return true;
        }

    }

    public Cursor DisplayData()
    {
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor rs=db.rawQuery("SELECT * FROM "+MyTable,null);
        return rs;
    }
}
