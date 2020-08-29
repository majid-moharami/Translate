package com.example.translate.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.example.translate.database.TranslateDBSchema.WordTable.*;

import com.example.translate.database.TranslateBaseHelper;
import com.example.translate.database.TranslateDBSchema;
import com.example.translate.database.cursorwrapper.WordCursorWrapper;
import com.example.translate.model.WordTranslate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WordDBRepository implements IRepository<WordTranslate> {

    private static WordDBRepository sWordDBRepository;
    private SQLiteDatabase mSQLiteDatabase;
    private static Context mContext;

    public static WordDBRepository getInstance(Context context) {
        mContext = context.getApplicationContext();

        if (sWordDBRepository == null)
            sWordDBRepository = new WordDBRepository();
        return sWordDBRepository;
    }

    private WordDBRepository() {
        TranslateBaseHelper translateBaseHelper = new TranslateBaseHelper(mContext);
        mSQLiteDatabase = translateBaseHelper.getWritableDatabase();
    }

    @Override
    public void insert(WordTranslate wordTranslate) {
        ContentValues values = getWordContentValues(wordTranslate);
        mSQLiteDatabase.insert(NAME, null, values);
    }

    @Override
    public WordTranslate get(UUID uuid) {
        String where = COLS.UUID + "=?";
        String[] whereArgs = {uuid.toString()};
        WordCursorWrapper cursorWrapper = queryWord(where, whereArgs);

        try {
            cursorWrapper.moveToFirst();
            return cursorWrapper.getWord();
        } finally {
            cursorWrapper.close();
        }
    }

    @Override
    public List<WordTranslate> getList() {
        List<WordTranslate> wordTranslates = new ArrayList<>();
        WordCursorWrapper cursorWrapper = queryWord(null, null);

        try {
            cursorWrapper.moveToFirst();

            while (!cursorWrapper.isAfterLast()) {
                wordTranslates.add(cursorWrapper.getWord());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return wordTranslates;
    }

    @Override
    public void delete(WordTranslate wordTranslate) {
        String where = COLS.UUID + "=?";
        String[] whereArgs = {wordTranslate.getUUID().toString()};
        mSQLiteDatabase.delete(NAME, where, whereArgs);
    }

    @Override
    public void update(WordTranslate wordTranslate) {
        ContentValues values = getWordContentValues(wordTranslate);
        String where = COLS.UUID + "=?";
        String[] whereArgs = {wordTranslate.getUUID().toString()};
        mSQLiteDatabase.update(NAME, values, where, whereArgs);
    }

    public List<WordTranslate> findMeaning( String where) {
        List<WordTranslate> words = new ArrayList<>();

        WordCursorWrapper cursorWrapper = queryWord(where, null);

        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                words.add(cursorWrapper.getWord());
                cursorWrapper.moveToNext();
            }
            return words;
        }finally {
            cursorWrapper.close();
        }


    }

    private ContentValues getWordContentValues(WordTranslate wordTranslate) {
        ContentValues values = new ContentValues();
        values.put(COLS.PERSIAN, wordTranslate.getPersian());
        values.put(COLS.ENGLISH, wordTranslate.getEnglish());
        values.put(COLS.FRANCE, wordTranslate.getFrance());
        values.put(COLS.ARABIAN, wordTranslate.getArabian());
        values.put(COLS.UUID, wordTranslate.getUUID().toString());
        return values;
    }

    private WordCursorWrapper queryWord(String where, String[] whereArgs) {
        Cursor cursor = mSQLiteDatabase.query(NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);

        return new WordCursorWrapper(cursor);
    }

}
