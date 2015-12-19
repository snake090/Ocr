package org.nunocky.ocrtest01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Owner on 2015/12/02.
 */
public class ResearchHistory {
    static final String DB = "sqlite_history.db";
    static final int DB_VERSION = 2;
    static final String CREATE_TABLE = "create table mytable ( _id integer primary key autoincrement, kind integer not null,type integer not null, research string not null, result string not null  );";
    static final String DROP_TABLE = "drop table mytable;";
    static SQLiteDatabase mydb;
    private static String TAG = "AndroidOCR";

    public ResearchHistory(Context context) {
        MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(context);
        mydb = hlpr.getWritableDatabase();

    }

    public void Insert(int kind,int type ,String research, String result) {
        ContentValues values = new ContentValues();
        values.put("kind", kind);
        values.put("type",type);
        values.put("research", research);
        values.put("result", result);

        long ret;
        try {
            ret = mydb.insert("mytable", null, values);
        } finally {
            mydb.close();
        }
        if (ret == -1) {
            Log.v(TAG, "insertfailed");
        } else {
            Log.v(TAG, "insertsuccess");
        }
    }

    public Cursor Query(String sqlstr) {
        Cursor c = mydb.rawQuery(sqlstr,null);

        return c;


    }

    private static class MySQLiteOpenHelper extends SQLiteOpenHelper {
        public MySQLiteOpenHelper(Context c) {
            super(c, DB, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
    }
}
