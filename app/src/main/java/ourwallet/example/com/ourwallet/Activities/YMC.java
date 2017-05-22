package ourwallet.example.com.ourwallet.Activities;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import ourwallet.example.com.ourwallet.R;
import ourwallet.example.com.ourwallet.ViewPager.ViewPagerAdapter;
import ourwallet.example.com.ourwallet.YMC_fragments.Address_verification_fragment;
import ourwallet.example.com.ourwallet.YMC_fragments.Email_verification_fragment;
import ourwallet.example.com.ourwallet.YMC_fragments.Photo_verification_fragment;

public class YMC extends AppCompatActivity implements com.android.volley.Response.Listener<JSONObject>,
        com.android.volley.Response.ErrorListener,View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    User_detailsfragment afterMarketFragment;
    public  static final int RequestPermissionCode  = 1 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_manage);
        EnableRuntimePermission();
        viewPager = (ViewPager) findViewById(R.id.viewpager_parts_list);
        tabLayout = (TabLayout) findViewById(R.id.tabs_parts_list);
        SetToolbar();
        afterMarketFragment = new User_detailsfragment();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }
    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                YMC.this,
                Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(YMC.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(YMC.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Email_verification_fragment(), "Email Verification");
        adapter.addFragment(new Photo_verification_fragment(), "Photo Verification");
        adapter.addFragment(new Address_verification_fragment(), "Address Verification");


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
        homeTitle.setText("KYC");


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

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onClick(View view) {

    }
}