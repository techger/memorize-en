package com.memorize;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.memorize.Database.DatabaseHelper;
import com.memorize.Database.RememberWordsAdapter;
import com.memorize.Database.WordsAdapter;
import com.memorize.Model.RememberWord;
import com.memorize.Model.Word;


public class WordLookFragment extends Fragment {

    private static final String TAG = "===WordLookFragment===";
    public static View rootView;
    public TextView english;
    public TextView wordtype;
    public TextView mongolia;
    FloatingActionButton editFButton;
    FloatingActionButton deleteFButton;
    FloatingActionButton wordListFButton;
    WordsAdapter wordsAdapter;
    RememberWordsAdapter rememberWordsAdapter;
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
        wordsAdapter = new WordsAdapter(getActivity());
        rememberWordsAdapter = new RememberWordsAdapter(getActivity());
        SharedPreferences prefs = getActivity().getSharedPreferences(MainFragment.PREFER_NAME, 0);
        String searchedWord = prefs.getString("SearchedWord", "");
        Word word = wordsAdapter.getWord(searchedWord);
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
                        .setAction("Action", null)
                        .show();
            }
        });

        deleteFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.exit)
                        .setTitle("Устгах")
                        .setMessage("Энэхүү үгийг устгах уу?")
                        .setPositiveButton("Тийм", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                                        R.style.AppTheme_Dark_Dialog);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage("Устгаж байна...");
                                progressDialog.show();
                                // TODO: Implement your own authentication logic here.
                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                wordsAdapter.deleteWord(new Word(eng, type, mon));
                                                Log.d(TAG, "Амжилттай устгалаа..." + eng);
                                                getActivity().getFragmentManager().popBackStack();
                                                progressDialog.dismiss();
                                            }
                                        }, 1000);
                            }
                        })
                        .setNegativeButton("Үгүй", null)
                        .show();
            }
        });
        wordListFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eng = english.getText().toString();
                String type = wordtype.getText().toString();
                String mon = mongolia.getText().toString();
                Cursor words = rememberWordsAdapter.checkRememberWord(eng);
                if (words == null) {
                    Snackbar.make(v, "Өгөгдлийн сангийн query алдаатай байна...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.d(TAG,"Өгөгдлийн сангийн query алдаатай байна");
                } else {
                    getActivity().startManagingCursor(words);
                    if (words.getCount() > 0) {
                        Snackbar.make(v, "Бүртгэлтэй үг байна...", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Log.d(TAG,"Бүртгэлтэй үг байна");
                        getActivity().stopManagingCursor(words);
                        words.close();
                    } else {
                        rememberWordsAdapter.addRememberWord(new RememberWord(eng, type, mon));
                        Toast.makeText(getActivity(), "Амжилттай нэмлээ", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Амжилттай нэмлээ");
                        Snackbar.make(v, "Цээжлэх үгийн жагсаалтанд амжилттай нэмэгдлээ...", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });
    }
}
