package com.memorize.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.memorize.database.RememberWordsAdapter;
import com.memorize.database.WordsAdapter;
import com.memorize.Main;
import com.memorize.model.RememberWord;
import com.memorize.model.Word;
import com.memorize.R;

public class WordDetail extends AppCompatActivity {

    private static final String TAG = "WordDetails";

    SharedPreferences.Editor editor;
    public TextView english;
    public TextView mongolia;
    FloatingActionButton editFButton;
    FloatingActionButton wordListFButton;
    WordsAdapter wordsAdapter;
    RememberWordsAdapter rememberWordsAdapter;
    String eng = "";
    String mon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        init();
    }


    public void init(){
        english = (TextView)findViewById(R.id.englishText);
//        wordtype = (TextView)findViewById(R.id.typeText);
        mongolia= (TextView)findViewById(R.id.mongolianText);
        wordsAdapter = new WordsAdapter(this);
        rememberWordsAdapter = new RememberWordsAdapter(this);
        SharedPreferences prefs = getSharedPreferences(Main.PREFER_NAME, 0);
        String searchedWord = prefs.getString("SearchedWord", "");
        editor = prefs.edit();

        Word word = wordsAdapter.getWord(searchedWord);
        eng = word.getEnglish();
        mon = word.getMongolia();
        english.setText(eng);
        mongolia.setText(mon);

        editFButton = (FloatingActionButton)findViewById(R.id.editFButton);
        wordListFButton = (FloatingActionButton)findViewById(R.id.listWordButton);

        editFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("denglish", english.getText().toString());
                editor.putString("dmongolia", mongolia.getText().toString());
                editor.commit();
                Intent intent = new Intent(WordDetail.this, WordEdit.class);
                startActivity(intent);
                Snackbar.make(v, "Үг засах хэсэг", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });


        wordListFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eng = english.getText().toString();
                String mon = mongolia.getText().toString();
                Cursor words = rememberWordsAdapter.checkRememberWord(eng);
                if (words == null) {
                    Snackbar.make(v, "Өгөгдлийн сангийн query алдаатай байна...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    startManagingCursor(words);
                    if (words.getCount() > 0) {
                        Snackbar.make(v, "Бүртгэлтэй үг байна...", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        stopManagingCursor(words);
                        words.close();
                    } else {
//                        rememberWordsAdapter.addRememberWord(new RememberWord(eng, type, mon));
                        Toast.makeText(getApplicationContext(), "Амжилттай нэмлээ", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Амжилттай нэмлээ");
                        Snackbar.make(v, "Цээжлэх үгийн жагсаалтанд амжилттай нэмэгдлээ...", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });
    }
}
