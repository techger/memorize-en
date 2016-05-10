package com.memorize;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.memorize.Database.WordsAdapter;
import com.memorize.Model.Word;

public class WordEditActivity extends AppCompatActivity {

    private static final String TAG = "===EditWord===";
    EditText englishWordInput;
    EditText wordTypeInput;
    EditText mongolianWordInput;
    FloatingActionButton saveButton;
    WordsAdapter wordsAdapter;
    AlertDialogManager alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_edit);
        init();
    }

    public void init(){
        alert = new AlertDialogManager();
        wordsAdapter = new WordsAdapter(this);
        englishWordInput = (EditText)findViewById(R.id.englishWordInputE);
        wordTypeInput = (EditText)findViewById(R.id.wordTypeInputE);
        mongolianWordInput = (EditText)findViewById(R.id.mongolianWordInputE);
        saveButton = (FloatingActionButton)findViewById(R.id.editedWordSave);

        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFER_NAME, 0);
        String english = prefs.getString("denglish","error");
        String wordtype = prefs.getString("dtype","error");
        String mongolia = prefs.getString("dmongolia","error");
        englishWordInput.setText(english);
        wordTypeInput.setText(wordtype);
        mongolianWordInput.setText(mongolia);
        Log.d(TAG, "English: " + english + " Type: " + wordtype + " Mongolia: " + mongolia);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(WordEditActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Шинэчилж байна...");
                progressDialog.show();
                // TODO: Implement your own authentication logic here.
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String eng = englishWordInput.getText().toString();
                                String type = wordTypeInput.getText().toString();
                                String mon = mongolianWordInput.getText().toString();
                                wordsAdapter.updateWord(new Word(eng, type, mon));
                                Log.d(TAG, "Амжилттай заслаа " + eng + ", " + type + ", " + mon);
                                finish();
                                Toast.makeText(getApplicationContext(), "Амжилттай заслаа", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }, 1000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_word_edit, menu);
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
