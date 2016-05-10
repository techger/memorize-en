package com.memorize.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.memorize.Component.MyAlertDialog;
import com.memorize.Database.RememberWordsAdapter;
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
        List<com.memorize.Model.RememberWord> words = rememberWordsAdapter.getAllRememberWords();
        try {
            for (com.memorize.Model.RememberWord rememberWord : words){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remember_word, menu);
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
