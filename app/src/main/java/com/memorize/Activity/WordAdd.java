package com.memorize.activity;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.memorize.R;
import com.memorize.component.MyAlertDialog;
import com.memorize.database.WordsAdapter;
import com.memorize.model.Word;
import com.memorize.service.ServerRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WordAdd extends AppCompatActivity {

    private static final String TAG = "===AddWordActivity===";
    TextView englishWordInput;
    TextView wordTypeInput;
    TextView mongolianWordInput;
    FloatingActionButton saveButton;
    WordsAdapter wordsAdapter;
    MyAlertDialog alert;
    Animation shake;
    Animation myshake;

    List<NameValuePair> params;

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

        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        myshake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.myshake);

        saveButton = (FloatingActionButton)findViewById(R.id.newWordSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewWord();
            }
        });
    }

    public void addNewWord(){
        if (!validate()){
            Toast.makeText(getBaseContext(), "Үг нэмхэд алдаа гарлаа", Toast.LENGTH_LONG).show();
            return;
        }

        String english = englishWordInput.getText().toString();
        String type = wordTypeInput.getText().toString();
        String mongolia = mongolianWordInput.getText().toString();

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("eng", english));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("mon", mongolia));

        ServerRequest sr = new ServerRequest();
        JSONObject json = sr.getJSON("https://memorize-server-tortuvshin.c9users.io/addword",params);

        if(json != null){
            try{
                String jsonstr = json.getString("response");

                Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();

                Log.d("Hello", jsonstr);
            }catch (JSONException e) {
                e.printStackTrace();
                Log.e("Алдаа"," алдаа"+e);
            }
        }
        final ProgressDialog progressDialog = new ProgressDialog(WordAdd.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Шинэ үг нэмж байна...");
        progressDialog.show();

        // TODO: Implement your own signup logic here.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        alert.showAlertDialog(WordAdd.this, "    Амжилттай бүртгэгдлээ.", "Шинэ үг амжилттай нэмлээ...", true);
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        finish();
                                    }
                                }, 1000);
                        Log.d(TAG, "Бүртгэл амжилттай боллоо...");
                        String english = englishWordInput.getText().toString();
                        String type = wordTypeInput.getText().toString();
                        String mongolia = mongolianWordInput.getText().toString();
                        wordsAdapter.addWord(new Word(english, type, mongolia));


                        Toast.makeText(getApplicationContext(), "Амжилттай нэмлээ", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Амжилттай нэмлээ");
                        progressDialog.dismiss();
                    }
                }, 1000);

    }

    public boolean validate(){
        boolean valid = true;

        String english = englishWordInput.getText().toString();
        String type = wordTypeInput.getText().toString();
        String mongolia = mongolianWordInput.getText().toString();

        Cursor words = wordsAdapter.checkWord(english);

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

        if (words == null) {
            alert.showAlertDialog(getApplicationContext(), "Error", "Database query error", false);
            Log.e(TAG, "Өгөгдлийн сангийн query алдаатай байна");
            valid = false;
        } else {
            startManagingCursor(words);
            if (words.getCount() > 0) {
                alert.showAlertDialog(getApplicationContext(), "Алдаа", "Бүртгэлтэй үг байна", false);
                Log.e(TAG, "Бүртгэлтэй үг байна");
                stopManagingCursor(words);
                words.close();
                valid = false;
            }
        }

        return valid;
    }
}
