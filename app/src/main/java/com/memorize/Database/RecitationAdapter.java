package com.memorize.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.memorize.model.Recitation;
import com.memorize.model.RememberWord;
import com.memorize.model.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Programmer on 5/12/2016.
 */
public class RecitationAdapter extends DatabaseHelper{

    private static final String TAG = "RecitationAdapter : ";

    public static final String TABLE_RECITATION = "recitations";
    public static final String RECITATION_ENG    = "english";
    public static final String RECITATION_TYPE   = "type";
    public static final String RECITATION_MON    = "mongolia";

    private static final String[] PROJECTIONS_RECITATIONS = {RECITATION_ENG, RECITATION_TYPE, RECITATION_MON};

    private static final int RECITATION_ENG_INDEX     = 0;
    private static final int RECITATION_TYPE_INDEX    = 1;
    private static final int RECITATION_MON_INDEX     = 2;

    public RecitationAdapter(Context context) {
        super(context);
    }

    public void addRecitation(Recitation recitation) {
        if (recitation == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(RECITATION_ENG, recitation.getEnglish());
        cv.put(RECITATION_TYPE, recitation.getType());
        cv.put(RECITATION_MON, recitation.getMongolia());
        // Inserting Row
        db.insert(TABLE_RECITATION, null, cv);
        db.close();
    }

    public Recitation getRecitation(String id) {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(TABLE_RECITATION, PROJECTIONS_RECITATIONS, RECITATION_ENG + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        Recitation recitation = new Recitation(cursor.getString(RECITATION_ENG_INDEX),
                cursor.getString(RECITATION_TYPE_INDEX),
                cursor.getString(RECITATION_MON_INDEX));
        cursor.close();
        return recitation;
    }

    public Cursor checkRecitation(String english){

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECITATION,new String[]{RECITATION_ENG,RECITATION_TYPE,RECITATION_MON},
                RECITATION_ENG +    "='" +english +"'",null,null,null,null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public List<Recitation> getAllRecitations() {
        List<Recitation> recitations = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RECITATION +" ORDER BY " +RECITATION_ENG+" DESC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String eng = cursor.getString(RECITATION_ENG_INDEX);
                String type = cursor.getString(RECITATION_TYPE_INDEX);
                String mon = cursor.getString(RECITATION_MON_INDEX);
                Recitation recitation = new Recitation(eng,type,mon);
                recitations.add(recitation);
            } while (cursor.moveToNext());
        }

        Log.d(TAG, "" + recitations);
        cursor.close();
        return recitations;
    }

    public int updateRecitation(Recitation recitation) {
        if (recitation == null) {
            return -1;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return -1;
        }
        ContentValues cv = new ContentValues();
        cv.put(RECITATION_ENG, recitation.getEnglish());
        cv.put(RECITATION_TYPE, recitation.getType());
        cv.put(RECITATION_MON, recitation.getMongolia());

        // Upating the row
        int rowCount = db.update(TABLE_RECITATION, cv, RECITATION_ENG + "=?",
                new String[]{String.valueOf(recitation.getEnglish())});
        db.close();
        return rowCount;
    }

    public void deleteRecitation(Recitation recitation) {
        if (recitation == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.delete(TABLE_RECITATION, RECITATION_ENG + "=?", new String[]{String.valueOf(recitation.getEnglish())});
        db.close();
    }

}
