package programmer.laboratore_6;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    ImageView imageView;
    AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView = (ImageView) findViewById(R.id.imageView2);
        try {
            BitmapDrawable frame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.loading1);
            BitmapDrawable frame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.loading2);
            BitmapDrawable frame3 = (BitmapDrawable) getResources().getDrawable(R.drawable.loading3);
            BitmapDrawable frame4 = (BitmapDrawable) getResources().getDrawable(R.drawable.loading4);
            BitmapDrawable frame5 = (BitmapDrawable) getResources().getDrawable(R.drawable.loading5);
            animationDrawable = new AnimationDrawable();
            animationDrawable.addFrame(frame1, 100);
            animationDrawable.addFrame(frame2, 100);
            animationDrawable.addFrame(frame3, 100);
            animationDrawable.addFrame(frame4, 100);
            animationDrawable.addFrame(frame5, 100);
            animationDrawable.setOneShot(false);
            imageView.setBackgroundDrawable(animationDrawable);
            animationDrawable.start();
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 5 * 1000);*/
            Thread timerThread = new Thread(){
                public void run(){
                    try{
                        sleep(3000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }finally{
                        Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timerThread.start();
        }catch (Exception e){

        }
    }
}
