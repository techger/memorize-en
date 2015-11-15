package programmer.laboratore_6;
import android.app.Activity;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.TimeoutException;
import programmer.laboratore_6.Database.MyDbHandler;
import programmer.laboratore_6.Model.Word;

public class WordAddFragment extends Fragment {

    private static final String TAG = "===AddWordFragment===";
    private static View rootView;
    TextView englishWordInput;
    TextView wordTypeInput;
    TextView mongolianWordInput;
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
        rootView = inflater.inflate(R.layout.fragment_word_add, container, false);
        init();
        return rootView;
    }
    public void init(){
        alert = new AlertDialogManager();
        myDbHandler = new MyDbHandler(getActivity());
        englishWordInput = (TextView)rootView.findViewById(R.id.englishWordInput);
        wordTypeInput = (TextView)rootView.findViewById(R.id.wordTypeInput);
        mongolianWordInput = (TextView)rootView.findViewById(R.id.mongolianWordInput);
        saveButton = (FloatingActionButton)rootView.findViewById(R.id.newWordSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eng = englishWordInput.getText().toString();
                String type = wordTypeInput.getText().toString();
                String mon = mongolianWordInput.getText().toString();
                Cursor words = myDbHandler.checkWord(eng);
                if (words == null) {
                    alert.showAlertDialog(getActivity(), "Error", "Database query error", false);
                    Log.d(TAG,"Өгөгдлийн сангийн query алдаатай байна");
                } else {
                    getActivity().startManagingCursor(words);
                    if (words.getCount() > 0) {
                        alert.showAlertDialog(getActivity(), "Алдаа", "Бүртгэлтэй үг байна", false);
                        Log.d(TAG,"Бүртгэлтэй үг байна");
                        getActivity().stopManagingCursor(words);
                        words.close();
                    } else {
                        myDbHandler.addWord(new Word(eng, type, mon));
                        Toast.makeText(getActivity(), "Амжилттай нэмлээ", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Амжилттай нэмлээ");
                        Snackbar.make(v, "Шинэ үг амжилттай нэмэгдлээ...", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });
    }
}
