package programmer.laboratore_6;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import programmer.laboratore_6.Database.MyDbHandler;
import programmer.laboratore_6.Model.RememberWord;

public class LockScreen extends AppCompatActivity{

    MyDbHandler myDbHandler;
    private TextView words;
    private Button exit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        myDbHandler = new MyDbHandler(this);
        words = (TextView)findViewById(R.id.lockScreenWord);
        exit = (Button) findViewById(R.id.lockerExit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {

            Random random = new Random();
            List<String> wordList = getLockerRememberWords();
            int nextInt = random.nextInt(wordList.size());
            words.setText(wordList.get(nextInt));
            Toast.makeText(context, "Word: "+wordList.get(nextInt), Toast.LENGTH_LONG).show();
            String action = intent.getAction();
            if(action.equals("android.intent.action.SCREEN_OFF")){
            }
        }
    };

    public List<String> getLockerRememberWords(){
        List<String> wordList = new ArrayList<String>();
        List<RememberWord> rememberWords = myDbHandler.getAllRememberWords();
        for (RememberWord rememberWord : rememberWords){
            String listWord = ""+rememberWord.getRememberEnglish() +
                    " - "+rememberWord.getRememberType()+" "+rememberWord.getRememberMongolia();
            wordList.add(listWord);
        }
        words.setText(wordList.toString());
        return wordList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
