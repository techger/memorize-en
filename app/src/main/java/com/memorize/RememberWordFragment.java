package com.memorize;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.memorize.Database.DatabaseHelper;
import com.memorize.Model.RememberWord;

public class RememberWordFragment extends Fragment {

    private static final String TAG = "===RememberFragment===";
    ListView wordList;
    DatabaseHelper databaseHelper;
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
        databaseHelper = new DatabaseHelper(getActivity());

        final ArrayList<String> wordListItems = new ArrayList<String>();
        final ArrayAdapter<String> myArrayAdapter;
        myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,wordListItems);
        wordList.setAdapter(myArrayAdapter);
        List<RememberWord> words = databaseHelper.getAllRememberWords();
        try {
            for (RememberWord rememberWord : words){
                String wordAdd = "\n"+rememberWord.getRememberEnglish() +" - "+rememberWord.getRememberType()+
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