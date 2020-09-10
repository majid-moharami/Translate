package com.example.translate.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.translate.database.TranslateDBSchema;
import com.example.translate.model.*;

import java.util.UUID;

public class WordCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public WordCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public WordTranslate getWord(){
        String persian = getString(getColumnIndex(TranslateDBSchema.WordTable.COLS.PERSIAN));
        String english = getString(getColumnIndex(TranslateDBSchema.WordTable.COLS.ENGLISH));
        String france = getString(getColumnIndex(TranslateDBSchema.WordTable.COLS.FRANCE));
        String arabian = getString(getColumnIndex(TranslateDBSchema.WordTable.COLS.ARABIAN));
        String uuid = getString(getColumnIndex(TranslateDBSchema.WordTable.COLS.UUID));
        WordTranslate wordTranslate = new WordTranslate(persian , english , france , arabian,UUID.fromString(uuid));
        //TODO make counstractor
        return wordTranslate;
    }
}
