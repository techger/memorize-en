package programmer.laboratore_6;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class WordLookFragment extends Fragment {

    private static final String TAG = "===WordLookFragment===";

    public static View rootView;
    public TextView english;
    public TextView mongolia;
    MainFragment mainFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_word_look, container, false);
        return rootView;
    }
    public void init(){
        english = (TextView)rootView.findViewById(R.id.englishText);
        mongolia= (TextView)rootView.findViewById(R.id.mongolianText);
    }

}
