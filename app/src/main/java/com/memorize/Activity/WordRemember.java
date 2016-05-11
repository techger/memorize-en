package com.memorize.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.memorize.component.MyAlertDialog;
import com.memorize.database.RememberWordsAdapter;
import com.memorize.R;

import java.util.ArrayList;
import java.util.List;

public class WordRemember extends AppCompatActivity {

    private static final String TAG = "Remember";
    ListView wordList;
    RememberWordsAdapter rememberWordsAdapter;
    MyAlertDialog myAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember_word);
        init();
    }

    private void init(){

        wordList = (ListView)findViewById(R.id.rememberWordListView);
        rememberWordsAdapter = new RememberWordsAdapter(this);

        final ArrayList<String> wordListItems = new ArrayList<String>();
        final ArrayAdapter<String> myArrayAdapter;
        myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,wordListItems);
        wordList.setAdapter(myArrayAdapter);
        List<com.memorize.model.RememberWord> words = rememberWordsAdapter.getAllRememberWords();
        try {
            for (com.memorize.model.RememberWord rememberWord : words){
                String wordAdd = "\n"+rememberWord.getRememberEnglish() +" - "+rememberWord.getRememberType()+
                        " ,"+rememberWord.getRememberMongolia();
                Log.d(TAG, wordAdd);
                wordListItems.add(0, wordAdd);
            }
        }catch (NullPointerException npe){
            Log.e(TAG,"Алдаа : "+npe);
        }catch (Exception e){
            Log.e(TAG,"Алдаа : "+e);
        }
    }
}
