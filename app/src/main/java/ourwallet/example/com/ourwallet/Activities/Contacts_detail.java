package ourwallet.example.com.ourwallet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import ourwallet.example.com.ourwallet.R;

public class Contacts_detail extends AppCompatActivity {
    private TextView f_name, l_name, number, email, fax, address, grade,
    label_firstname,label_lastname,label_number,label_email,label_fax,label_address,label_grade;
    ;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private int value=0;
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
        label_firstname=(TextView)findViewById(R.id.label_firstname);
        label_lastname=(TextView)findViewById(R.id.label_lastname);
        label_number=(TextView)findViewById(R.id.label_number);
        label_email=(TextView)findViewById(R.id.label_email);
        label_fax=(TextView)findViewById(R.id.label_fax);
        label_address=(TextView)findViewById(R.id.label_address);
        label_grade=(TextView)findViewById(R.id.label_grade);

        Intent i=getIntent();
        value=i.getIntExtra("value",0);
        if(value==2) {
            setContact_details();
        }
        else if(value==1){
            setEpin_Details();
        }
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
    private void setEpin_Details() {

        Intent intent = getIntent();
        String get_f_name;
        get_f_name = intent.getStringExtra("pin");
        String get_l_name = intent.getStringExtra("status");
        String get_number = intent.getStringExtra("date");
        String get_email = intent.getStringExtra("amount");
        String get_fax = intent.getStringExtra("used_by");


        f_name.setText(get_f_name);
        l_name.setText(get_l_name);
        number.setText(get_number);
        email.setText(get_email);
        fax.setText(get_fax);

        label_firstname.setText("Pin");
        label_lastname.setText("Status");
        label_number.setText("Date");
        label_email.setText("Amount");
        label_fax.setText("Used_by");
        setToolbar("Epins Detail");
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
        label_firstname.setText("First Name");
        label_lastname.setText("Last Name");
        label_number.setText("Phone Number");
        label_email.setText("Email Address");
        fax.setText("Fax");
        label_address.setText("Address");
        label_grade.setText("Grade");

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
