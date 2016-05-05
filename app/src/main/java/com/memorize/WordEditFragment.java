package com.memorize;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.memorize.Database.MyDbHandler;
import com.memorize.Model.Word;


public class WordEditFragment extends Fragment {

    private static final String TAG = "===EditWordFragment===";
    private static View rootView;
    EditText englishWordInput;
    EditText wordTypeInput;
    EditText mongolianWordInput;
    FloatingActionButton saveButton;
    MyDbHandler myDbHandler;
    AlertDialogManager alert;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_word_edit, container, false);
        init();
        return rootView;
    }

    public void init(){
        alert = new AlertDialogManager();
        myDbHandler = new MyDbHandler(getActivity());
        englishWordInput = (EditText)rootView.findViewById(R.id.englishWordInputE);
        wordTypeInput = (EditText)rootView.findViewById(R.id.wordTypeInputE);
        mongolianWordInput = (EditText)rootView.findViewById(R.id.mongolianWordInputE);
        saveButton = (FloatingActionButton)rootView.findViewById(R.id.editedWordSave);

        Bundle bundle = getArguments();

        String english = bundle.getString("english","error");
        String wordtype = bundle.getString("type","error");
        String mongolia = bundle.getString("mongolia","error");
        englishWordInput.setText(english);
        wordTypeInput.setText(wordtype);
        mongolianWordInput.setText(mongolia);
        Log.d(TAG, "English: " + english + " Type: " + wordtype + " Mongolia: " + mongolia);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Шинэчилж байна...");
                progressDialog.show();
                // TODO: Implement your own authentication logic here.
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String eng = englishWordInput.getText().toString();
                                String type = wordTypeInput.getText().toString();
                                String mon = mongolianWordInput.getText().toString();
                                myDbHandler.updateWord(new Word(eng, type, mon));
                                Log.d(TAG, "Амжилттай заслаа " + eng + ", " + type + ", " + mon);
                                getActivity().getFragmentManager().popBackStack();
                                Toast.makeText(getActivity(), "Амжилттай заслаа", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }, 1000);
            }
        });
    }
}
