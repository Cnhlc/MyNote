package com.wuwl.mynote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDB extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "notes";
    public static final String ID = "id";
    public static final String CONTENT = "content";
    public static final String TIME = "time";

    public static final String TABLE_NAME_PWD = "password";
    public static final String PWD = "password";

    public NoteDB(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_NAME + " ( " + ID + " integer primary key AUTOINCREMENT, "
                + CONTENT + " TEXT NOT NULL, "
                + TIME + " TEXT NOT NULL )";
        db.execSQL(sql);
        String sql2 = "create table "+TABLE_NAME_PWD+" ( "+PWD+" TEXT primary key);";
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
