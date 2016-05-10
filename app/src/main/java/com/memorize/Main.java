package com.memorize;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.memorize.activity.Settings;
import com.memorize.activity.Web;
import com.memorize.activity.WordAdd;
import com.memorize.activity.WordDetail;
import com.memorize.activity.WordRemember;
import com.memorize.database.RememberWordsAdapter;
import com.memorize.database.WordsAdapter;
import com.memorize.model.RememberWord;
import com.memorize.model.Word;
import com.memorize.sensor.ShakeEventManager;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ShakeEventManager.ShakeListener {
    private static final String TAG = "Main";

    public static final String PREFER_NAME = "Memorize";
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ListView wordList;
    WordsAdapter wordsAdapter;
    RememberWordsAdapter rememberWordsAdapter;
    private ShakeEventManager shakeEventManager;
    NavigationView navigationView;
    final ArrayList<String> wordListItems = new ArrayList<String>();
    ArrayAdapter<String> myArrayAdapter;

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
            Log.d(TAG, "Inserting words...");
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
            Intent intent = new Intent(Main.this, WordAdd.class);
            startActivity(intent);

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
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
            Log.d(TAG,"Back pressed");
        }
        else{
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.exit)
                    .setTitle("Толь бичиг")
                    .setMessage("Та програмаас гарах уу?")
                    .setPositiveButton("Тийм", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Үгүй", null)
                    .show();
        }

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
