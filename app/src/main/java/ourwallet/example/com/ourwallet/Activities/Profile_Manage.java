package ourwallet.example.com.ourwallet.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ourwallet.example.com.ourwallet.Constants;
import ourwallet.example.com.ourwallet.Home_Fragments.User_detailsfragment;
import ourwallet.example.com.ourwallet.Profile_fragments.Contact_info;
import ourwallet.example.com.ourwallet.Profile_fragments.Kyc_info;
import ourwallet.example.com.ourwallet.Profile_fragments.Login_info;
import ourwallet.example.com.ourwallet.R;
import ourwallet.example.com.ourwallet.ViewPager.ViewPagerAdapter;

public class Profile_Manage extends AppCompatActivity implements com.android.volley.Response.Listener<JSONObject>,
        com.android.volley.Response.ErrorListener,View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int validate=0;
    private ImageView edit_image;
    User_detailsfragment afterMarketFragment;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_manage);
        viewPager = (ViewPager) findViewById(R.id.viewpager_parts_list);
        tabLayout = (TabLayout) findViewById(R.id.tabs_parts_list);
        edit_image=(ImageView)findViewById(R.id.add_contact);

        SetToolbar();
        afterMarketFragment = new User_detailsfragment();


        verify_address();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(Profile_Manage.this,R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setMessage("Please Wait..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Contact_info(), "Contact Info");
        adapter.addFragment(new Kyc_info(), "KYC Info");
        adapter.addFragment(new Login_info(), "Login Info");


        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                validate = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void SetToolbar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        TextView homeTitle = (TextView) findViewById(R.id.title_toolbar);
        homeTitle.setText("Profile");
        edit_image.setVisibility(View.VISIBLE);
        edit_image.setOnClickListener(this);

        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void verify_address() {
        showProgressDialog();
        try {
            JSONObject json = new JSONObject();
            json.put("user_id", Constants.user_id);
            json.put("token", Constants.token);
            JSONObject json2 = new JSONObject();
            json2.put("to", "orupartners");
            json2.put("methods", "get_verification_details");
            json2.accumulate("complex", json);
            String url = "http://propiran.com/cp/redirect_to.php";
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, json2, this, this) {

            };
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(getApplication()).addToRequestQueue(jsObjRequest);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("erresponse", error.toString());
        mProgressDialog.dismiss();
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e("response", response.toString());
        try {
            Constants.varification_email = response.getString("varification_email");
            Constants.verification_mobile = response.getString("verification_mobile");
            Constants.verification_address = response.getString("verification_mobile");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mProgressDialog.dismiss();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.add_contact:
            Constants.fragments_values=validate;
            if(Constants.fragments_values==0){
               Contact_info fragment = new  Contact_info();
                (( Contact_info)fragment).full_name.setEnabled(true);
                (( Contact_info)fragment).email.setEnabled(true);
                (( Contact_info)fragment).number.setEnabled(true);
                (( Contact_info)fragment).country.setEnabled(true);
                (( Contact_info)fragment).city.setEnabled(true);
                (( Contact_info)fragment).address1.setEnabled(true);
                (( Contact_info)fragment).address2.setEnabled(true);
                (( Contact_info)fragment).change.setVisibility(View.VISIBLE);


            }
            if(Constants.fragments_values==1){
                Kyc_info fragment = new Kyc_info();
                ((Kyc_info)fragment).mobile_verify.setEnabled(true);
                ((Kyc_info)fragment).email_verify.setEnabled(true);
                ((Kyc_info)fragment).email_verify.setEnabled(true);
            }
            if(Constants.fragments_values==2){
                Login_info fragment = new Login_info();
                ((Login_info)fragment).new_layout.setVisibility(View.VISIBLE);
                ((Login_info)fragment).c_layout.setVisibility(View.VISIBLE);
                ((Login_info)fragment).change.setVisibility(View.VISIBLE);

            }
            Toast.makeText(this, "click"+ Constants.fragments_values, Toast.LENGTH_SHORT).show();

    }
    }
}