package programmer.laboratore_6.Database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import programmer.laboratore_6.Model.User;
import java.util.ArrayList;
import java.util.List;
import programmer.laboratore_6.Model.User;
import programmer.laboratore_6.Model.Word;

/**
 * Created by Byambaa on 10/25/2015.
 */
public class MyDbHandler extends SQLiteOpenHelper {
    private static final String TAG = "Database Handler";
    private static final int    DATABASE_VERSION = 1;
    private static final String DATABASE_NAME    = "myapp2.db";

    public static final String TABLE_USERS = "users";
    public static final String USER_ID         = "id";
    public static final String USER_NAME       = "name";
    public static final String USER_EMAIL   = "email";
    public static final String USER_PASSWORD = "password";

    public static final String TABLE_WORDS = "words";
    public static final String WORD_WORD       = "word";
    public static final String WORD_DESCRIPTION   = "description";

    private static final String[] PROJECTIONS_USERS = {USER_ID, USER_NAME, USER_EMAIL,USER_PASSWORD};
    private static final String[] PROJECTIONS_WORDS = {WORD_WORD, WORD_DESCRIPTION};

    private static final int USER_ID_INDEX       = 0;
    private static final int USER_NAME_INDEX     = 1;
    private static final int USER_EMAIL_INDEX    = 2;
    private static final int USER_PASSWORD_INDEX = 3;

    private static final int WORD_WORD_INDEX     = 0;
    private static final int WORD_DESCRIPTION_INDEX    = 1;

    private static final String CREATE_TABLE_USERS = "CREATE TABLE users (" +
            USER_ID + " INTEGER PRIMARY KEY," +
            USER_NAME + " TEXT," +
            USER_EMAIL + " TEXT," +
            USER_PASSWORD + " TEXT)";

    private static final String CREATE_TABLE_WORDS = "CREATE TABLE words (" +
            WORD_WORD + " TEXT PRIMARY KEY," +
            WORD_DESCRIPTION + " TEXT)";

    public MyDbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_WORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(TABLE_USERS);
        dropTable(TABLE_WORDS);

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
        cv.put(WORD_WORD, word.getWord());
        cv.put(WORD_DESCRIPTION, word.getDescription());
        // Inserting Row
        db.insert(TABLE_WORDS, null, cv);
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
    public Word getWord(int id) {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(TABLE_WORDS, PROJECTIONS_WORDS, WORD_WORD + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        Word word = new Word(cursor.getString(WORD_WORD_INDEX),
                cursor.getString(WORD_DESCRIPTION_INDEX));
        cursor.close();
        return word;
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

    public Cursor checkWord(String word,String description){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_WORDS,new String[]{WORD_WORD,WORD_DESCRIPTION},
                WORD_WORD +    "='" +word + "' AND " +
                        WORD_DESCRIPTION + "='"+WORD_DESCRIPTION+"'",null,null,null,null);
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
        String selectQuery = "SELECT * FROM " + TABLE_WORDS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String word = cursor.getString(WORD_WORD_INDEX);
                String description = cursor.getString(WORD_DESCRIPTION_INDEX);
                Word word1 = new Word(word,description);
                words.add(word1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return words;
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
