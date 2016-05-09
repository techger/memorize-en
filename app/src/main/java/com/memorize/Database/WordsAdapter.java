package com.memorize.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.memorize.Model.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tortuvshin on 5/10/2016.
 */
public class WordsAdapter extends DatabaseHelper{

    private static final String TAG = "===WordsAdapter===";

    public static final String TABLE_WORDS = "words";
    public static final String WORD_ENG    = "english";
    public static final String WORD_TYPE   = "type";
    public static final String WORD_MON    = "mongolia";

    private static final String[] PROJECTIONS_WORDS = {WORD_ENG, WORD_TYPE, WORD_MON};

    private static final int WORD_ENG_INDEX     = 0;
    private static final int WORD_TYPE_INDEX    = 1;
    private static final int WORD_MON_INDEX     = 2;

    public WordsAdapter(Context context) {
        super(context);
    }

    public void addWord(Word word) {
        if (word == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(WORD_ENG, word.getEnglish());
        cv.put(WORD_TYPE, word.getType());
        cv.put(WORD_MON, word.getMongolia());
        // Inserting Row
        db.insert(TABLE_WORDS, null, cv);
        db.close();
    }

    public Word getWord(String id) {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(TABLE_WORDS, PROJECTIONS_WORDS, WORD_ENG + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        Word word = new Word(cursor.getString(WORD_ENG_INDEX),
                cursor.getString(WORD_TYPE_INDEX),
                cursor.getString(WORD_MON_INDEX));
        cursor.close();
        return word;
    }

    public Cursor checkWord(String english){

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_WORDS,new String[]{WORD_ENG,WORD_TYPE,WORD_MON},
                WORD_ENG +    "='" +english +"'",null,null,null,null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public List<Word> getAllWords() {
        List<Word> words = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_WORDS +" ORDER BY " +WORD_ENG+" DESC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String eng = cursor.getString(WORD_ENG_INDEX);
                String type = cursor.getString(WORD_TYPE_INDEX);
                String mon = cursor.getString(WORD_MON_INDEX);
                Word word1 = new Word(eng,type,mon);
                words.add(word1);
            } while (cursor.moveToNext());
        }

        Log.d(TAG, "" + words);
        cursor.close();
        return words;
    }

    public int updateWord(Word word) {
        if (word == null) {
            return -1;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return -1;
        }
        ContentValues cv = new ContentValues();
        cv.put(WORD_ENG, word.getEnglish());
        cv.put(WORD_TYPE, word.getType());
        cv.put(WORD_MON, word.getMongolia());

        // Upating the row
        int rowCount = db.update(TABLE_WORDS, cv, WORD_ENG + "=?",
                new String[]{String.valueOf(word.getEnglish())});
        db.close();
        return rowCount;
    }

    public void deleteWord(Word word) {
        if (word == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.delete(TABLE_WORDS, WORD_ENG + "=?", new String[]{String.valueOf(word.getEnglish())});
        db.close();
    }

}
