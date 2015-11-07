package programmer.laboratore_6;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;

import programmer.laboratore_6.Database.MyDbHandler;
import programmer.laboratore_6.Model.User;
import programmer.laboratore_6.Model.Word;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    MyDbHandler myDbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment MainFragment = new MainFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction= fm.beginTransaction();
        fragmentTransaction.replace(R.id.container, MainFragment);
        fragmentTransaction.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Fragment MainFragment = new MainFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction= fm.beginTransaction();
            fragmentTransaction.replace(R.id.container, MainFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.add) {
            Fragment wordAddFragment = new WordAddFragment();
            FragmentManager fm = getFragmentManager();
            fm.popBackStack("test",0);
            FragmentTransaction fragmentTransaction= fm.beginTransaction();
            fragmentTransaction.replace(R.id.container, wordAddFragment);
            fragmentTransaction.addToBackStack("Add").commit();
        } else if (id == R.id.settings) {
            Fragment blogFragment = new BlogFragment();
            FragmentManager fm = getFragmentManager();
            fm.popBackStack("test",0);
            FragmentTransaction fragmentTransaction= fm.beginTransaction();
            fragmentTransaction.replace(R.id.container, blogFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.map) {

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
            new AlertDialog.Builder(this,R.style.AlertDialog)
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



}
