package org.nunocky.ocrtest01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Owner on 2015/12/02.
 */
public class ResearchHistory {
    static final String DB = "sqlite_history.db";
    static final int DB_VERSION = 12;
    static final String CREATE_TABLE = "create table mytable ( _id integer primary key autoincrement, kind integer not null,type integer not null, research string not null, result string not null, time integer not null  );";
    static final String DROP_TABLE = "drop table mytable;";
    static SQLiteDatabase mydb;
    private static String TAG = "AndroidOCR";

    public ResearchHistory(Context context) {
        MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(context);
        mydb = hlpr.getWritableDatabase();
        Delete();

    }

    public void Insert(int kind, int type, String research, String result) {
        Cursor cursor = Query("SELECT * FROM mytable WHERE research = ?", new String[]{research});

        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put("kind", kind);
            values.put("type", type);
            values.put("research", research);
            values.put("result", result);
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
            calendar.setTimeZone(tz);
            if (calendar.get(Calendar.AM_PM) == 0) {
                String time = Integer.toString(calendar.get(Calendar.YEAR)) + Integer.toString(calendar.get(Calendar.MONTH) + 1) + Integer.toString(calendar.get(Calendar.DATE))
                        + Integer.toString(calendar.get(Calendar.HOUR)) + Integer.toString(calendar.get(Calendar.MINUTE)) + Integer.toString(calendar.get(Calendar.SECOND));
                values.put("time", Long.parseLong(time));
            } else {
                String time = Integer.toString(calendar.get(Calendar.YEAR)) + Integer.toString(calendar.get(Calendar.MONTH) + 1) + Integer.toString(calendar.get(Calendar.DATE))
                        + Integer.toString(calendar.get(Calendar.HOUR) + 12) + Integer.toString(calendar.get(Calendar.MINUTE)) + Integer.toString(calendar.get(Calendar.SECOND));
                values.put("time", Long.parseLong(time));
            }

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
        } else {
            long ret;
            try {

                String _id = "";
                while (cursor.moveToNext()) {
                    _id = cursor.getString(cursor.getColumnIndex("_id"));
                }
                ContentValues values = new ContentValues();
                Calendar calendar = Calendar.getInstance();
                TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
                calendar.setTimeZone(tz);
                if (calendar.get(Calendar.AM_PM) == 0) {
                    String time = Integer.toString(calendar.get(Calendar.YEAR)) + Integer.toString(calendar.get(Calendar.MONTH) + 1) + Integer.toString(calendar.get(Calendar.DATE))
                            + Integer.toString(calendar.get(Calendar.HOUR)) + Integer.toString(calendar.get(Calendar.MINUTE)) + Integer.toString(calendar.get(Calendar.SECOND));
                    values.put("time", Long.parseLong(time));
                } else {
                    String time = Integer.toString(calendar.get(Calendar.YEAR)) + Integer.toString(calendar.get(Calendar.MONTH) + 1) + Integer.toString(calendar.get(Calendar.DATE))
                            + Integer.toString(calendar.get(Calendar.HOUR) + 12) + Integer.toString(calendar.get(Calendar.MINUTE)) + Integer.toString(calendar.get(Calendar.SECOND));

                    values.put("time", Long.parseLong(time));
                }
                ret = mydb.update("mytable", values, "_id = '" + _id + "' ", null);

            } finally {
                mydb.close();
            }
            if (ret == -1) {
                Log.v(TAG, "updatefailed");
            } else {
                Log.v(TAG, "updatesuccess");
            }

        }
    }

    public Cursor Query(String sqlstr) {
        Cursor c = mydb.rawQuery(sqlstr, null);

        return c;
    }

    public Cursor Query(String sqlstr, String[] conditon) {
        Cursor c = mydb.rawQuery(sqlstr, conditon);

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

    private void Delete() {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        calendar.setTimeZone(tz);
        String time;
        if (calendar.get(Calendar.AM_PM) == 0) {
            time = Integer.toString(calendar.get(Calendar.YEAR)) + Integer.toString(calendar.get(Calendar.MONTH)) + Integer.toString(calendar.get(Calendar.DATE))
                    + Integer.toString(calendar.get(Calendar.HOUR)) + Integer.toString(calendar.get(Calendar.MINUTE)) + Integer.toString(calendar.get(Calendar.SECOND));
        } else {
            time = Integer.toString(calendar.get(Calendar.YEAR)) + Integer.toString(calendar.get(Calendar.MONTH)) + Integer.toString(calendar.get(Calendar.DATE))
                    + Integer.toString(calendar.get(Calendar.HOUR) + 12) + Integer.toString(calendar.get(Calendar.MINUTE)) + Integer.toString(calendar.get(Calendar.SECOND));

        }
        Cursor cursor = Query("SELECT * FROM mytable WHERE time < '" + time + "'", null);
        if (cursor.getCount() != 0) {

                mydb.delete("mytable", "time < '" + time + "'", null);

        }

    }
}
