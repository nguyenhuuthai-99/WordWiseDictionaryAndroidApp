package com.namsan.learnjsoup1.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static  final String DATABASE_NAME = "SavedWordsDatabase";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sqlQuery ="create table savedwords (id integer primary key autoincrement, url text,title text, wordname text, wordform text, definition text, examples text)";

        sqLiteDatabase.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String sqlQuery = "Drop table if exists savedwords";

        sqLiteDatabase.execSQL(sqlQuery);

        onCreate(sqLiteDatabase);

    }
}
