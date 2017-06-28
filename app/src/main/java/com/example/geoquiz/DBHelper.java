package com.example.geoquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static  final int DATABASE_VERSION = 1;
    public static  final String DATABASE_NAME = "questionsDB";
    public static  final String TABLE_QUESTIONS = "questions";

    public static final String KEY_ID = "_id";
    public static final String KEY_TEXT = "text";
    public static final String KEY_ANSWER = "answer";
    public static final String KEY_IMAGE = "image";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // запрос на созадние таблицы
        db.execSQL("create table " + TABLE_QUESTIONS + "(" + KEY_ID + " integer primary key,"
                + KEY_TEXT + " text," + KEY_ANSWER + " numeric," + KEY_IMAGE + " text" + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // пересоздание таблицы, если меняется номер версии БД
        db.execSQL("drop table if exists " + TABLE_QUESTIONS);
        onCreate(db);
    }
}
