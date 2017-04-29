package ourwallet.example.com.ourwallet;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Button lock;
    private ImageView profile_image;
    private TextView name,email;
    Fragment fragment = null;
    private Cursor cursor ;
    private String phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        SharedPreferences   details=getSharedPreferences("User_details", Context.MODE_PRIVATE);
        Constants.name=details.getString("name","no name");
        Constants.email=details.getString("email","email");
        Constants.adress=details.getString("wallet_address","0");
        Constants.balance=details.getString("balance","balance");
        Constants.image=details.getString("image","image");
        Constants.image =  Constants.image.replace("\\/\\/", "//");
        Constants.image =  Constants.image.replace("\\/", "//");
        details=getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=details.edit();
        editor.putInt("value",1);
        editor.commit();
        Log.e("Image",Constants.image);
        Toolbar toolbar =  (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        lock=(Button)findViewById(R.id.lock);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        profile_image=(ImageView)hView.findViewById(R.id.imageView);
        name=(TextView)hView.findViewById(R.id.name);
        email=(TextView)hView.findViewById(R.id.email);
        name.setText(Constants.name);
        email.setText(Constants.email);
        Picasso.with(getApplicationContext()).
                load(Constants.image).resize(80,80).
                placeholder(R.drawable.default_avatar).into(profile_image);
        lock.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(Home.this, "lock" , Toast.LENGTH_SHORT).show();
        }
    });
        fragment = new User_detailsfragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
        GetContactsIntoArrayList();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void GetContactsIntoArrayList(){

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
      //  name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        while (cursor.moveToNext()) {

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.e("NAmes",phonenumber);
          //  StoreContacts.add(name + " "  + ":" + " " + phonenumber);
        }

        cursor.close();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
         if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment = new User_detailsfragment();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                // Handle the camera action
            }
        }
            return true;

    }
}
