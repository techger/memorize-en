package com.memorize;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.memorize.activity.Settings;
import com.memorize.activity.Web;
import com.memorize.activity.WordDetail;
import com.memorize.activity.WordRemember;
import com.memorize.database.RememberWordsAdapter;
import com.memorize.model.RememberWord;
import com.memorize.sensor.ShakeEventManager;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ShakeEventManager.ShakeListener {
    private static final String TAG = "Main";

    boolean doubleBackToExitPressedOnce = false;

    public static final String PREFER_NAME = "Memorize";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ListView wordList;
    private List<NameValuePair> params;
    final Context context = this;
    private WordsAdapter wordsAdapter;
    private RememberWordsAdapter rememberWordsAdapter;
    private ShakeEventManager shakeEventManager;
    private NavigationView navigationView;
    final ArrayList<String> wordListItems = new ArrayList<String>();
    private ArrayAdapter<String> myArrayAdapter;

    private TextView englishWordInput;
    private TextView wordTypeInput;
    private TextView mongolianWordInput;
    private Button saveButton;
    private MyAlertDialog alert;
    private Animation shake;
    private Animation myshake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        rememberWordsAdapter = new RememberWordsAdapter(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        shakeEventManager = new ShakeEventManager();
        shakeEventManager.setListener(this);
        shakeEventManager.init(this);
    }

    private void init(){

        wordList = (ListView)findViewById(R.id.wordListView);

        wordsAdapter = new WordsAdapter(this);
        sharedPreferences = getApplicationContext().getSharedPreferences(PREFER_NAME, 0);
        editor = sharedPreferences.edit();

        myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,wordListItems);
        wordList.setAdapter(myArrayAdapter);
        wordList.setTextFilterEnabled(true);
        List<Word> words = wordsAdapter.getAllWords();

        try {
            Log.d(TAG, "Үг нэмж байна...");
            for (Word word : words){
                String wordAdd = word.getEnglish();
                //  Log.d(TAG,wordAdd);
                wordListItems.add(0, wordAdd);
            }
        }catch (NullPointerException npe){
            Log.d(TAG,"Алдаа : "+npe);
        }catch (Exception e){
            Log.d(TAG,"Алдаа : "+e);
        }

        wordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                final String english = o.toString();
                Log.d(TAG, "Дарагдсан лист дээрх үг : " + english);
                editor.putString("SearchedWord", english);
                editor.commit();
                Intent intent = new Intent(Main.this, WordDetail.class);
                startActivity(intent);
            }
        });
        myArrayAdapter.notifyDataSetChanged();

    }

    // Шинэ үг нэмэх dialog
    public void onCreateDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.word_add);
        dialog.setTitle("Шинэ үг нэмэх");
        WindowManager.LayoutParams layoutParam = new WindowManager.LayoutParams();
        layoutParam.copyFrom(dialog.getWindow().getAttributes());
        layoutParam.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParam.height = WindowManager.LayoutParams.WRAP_CONTENT;

        alert = new MyAlertDialog();
        wordsAdapter = new WordsAdapter(this);
        englishWordInput = (TextView)findViewById(R.id.englishWordInput);
        wordTypeInput = (TextView)findViewById(R.id.wordTypeInput);
        mongolianWordInput = (TextView)findViewById(R.id.mongolianWordInput);

        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        myshake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.myshake);

        saveButton = (Button) findViewById(R.id.newWordSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addNewWord();
//            }
//        });
        dialog.show();

        dialog.getWindow().setAttributes(layoutParam);

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
        final ProgressDialog progressDialog = new ProgressDialog(Main.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Шинэ үг нэмж байна...");
        progressDialog.show();

        // TODO: Implement your own signup logic here.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        alert.showAlertDialog(Main.this, "    Амжилттай бүртгэгдлээ.", "Шинэ үг амжилттай нэмлээ...", true);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                myArrayAdapter.getFilter().filter(newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                myArrayAdapter.getFilter().filter(query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.search) {

        } else if (id == R.id.add) {
//            Intent intent = new Intent(Main.this, WordAdd.class);
//            startActivity(intent);
            onCreateDialog();

        } else if (id == R.id.rememberWord){
            Intent intent = new Intent(Main.this, WordRemember.class);
            startActivity(intent);

        } else if (id == R.id.settings) {
            Intent intent = new Intent(Main.this, Settings.class);
            startActivity(intent);

        } else if (id == R.id.github) {
            Intent intent = new Intent(Main.this, Web.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Гарах бол дахин дарна уу.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    public void onShake() {
        Log.d(TAG,"Shake shake shake shake shake");
        List<String> remember = new ArrayList<String>();
        //qList.add("");
        List<RememberWord> rememberWords = rememberWordsAdapter.getAllRememberWords();
        for (RememberWord rememberWord : rememberWords){
            String listWord = "\n"+rememberWord.getRememberEnglish() +
                    " - "+rememberWord.getRememberType()+
                    " "+rememberWord.getRememberMongolia()+"";
            remember.add(listWord);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
        builder.setTitle("Цээжлэх үгийн жагсаалт");
        builder.setIcon(R.drawable.task);
        builder.setMessage(remember.toString());
        builder.setPositiveButton("Хаах", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onResume();
                // Do something
            }
        });
        builder.show();
        onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        shakeEventManager.register();
    }
    @Override
    protected void onPause() {
        super.onPause();
        shakeEventManager.deregister();
    }
}
