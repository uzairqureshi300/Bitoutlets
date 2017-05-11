package ourwallet.example.com.ourwallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Contacts_detail extends AppCompatActivity {
    private TextView f_name, l_name, number, email, fax, address, grade;
    private Toolbar toolbar;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contacts_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.title_toolbar);
        f_name = (TextView) findViewById(R.id.f_name);
        l_name = (TextView) findViewById(R.id.l_name);
        number = (TextView) findViewById(R.id.number);
        email = (TextView) findViewById(R.id.email);
        fax = (TextView) findViewById(R.id.fax);
        address = (TextView) findViewById(R.id.adress);
        grade = (TextView) findViewById(R.id.grade);
        setContact_details();

    }

    private void setToolbar(String name) {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);
        toolbar_title.setText(name);

    }

    private void setContact_details() {

        Intent intent = getIntent();
        String get_f_name;
        get_f_name = intent.getStringExtra("f_name");
        String get_l_name = intent.getStringExtra("l_name");
        String get_number = intent.getStringExtra("number");
        String get_email = intent.getStringExtra("email");
        String get_fax = intent.getStringExtra("fax");
        String get_address = intent.getStringExtra("address");
        String get_grade = intent.getStringExtra("grade");

        f_name.setText(get_f_name);
        l_name.setText(get_l_name);
        number.setText(get_number);
        email.setText(get_email);
        fax.setText(get_fax);
        address.setText(get_address);
        grade.setText(get_grade);
        setToolbar(get_f_name+" "+get_l_name);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
