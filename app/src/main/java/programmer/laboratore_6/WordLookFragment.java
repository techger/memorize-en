package programmer.laboratore_6;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import programmer.laboratore_6.Database.MyDbHandler;
import programmer.laboratore_6.Model.Word;


public class WordLookFragment extends Fragment {

    private static final String TAG = "===WordLookFragment===";
    public static View rootView;
    public TextView english;
    public TextView wordtype;
    public TextView mongolia;
    FloatingActionButton editFButton;
    FloatingActionButton deleteFButton;
    FloatingActionButton wordListFButton;
    MyDbHandler myDbHandler;
    String eng = "";
    String type = "";
    String mon = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_word_look, container, false);
        init();
        return rootView;
    }
    public void init(){
        english = (TextView)rootView.findViewById(R.id.englishText);
        wordtype = (TextView)rootView.findViewById(R.id.typeText);
        mongolia= (TextView)rootView.findViewById(R.id.mongolianText);
        myDbHandler = new MyDbHandler(getActivity());
        SharedPreferences prefs = getActivity().getSharedPreferences(MainFragment.PREFER_NAME, 0);
        String searchedWord = prefs.getString("SearchedWord", "");
        Word word = myDbHandler.getWord(searchedWord);
        eng = word.getEnglish();
        type = word.getType();
        mon = word.getMongolia();
        english.setText(eng);
        wordtype.setText(type);
        mongolia.setText(mon);

        editFButton = (FloatingActionButton) rootView.findViewById(R.id.editFButton);
        deleteFButton = (FloatingActionButton) rootView.findViewById(R.id.deleteFButton);
        wordListFButton = (FloatingActionButton) rootView.findViewById(R.id.listWordButton);

        editFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("english",english.getText().toString());
                bundle.putString("type", wordtype.getText().toString());
                bundle.putString("mongolia", mongolia.getText().toString());
                WordEditFragment editFragment = new WordEditFragment();
                Log.d(TAG, english.getText().toString()+""+wordtype.getText().toString()+""+mongolia.getText().toString());
                editFragment.setArguments(bundle);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction= fm.beginTransaction();
                fragmentTransaction.replace(R.id.container, editFragment);
                fragmentTransaction.addToBackStack("Edit").commit();
                Snackbar.make(v, "Үг засах хэсэг", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        deleteFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDbHandler.deleteWord(new Word(eng,type,mon));
                Log.d(TAG, "Амжилттай устгалаа..."+eng);
                Snackbar.make(v, "Амжилттай устгалаа...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



    }

}
