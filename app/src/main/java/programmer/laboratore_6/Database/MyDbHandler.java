package programmer.laboratore_6.Database;
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

import programmer.laboratore_6.Model.RememberWord;
import programmer.laboratore_6.Model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import programmer.laboratore_6.Model.User;
import programmer.laboratore_6.Model.Word;
import programmer.laboratore_6.R;

/**
 * Created by Byambaa on 10/25/2015.
 */
public class MyDbHandler extends SQLiteOpenHelper {

    private Context myContext;

    private static final String TAG = "===DatabaseHandler===";
    private static final int    DATABASE_VERSION = 1;
    private static final String DATABASE_NAME    = "dictionary5.db";

    public static final String TABLE_USERS   = "users";
    public static final String USER_ID       = "id";
    public static final String USER_NAME     = "name";
    public static final String USER_EMAIL    = "email";
    public static final String USER_PASSWORD = "password";

    public static final String TABLE_WORDS = "words";
    public static final String WORD_ENG    = "english";
    public static final String WORD_TYPE   = "type";
    public static final String WORD_MON    = "mongolia";

    public static final String TABLE_REMEMBER_WORDS = "remember_words";
    public static final String REMEMBER_WORD_ENG = "remember_word_english";
    public static final String REMEMBER_WORD_TYPE = "remember_word_type";
    public static final String REMEMBER_WORD_MON = "remember_word_mongolia";

    private static final String[] PROJECTIONS_USERS = {USER_ID, USER_NAME, USER_EMAIL,USER_PASSWORD};
    private static final String[] PROJECTIONS_WORDS = {WORD_ENG, WORD_TYPE, WORD_MON};
    private static final String[] PROJECTIONS_REMEMBER_WORDS = {REMEMBER_WORD_ENG, REMEMBER_WORD_TYPE, REMEMBER_WORD_MON};

    private static final int USER_ID_INDEX       = 0;
    private static final int USER_NAME_INDEX     = 1;
    private static final int USER_EMAIL_INDEX    = 2;
    private static final int USER_PASSWORD_INDEX = 3;

    private static final int WORD_ENG_INDEX     = 0;
    private static final int WORD_TYPE_INDEX    = 1;
    private static final int WORD_MON_INDEX     = 2;

    private static final int REMEMBER_WORD_ENG_INDEX = 0;
    private static final int REMEMBER_WORD_TYPE_INDEX = 1;
    private static final int REMEMBER_WORD_MON_INDEX= 2;

    private static final String CREATE_TABLE_USERS = "CREATE TABLE users (" +
            USER_ID + " INTEGER PRIMARY KEY," +
            USER_NAME + " TEXT," +
            USER_EMAIL + " TEXT," +
            USER_PASSWORD + " TEXT)";

    private static final String CREATE_TABLE_WORDS = "CREATE TABLE words (" +
            WORD_ENG + " TEXT PRIMARY KEY," +
            WORD_TYPE + " TEXT," +
            WORD_MON + " TEXT)";

    private static final String CREATE_TABLE_REMEMBER_WORDS = "CREATE TABLE remember_words ("+
            REMEMBER_WORD_ENG + " TEXT PRIMARY KEY,"+
            REMEMBER_WORD_TYPE + " TEXT,"+
            REMEMBER_WORD_MON + " TEXT)";

    public MyDbHandler(Context context) {
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
                    String word_eng = _xml.getAttributeValue(null, WORD_ENG);
                    String word_type =  _xml.getAttributeValue(null, WORD_TYPE);
                    String word_mon = _xml.getAttributeValue(null, WORD_MON);
                    contentValues.put(WORD_ENG, word_eng);
                    contentValues.put(WORD_TYPE, word_type);
                    contentValues.put(WORD_MON, word_mon);
                    db.insert(TABLE_WORDS, null, contentValues);
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
        dropTable(TABLE_USERS);
        dropTable(TABLE_WORDS);
        dropTable(TABLE_REMEMBER_WORDS);
        onCreate(db);
    }
    public void addUser(User user) {
        if (user == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(USER_NAME, user.getUserName());
        cv.put(USER_EMAIL, user.getUserEmail());
        cv.put(USER_PASSWORD, user.getUserPassword());
        // Inserting Row
        db.insert(TABLE_USERS, null, cv);
        db.close();
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
    public User getUser(int id) {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(TABLE_USERS, PROJECTIONS_USERS, USER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        User user = new User(cursor.getInt(USER_ID_INDEX),
                cursor.getString(USER_NAME_INDEX),
                cursor.getString(USER_EMAIL_INDEX),
                cursor.getString(USER_PASSWORD_INDEX));
        cursor.close();
        return user;
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

    public Cursor checkUser(String username,String password){

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS,new String[]{USER_ID,USER_NAME,USER_EMAIL,USER_PASSWORD},
                USER_NAME +    "='" +username + "' AND " +
                        USER_PASSWORD + "='"+password+"'",null,null,null,null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
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

    public Cursor checkRememberWord(String english){

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_REMEMBER_WORDS,new String[]{REMEMBER_WORD_ENG,REMEMBER_WORD_TYPE,REMEMBER_WORD_MON},
                REMEMBER_WORD_ENG +    "='" +english +"'",null,null,null,null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(USER_ID_INDEX);
                String name = cursor.getString(USER_NAME_INDEX);
                String email = cursor.getString(USER_EMAIL_INDEX);
                String password = cursor.getString(USER_PASSWORD_INDEX);
                User user = new User(id, name, email, password);
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
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

        Log.d(TAG,""+words);
        cursor.close();
        return words;
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

        Log.d(TAG,""+rwords);
        cursor.close();
        return rwords;
    }
    public int updateUser(User user) {
        if (user == null) {
            return -1;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return -1;
        }
        ContentValues cv = new ContentValues();
        cv.put(USER_NAME, user.getUserName());
        cv.put(USER_EMAIL, user.getUserEmail());
        cv.put(USER_PASSWORD,user.getUserPassword());
        // Upating the row
        int rowCount = db.update(TABLE_USERS, cv, USER_ID + "=?",
                new String[]{String.valueOf(user.getUserId())});
        db.close();
        return rowCount;
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

    public void deleteUser(User user) {
        if (user == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.delete(TABLE_USERS, USER_ID + "=?", new String[]{String.valueOf(user.getUserId())});
        db.close();
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
    public void deleteRememberWord(RememberWord rememberWord) {
        if (rememberWord == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.delete(TABLE_REMEMBER_WORDS, WORD_ENG + "=?", new String[]{String.valueOf(rememberWord.getRememberEnglish())});
        db.close();
    }
    public int getUserCount() {
        String query = "SELECT * FROM  " + TABLE_USERS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public void dropTable(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        if (db == null || TextUtils.isEmpty(tableName)) {
            return;
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
    }
}
