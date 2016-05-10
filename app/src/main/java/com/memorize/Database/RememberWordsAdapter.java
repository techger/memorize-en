package com.memorize.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.memorize.model.RememberWord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tortuvshin on 5/10/2016.
 */
public class RememberWordsAdapter extends DatabaseHelper{

    private static final String TAG = "RememberWordsAdapter : ";

    public static final String TABLE_REMEMBER_WORDS = "remember_words";
    public static final String REMEMBER_WORD_ENG    = "remember_word_english";
    public static final String REMEMBER_WORD_TYPE   = "remember_word_type";
    public static final String REMEMBER_WORD_MON    = "remember_word_mongolia";

    private static final String[] PROJECTIONS_REMEMBER_WORDS = { REMEMBER_WORD_ENG, REMEMBER_WORD_TYPE, REMEMBER_WORD_MON };

    private static final int REMEMBER_WORD_ENG_INDEX  = 0;
    private static final int REMEMBER_WORD_TYPE_INDEX = 1;
    private static final int REMEMBER_WORD_MON_INDEX  = 2;

    public RememberWordsAdapter(Context context) {
        super(context);
    }

    public void addRememberWord(RememberWord rememberWord) {
        if (rememberWord == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(REMEMBER_WORD_ENG, rememberWord.getRememberEnglish());
        cv.put(REMEMBER_WORD_TYPE, rememberWord.getRememberType());
        cv.put(REMEMBER_WORD_MON, rememberWord.getRememberMongolia());
        db.insert(TABLE_REMEMBER_WORDS, null, cv);
        db.close();
    }

    public RememberWord getRememberWord(String id) {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(TABLE_REMEMBER_WORDS, PROJECTIONS_REMEMBER_WORDS, REMEMBER_WORD_ENG + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        RememberWord rememberWord = new RememberWord(cursor.getString(REMEMBER_WORD_ENG_INDEX),
                cursor.getString(REMEMBER_WORD_TYPE_INDEX),
                cursor.getString(REMEMBER_WORD_MON_INDEX));
        cursor.close();
        return rememberWord;
    }

    public Cursor checkRememberWord(String english){

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_REMEMBER_WORDS, new String[]{REMEMBER_WORD_ENG, REMEMBER_WORD_TYPE, REMEMBER_WORD_MON},
                REMEMBER_WORD_ENG + "='" + english + "'", null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public List<RememberWord> getAllRememberWords() {
        List<RememberWord> rwords = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_REMEMBER_WORDS +" ORDER BY " +REMEMBER_WORD_ENG+" DESC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String eng = cursor.getString(REMEMBER_WORD_ENG_INDEX);
                String type = cursor.getString(REMEMBER_WORD_TYPE_INDEX);
                String mon = cursor.getString(REMEMBER_WORD_MON_INDEX);
                RememberWord rememberWord = new RememberWord(eng,type,mon);
                rwords.add(rememberWord);
            } while (cursor.moveToNext());
        }

        Log.d(TAG, "" + rwords);
        cursor.close();
        return rwords;
    }

    public void deleteRememberWord(RememberWord rememberWord) {
        if (rememberWord == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.delete(TABLE_REMEMBER_WORDS, REMEMBER_WORD_ENG + "=?", new String[]{String.valueOf(rememberWord.getRememberEnglish())});
        db.close();
    }

}
