package com.memorize.Activity;

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

import com.memorize.Component.MyAlertDialog;
import com.memorize.Database.WordsAdapter;
import com.memorize.Model.Word;
import com.memorize.R;

public class WordAdd extends AppCompatActivity {

    private static final String TAG = "===AddWordActivity===";
    TextView englishWordInput;
    TextView wordTypeInput;
    TextView mongolianWordInput;
    FloatingActionButton saveButton;
    WordsAdapter wordsAdapter;
    MyAlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_add);
        init();
    }

    public void init(){
        alert = new MyAlertDialog();
        wordsAdapter = new WordsAdapter(this);
        englishWordInput = (TextView)findViewById(R.id.englishWordInput);
        wordTypeInput = (TextView)findViewById(R.id.wordTypeInput);
        mongolianWordInput = (TextView)findViewById(R.id.mongolianWordInput);
        saveButton = (FloatingActionButton)findViewById(R.id.newWordSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eng = englishWordInput.getText().toString();
                String type = wordTypeInput.getText().toString();
                String mon = mongolianWordInput.getText().toString();
                Cursor words = wordsAdapter.checkWord(eng);

                if (words == null) {
                    alert.showAlertDialog(getApplicationContext(), "Error", "Database query error", false);
                    Log.e(TAG, "Өгөгдлийн сангийн query алдаатай байна");
                } else {
                    startManagingCursor(words);
                    if (words.getCount() > 0) {
                        alert.showAlertDialog(getApplicationContext(), "Алдаа", "Бүртгэлтэй үг байна", false);
                        Log.e(TAG, "Бүртгэлтэй үг байна");
                        stopManagingCursor(words);
                        words.close();
                    } else {
                        wordsAdapter.addWord(new Word(eng, type, mon));
                        Toast.makeText(getApplicationContext(), "Амжилттай нэмлээ", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Амжилттай нэмлээ");
                        Snackbar.make(v, "Шинэ үг амжилттай нэмэгдлээ...", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_word_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
