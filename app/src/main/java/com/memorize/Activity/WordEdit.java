package com.memorize.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.memorize.component.MyAlertDialog;
import com.memorize.database.WordsAdapter;
import com.memorize.Main;
import com.memorize.model.Word;
import com.memorize.R;

public class WordEdit extends AppCompatActivity {

    private static final String TAG = "===EditWord===";
    EditText englishWordInput;
    EditText wordTypeInput;
    EditText mongolianWordInput;
    FloatingActionButton saveButton;
    WordsAdapter wordsAdapter;
    MyAlertDialog alert;
    Animation shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_edit);
        init();
    }

    public void init(){
        alert = new MyAlertDialog();
        wordsAdapter = new WordsAdapter(this);
        englishWordInput = (EditText)findViewById(R.id.englishWordInputE);
        wordTypeInput = (EditText)findViewById(R.id.wordTypeInputE);
        mongolianWordInput = (EditText)findViewById(R.id.mongolianWordInputE);
        saveButton = (FloatingActionButton)findViewById(R.id.editedWordSave);

        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        SharedPreferences prefs = getSharedPreferences(Main.PREFER_NAME, 0);
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
                editedWord();
            }
        });
    }


    public void editedWord(){
        if (!validate()){
            Toast.makeText(getBaseContext(), "Үг засхад алдаа гарлаа", Toast.LENGTH_LONG).show();
            return;
        }


        final ProgressDialog progressDialog = new ProgressDialog(WordEdit.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Шинэчилж байна...");
        progressDialog.show();
        // TODO: Implement your own word edit logic here.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        alert.showAlertDialog(WordEdit.this, "    Амжилттай шинэчиллээ.", "Амжилттай шинэчиллээ....", true);
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        finish();
                                    }
                                }, 1000);
                        String eng = englishWordInput.getText().toString();
                        String type = wordTypeInput.getText().toString();
                        String mon = mongolianWordInput.getText().toString();
                        wordsAdapter.updateWord(new Word(eng, type, mon));
                        Log.d(TAG, "Амжилттай заслаа " + eng + ", " + type + ", " + mon);
                        Toast.makeText(getApplicationContext(), "Амжилттай заслаа", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }, 1000);

    }

    public boolean validate(){
        boolean valid = true;

        String english = englishWordInput.getText().toString();
        String type = wordTypeInput.getText().toString();
        String mongolia = mongolianWordInput.getText().toString();

        if (english.isEmpty() || type.isEmpty() || mongolia.isEmpty()) {
            englishWordInput.startAnimation(shake);
            wordTypeInput.startAnimation(shake);
            mongolianWordInput.startAnimation(shake);
            englishWordInput.setError("талбар хоосон байна");
            wordTypeInput.setError("талбар хоосон байна");
            mongolianWordInput.setError("талбар хоосон байна");
            valid = false;
        } else {
            englishWordInput.setError(null);
            wordTypeInput.setError(null);
            mongolianWordInput.setError(null);
        }

        return valid;
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
