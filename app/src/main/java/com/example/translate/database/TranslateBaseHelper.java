package com.example.translate.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import static com.example.translate.database.TranslateDBSchema.WordTable.*;

public class TranslateBaseHelper extends SQLiteOpenHelper {

    public TranslateBaseHelper(@Nullable Context context) {
        super(context, NAME, null, TranslateDBSchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + NAME + "(" +
                COLS.PERSIAN + " text," +
                COLS.ENGLISH + " text," +
                COLS.FRANCE + " text," +
                COLS.ARABIAN + " text," +
                COLS.UUID + " text," +
                COLS.ID + " integer primary key autoincrement" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
