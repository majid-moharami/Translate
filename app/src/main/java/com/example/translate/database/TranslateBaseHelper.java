package com.example.translate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import com.example.translate.model.WordTranslate;

import static com.example.translate.database.TranslateDBSchema.WordTable.*;

public class TranslateBaseHelper extends SQLiteOpenHelper {

    public TranslateBaseHelper(@Nullable Context context) {
        super(context, NAME, null, TranslateDBSchema.VERSION);
    }

    private void addDefault(SQLiteDatabase database , WordTranslate wordTranslate){
        ContentValues values = new ContentValues();
        values.put(COLS.PERSIAN , wordTranslate.getPersian());
        values.put(COLS.ENGLISH , wordTranslate.getEnglish());
        values.put(COLS.FRANCE , wordTranslate.getFrance());
        values.put(COLS.ARABIAN , wordTranslate.getArabian());
        values.put(COLS.UUID , wordTranslate.getUUID().toString());
        database.insert(NAME , null , values);
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
        this.addDefault(db,new WordTranslate("آب", "water","l'eau","ماء"));
        this.addDefault(db,new WordTranslate("خانه", "Home","Accueil","الصفحة الرئيسية"));
        this.addDefault(db,new WordTranslate("زمین", "the earth","La terre","الأرض"));
        this.addDefault(db,new WordTranslate("غذا", "Food","Aliments","طعام"));
        this.addDefault(db,new WordTranslate("مغز ", "the brain","le cerveau","الدماغ"));
        this.addDefault(db,new WordTranslate("گوشت ", "Meat","Viande","لحم"));
        this.addDefault(db, new WordTranslate("خوشحال", "Happy","Heureux","سعيدة"));
        this.addDefault(db,new WordTranslate("قرمز", "Red","rouge","أحمر"));
        this.addDefault(db,new WordTranslate("ابی", "Blue","Bleu","أزرق"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
