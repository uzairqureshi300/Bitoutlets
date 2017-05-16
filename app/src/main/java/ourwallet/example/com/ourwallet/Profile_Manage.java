package ourwallet.example.com.ourwallet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ourwallet.example.com.ourwallet.Profile_fragments.Contact_info;
import ourwallet.example.com.ourwallet.Profile_fragments.Kyc_info;
import ourwallet.example.com.ourwallet.Profile_fragments.Login_info;
import ourwallet.example.com.ourwallet.ViewPager.ViewPagerAdapter;

public class Profile_Manage extends AppCompatActivity implements com.android.volley.Response.Listener<JSONObject>,
        com.android.volley.Response.ErrorListener,View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    User_detailsfragment afterMarketFragment;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_manage);
        viewPager = (ViewPager) findViewById(R.id.viewpager_parts_list);
        tabLayout = (TabLayout) findViewById(R.id.tabs_parts_list);
        SetToolbar();
        afterMarketFragment = new User_detailsfragment();

        verify_address();
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


            JSONObject json2 = new JSONObject();
            json2.put("to", "orupartners");
            json2.put("methods", "get_verification_details");
            json2.accumulate("complex", json);
            String url = "http://orupartners.com/cp/redirect_to.php";
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

    }
}