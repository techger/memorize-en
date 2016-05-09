package com.memorize.Database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.memorize.Model.RememberWord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.memorize.Model.Word;
import com.memorize.R;

/**
 * Created by Tortuvshin on 10/25/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context myContext;

    private static final String TAG = "===DatabaseHandler===";
    private static final int    DATABASE_VERSION = 1;
    private static final String DATABASE_NAME    = "mydictionary.db";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE users (" +
            UserAdapter.USER_ID + " INTEGER PRIMARY KEY," +
            UserAdapter.USER_NAME + " TEXT," +
            UserAdapter.USER_EMAIL + " TEXT," +
            UserAdapter.USER_PASSWORD + " TEXT)";

    private static final String CREATE_TABLE_WORDS = "CREATE TABLE words (" +
            WordsAdapter.WORD_ENG + " TEXT PRIMARY KEY," +
            WordsAdapter.WORD_TYPE + " TEXT," +
            WordsAdapter.WORD_MON + " TEXT)";

    private static final String CREATE_TABLE_REMEMBER_WORDS = "CREATE TABLE remember_words ("+
            RememberWordsAdapter.REMEMBER_WORD_ENG + " TEXT PRIMARY KEY,"+
            RememberWordsAdapter.REMEMBER_WORD_TYPE + " TEXT,"+
            RememberWordsAdapter.REMEMBER_WORD_MON + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_WORDS);
        db.execSQL(CREATE_TABLE_REMEMBER_WORDS);

        ContentValues contentValues = new ContentValues();
        Resources resources = myContext.getResources();

        XmlResourceParser _xml = resources.getXml(R.xml.word_list);
        try
        {
            //Check for end of document
            int eventType = _xml.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //Search for record tags
                if ((eventType == XmlPullParser.START_TAG) &&(_xml.getName().equals("word"))){
                    //Record tag found, now get values and insert record
                    String word_eng = _xml.getAttributeValue(null, WordsAdapter.WORD_ENG);
                    String word_type =  _xml.getAttributeValue(null, WordsAdapter.WORD_TYPE);
                    String word_mon = _xml.getAttributeValue(null, WordsAdapter.WORD_MON);
                    contentValues.put(WordsAdapter.WORD_ENG, word_eng);
                    contentValues.put(WordsAdapter.WORD_TYPE, word_type);
                    contentValues.put(WordsAdapter.WORD_MON, word_mon);
                    db.insert(WordsAdapter.TABLE_WORDS, null, contentValues);
                    Log.d(TAG,"XML-ээс амжилттай уншлаа...");
                }
                eventType = _xml.next();
            }
        }
        //Catch errors
        catch (XmlPullParserException e)
        {
            Log.d(TAG, e.getMessage(), e);
        }
        catch (IOException e)
        {
            Log.d(TAG, e.getMessage(), e);

        }
        finally
        {
            //Close the xml file
            _xml.close();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(UserAdapter.TABLE_USERS);
        dropTable(WordsAdapter.TABLE_WORDS);
        dropTable(RememberWordsAdapter.TABLE_REMEMBER_WORDS);
        onCreate(db);
    }

    public void dropTable(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        if (db == null || TextUtils.isEmpty(tableName)) {
            return;
        }
        db.execSQL("DROP TABLE IF EXISTS " + UserAdapter.TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + WordsAdapter.TABLE_WORDS);
        db.execSQL("DROP TABLE IF EXISTS " + RememberWordsAdapter.TABLE_REMEMBER_WORDS);
    }
}
