package programmer.laboratore_6;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import programmer.laboratore_6.Database.MyDbHandler;
import programmer.laboratore_6.Model.Word;
public class MainFragment extends Fragment {

    private static final String TAG = "===MainFragment===";
    public static final String PREFER_NAME = "SearchedWord";
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText searchEditText;
    ListView wordList;
    MyDbHandler myDbHandler;
    private static View rootView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        init();
        return rootView;
    }
    private void init(){

        wordList = (ListView)rootView.findViewById(R.id.wordListView);
        searchEditText = (EditText)rootView.findViewById(R.id.searchEditText);

        myDbHandler = new MyDbHandler(getActivity());
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(PREFER_NAME, 0);
        editor = sharedPreferences.edit();

        final ArrayList<String> wordListItems = new ArrayList<String>();
        final ArrayAdapter<String> myArrayAdapter;
        myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,wordListItems);
        wordList.setAdapter(myArrayAdapter);
        List<Word> words = myDbHandler.getAllWords();

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

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myArrayAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        wordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                final String english = o.toString();
                Log.d(TAG,"Дарагдсан лист дээрх үг : "+english);
                editor.putString("SearchedWord", english);
                editor.commit();
                Fragment wordLookFragment = new WordLookFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction= fm.beginTransaction();
                fragmentTransaction.replace(R.id.container, wordLookFragment);
                fragmentTransaction.addToBackStack("Word view").commit();
            }
        });
        myArrayAdapter.notifyDataSetChanged();

    }

}