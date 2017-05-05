package ourwallet.example.com.ourwallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
ImageView splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        splash=(ImageView)findViewById(R.id.splash);
        SharedPreferences details=getSharedPreferences("Login", Context.MODE_PRIVATE);
        int login_value=details.getInt("value",0);
        if(login_value==1){
            Intent i=new Intent(Splash.this,Home.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();

        }
        if(login_value==0){
            Animation animation1 =
                    AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.blink);
            splash.startAnimation(animation1);
            Thread timer = new Thread()
            {
                public void run()
                {
                    try
                    {
                        sleep(3000);
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        Intent i=new Intent(Splash.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            };
            timer.start();

        }

    }
}
