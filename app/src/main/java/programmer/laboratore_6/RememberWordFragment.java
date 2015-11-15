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
import programmer.laboratore_6.Model.RememberWord;
import programmer.laboratore_6.Model.Word;
public class RememberWordFragment extends Fragment {

    private static final String TAG = "===RememberFragment===";
    ListView wordList;
    MyDbHandler myDbHandler;
    AlertDialogManager alertDialogManager;
    private static View rootView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_remember_word, container, false);
        init();
        return rootView;
    }
    private void init(){

        wordList = (ListView)rootView.findViewById(R.id.rememberWordListView);
        myDbHandler = new MyDbHandler(getActivity());

        final ArrayList<String> wordListItems = new ArrayList<String>();
        final ArrayAdapter<String> myArrayAdapter;
        myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,wordListItems);
        wordList.setAdapter(myArrayAdapter);
        List<RememberWord> words = myDbHandler.getAllRememberWords();
        try {
            for (RememberWord rememberWord : words){
                String wordAdd = ""+rememberWord.getRememberEnglish() +" - "+rememberWord.getRememberType()+
                        " ,"+rememberWord.getRememberMongolia();
                Log.d(TAG, wordAdd);
                wordListItems.add(0, wordAdd);
            }
        }catch (NullPointerException npe){
            Log.d(TAG,"Алдаа : "+npe);
        }catch (Exception e){
            Log.d(TAG,"Алдаа : "+e);
        }
    }

}