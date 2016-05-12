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
    public TextView wordtype;
    public TextView mongolia;
    FloatingActionButton editFButton;
    FloatingActionButton deleteFButton;
    FloatingActionButton wordListFButton;
    WordsAdapter wordsAdapter;
    RememberWordsAdapter rememberWordsAdapter;
    String eng = "";
    String type = "";
    String mon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        init();
    }

    public void init(){
        english = (TextView)findViewById(R.id.englishText);
        wordtype = (TextView)findViewById(R.id.typeText);
        mongolia= (TextView)findViewById(R.id.mongolianText);
        wordsAdapter = new WordsAdapter(this);
        rememberWordsAdapter = new RememberWordsAdapter(this);
        SharedPreferences prefs = getSharedPreferences(Main.PREFER_NAME, 0);
        String searchedWord = prefs.getString("SearchedWord", "");
        editor = prefs.edit();

        Word word = wordsAdapter.getWord(searchedWord);
        eng = word.getEnglish();
        type = word.getType();
        mon = word.getMongolia();
        english.setText(eng);
        wordtype.setText(type);
        mongolia.setText(mon);

        editFButton = (FloatingActionButton)findViewById(R.id.editFButton);
        deleteFButton = (FloatingActionButton)findViewById(R.id.deleteFButton);
        wordListFButton = (FloatingActionButton)findViewById(R.id.listWordButton);

        editFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("denglish", english.getText().toString());
                editor.putString("dtype", wordtype.getText().toString());
                editor.putString("dmongolia", mongolia.getText().toString());
                editor.commit();
                Intent intent = new Intent(WordDetail.this, WordEdit.class);
                startActivity(intent);
                Log.d(TAG, english.getText().toString() + "" + wordtype.getText().toString() + "" + mongolia.getText().toString());

                Snackbar.make(v, "Үг засах хэсэг", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });

        deleteFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(WordDetail.this)
                        .setIcon(R.drawable.fail)
                        .setTitle("Устгах")
                        .setMessage("Энэхүү үгийг устгах уу?")
                        .setPositiveButton("Тийм", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                final ProgressDialog progressDialog = new ProgressDialog(WordDetail.this,
                                        R.style.AppTheme_Dark_Dialog);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage("Устгаж байна...");
                                progressDialog.show();
                                // TODO: Implement your own authentication logic here.
                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                wordsAdapter.deleteWord(new Word(eng, type, mon));
                                                Log.d(TAG, "Амжилттай устгалаа..." + eng);
                                                progressDialog.dismiss();
                                                finish();
                                            }
                                        }, 1000);
                            }
                        })
                        .setNegativeButton("Үгүй", null)
                        .show();
            }
        });
        wordListFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eng = english.getText().toString();
                String type = wordtype.getText().toString();
                String mon = mongolia.getText().toString();
                Cursor words = rememberWordsAdapter.checkRememberWord(eng);
                if (words == null) {
                    Snackbar.make(v, "Өгөгдлийн сангийн query алдаатай байна...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.d(TAG,"Өгөгдлийн сангийн query алдаатай байна");
                } else {
                    startManagingCursor(words);
                    if (words.getCount() > 0) {
                        Snackbar.make(v, "Бүртгэлтэй үг байна...", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Log.d(TAG,"Бүртгэлтэй үг байна");
                        stopManagingCursor(words);
                        words.close();
                    } else {
                        rememberWordsAdapter.addRememberWord(new RememberWord(eng, type, mon));
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
