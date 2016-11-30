package com.yermakov.xplatform.lesson5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.util.Pair;
import android.util.Log;

import java.util.ArrayList;

class ListSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TABLE_LISTS = "lists";
    private static final String COLUMN_ID = "_id";

    private static final String COLUMN_TEXT = "text";

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    private static ListSQLiteOpenHelper instance = null;

    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_LISTS + "( " + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TEXT + " TEXT NOT NULL);";

    private static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + TABLE_LISTS;

    private static final String QUERY_SELECT = "SELECT * FROM " + TABLE_LISTS +
            " WHERE " + COLUMN_TEXT + " LIKE ?";

    private static final String QUERY_SELECT_ALL = "SELECT * FROM " + TABLE_LISTS;

    private ListSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static void createInstance(Context context) throws Exception {
        if (instance != null) throw new Exception("instance created");
        instance = new ListSQLiteOpenHelper(context);
    }

    static ListSQLiteOpenHelper getInstance() {
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_DROP);
        onCreate(db);
    }

    long insert(SQLiteDatabase db, String text) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, text);
        return insert(db, values);
    }

    private long insert(SQLiteDatabase db, ContentValues values) {
        return db.insert(TABLE_LISTS, COLUMN_TEXT, values);
    }

    Pair<ArrayList<String>, ArrayList<Long>> select(SQLiteDatabase db, String str) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<Long> ids = new ArrayList<>();
        Cursor cursor = db.rawQuery(QUERY_SELECT, new String[] { '%' + str + '%' });
        if (cursor.moveToFirst()) {
            do {
                list.add(list.size(), cursor.getString(cursor.getColumnIndex(COLUMN_TEXT)));
                ids.add(ids.size(), cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                Log.d("list", list.get(list.size() - 1));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return Pair.create(list, ids);
    }

    Pair<ArrayList<String>, ArrayList<Long>> select_all(SQLiteDatabase db) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<Long> ids = new ArrayList<>();
        Cursor cursor = db.rawQuery(QUERY_SELECT_ALL, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(list.size(), cursor.getString(cursor.getColumnIndex(COLUMN_TEXT)));
                ids.add(ids.size(), cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                Log.d("list", list.get(list.size() - 1));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return Pair.create(list, ids);
    }

    void delete(SQLiteDatabase db, long id) {
        db.delete(TABLE_LISTS, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }


}
