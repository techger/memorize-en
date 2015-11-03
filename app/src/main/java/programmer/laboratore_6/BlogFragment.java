package programmer.laboratore_6;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;


public class BlogFragment extends Fragment {
    private View rootView;
    Context mContext;
    Boolean isInternetPresent = false;
    AlertDialogManager alert = new AlertDialogManager();
    ConnectionDetector cd;
    String rline = "";
    WebView webView;

    private WebView myBlogWeb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_blog, container, false);

        myBlogWeb = (WebView) rootView.findViewById(R.id.myBlogView);
        WebSettings settings = myBlogWeb.getSettings();
        CookieSyncManager.createInstance(getActivity());
        settings.setJavaScriptEnabled(true);
        myBlogWeb.setScrollBarStyle(myBlogWeb.SCROLLBARS_OUTSIDE_OVERLAY);
        myBlogWeb.setScrollBarStyle(myBlogWeb.SCROLLBARS_OUTSIDE_OVERLAY);
        myBlogWeb.getSettings().setJavaScriptEnabled(true);
        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            myBlogWeb.setWebViewClient(new WebViewClient() {

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.i("WebContent", "Navigating to " + url);
                    view.loadUrl(url);
                    return true;
                }

                public void onPageFinished(WebView view, String url) {
                    Log.i("WebContent", "Finished loading of " + url);
                }

                public void onReceivedError(WebView view, int errorCode, String errorDescription, String errorUrl) {
                    Log.e("Programmer", "ERR AT   -> " + errorUrl);
                    Log.e("WebContent", "ERR CODE -> " + errorCode);
                    Log.e("WebContent", "ERR MSG  -> " + errorDescription);
                    //new Spawner().spawnView(WebContent.this, Offline.class); //TODO: Replace the offline-activity with an server-offline-activity
                }
            });
            myBlogWeb.loadUrl("http://www.turtuvshin.blogspot.com");

        } else {
            alert.showAlertDialog(getActivity(), "Интернэт холболтоо шалгана уу !!!",
                    "Интернэт холболтоо байхгүй байна !!!", false);
        }

        return rootView;
    }
}


class ConnectionDetector {

    private Context _context;

    public ConnectionDetector(Context context){
        this._context = context;
    }

    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }
}