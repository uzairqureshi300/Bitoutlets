package ourwallet.example.com.ourwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class Overview extends AppCompatActivity implements View.OnClickListener {
  private Button login,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_overview);
        login=(Button)findViewById(R.id.login);
        signup=(Button)findViewById(R.id.sign_up);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
     view.getId();
        switch (view.getId()){
            case R.id.login:
                login.setBackgroundColor(getResources().getColor(R.color.button_cream));
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
        }
    }
}
