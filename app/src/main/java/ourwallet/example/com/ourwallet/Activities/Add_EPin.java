package ourwallet.example.com.ourwallet.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ourwallet.example.com.ourwallet.Constants;
import ourwallet.example.com.ourwallet.R;
import ourwallet.example.com.ourwallet.RecyclerViewAdapters.Contacts_recyclerView;
import ourwallet.example.com.ourwallet.RecyclerViewAdapters.EpinsGenerate_recyclerView;
import ourwallet.example.com.ourwallet.RecyclerViewAdapters.SimpleDividerItemDecoration;

public class Add_EPin extends AppCompatActivity implements com.android.volley.Response.Listener<JSONObject>,
        com.android.volley.Response.ErrorListener, View.OnClickListener {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private EditText epn_number, transaction_password, price;
    private View snack_view;
    private String api_call = "No";
    private RecyclerView epin_list;
    private EpinsGenerate_recyclerView epinsGenerate_recyclerView;
    private TextInputLayout translayout;
    private List<String> generated_list = new ArrayList<String>();
    private ProgressDialog mProgressDialog;
    private Button generate_epin, save_epin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pins);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.title_toolbar);
        epin_list = (RecyclerView) findViewById(R.id.epin_list);
        generate_epin = (Button) findViewById(R.id.generate_key);
        transaction_password = (EditText) findViewById(R.id.transaction_passwrod);
        price = (EditText) findViewById(R.id.price);
        save_epin = (Button) findViewById(R.id.add_key);
        snack_view = (View) findViewById(R.id.epin_views);
        epn_number = (EditText) findViewById(R.id.number_pins);
        translayout = (TextInputLayout) findViewById(R.id.trans_layout);
        translayout.setVisibility(View.GONE);
        setToolbar();
        Click_Listeners();
    }
    private void Click_Listeners() {
        generate_epin.setOnClickListener(this);
        save_epin.setOnClickListener(this);
    }
    private void setToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);
        toolbar_title.setText("Add Epin");

    }
    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(Add_EPin.this, R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setMessage("Please Wait..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
    private void save() {
        showProgressDialog();
        try {
            JSONObject json = new JSONObject();
            json.put("user_id", Constants.user_id);
            json.put("token", Constants.token);
            json.put("transaction_password", transaction_password.getText().toString());
            //      json.put("epin", Arrays.toString(k));Jicstech54321
            JSONArray pins = new JSONArray();
            JSONObject cartItemsObjedct;
            for (int i = 0; i < generated_list.size(); i++) {
                cartItemsObjedct = new JSONObject();
                cartItemsObjedct.putOpt("epins", generated_list.get(i));
                pins.put(cartItemsObjedct);
            }

            json.put("epin", pins);
            JSONObject json2 = new JSONObject();
            json2.put("to", "orupartners");
            json2.put("methods", "save_epin");
            json2.accumulate("complex", json);
            String url = "http://propiran.com/cp/redirect_to.php";
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, json2, this, this) {

            };
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }
    private void Generate_Keys() {
        showProgressDialog();
        try {
            JSONObject json = new JSONObject();
            json.put("user_id", Constants.user_id);
            json.put("token", Constants.token);
            json.put("number", epn_number.getText().toString());
            json.put("price", price.getText().toString());

            JSONObject json2 = new JSONObject();
            json2.put("to", "orupartners");
            json2.put("methods", "generate_epins");
            json2.accumulate("complex", json);
            String url = "http://propiran.com/cp/redirect_to.php";
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, json2, this, this) {

            };
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.generate_key:
                api_call = "generate";
                if (epn_number.getText().toString().equals("") || price.getText().toString().equals("")) {
                    Snackbar.make(snack_view, "Enter respective fields", Snackbar.LENGTH_SHORT).show();

                } else {
                    Generate_Keys();
                }
                break;
            case R.id.add_key:
                api_call = "add";
                if (generated_list.isEmpty()) {
                    Snackbar.make(snack_view, "Genrate Keys First", Snackbar.LENGTH_SHORT).show();
                } else {
                    save();
                }
                break;
        }
    }
    @Override
    public void onErrorResponse(VolleyError error) {

    }
    @Override
    public void onResponse(JSONObject response) {
        Log.e("response", response.toString());
        Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
        try {
            switch (api_call) {
                case "generate":
                    mProgressDialog.dismiss();
                    JSONArray array = response.optJSONArray("keys");
                    for (int i = 0; i < array.length(); i++) {
                        generated_list.add(array.optString(i));
                        Log.e("List", generated_list.get(i));
                    }
                    translayout.setVisibility(View.VISIBLE);
                    epin_list.setVisibility(View.VISIBLE);
                    Constants.constrtuctor_value=1;
                    epinsGenerate_recyclerView = new EpinsGenerate_recyclerView(this, generated_list);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    epin_list.addItemDecoration(new SimpleDividerItemDecoration(this));
                    epin_list.setLayoutManager(mLayoutManager);
                    epin_list.setAdapter(epinsGenerate_recyclerView);
                    break;
                case "add":
                    mProgressDialog.dismiss();
                    String error=response.getString("error");
                    if(error.equals("0")){
                        Snackbar.make(snack_view,"Epins saved successfully",Snackbar.LENGTH_SHORT).show();
                        translayout.setVisibility(View.GONE);
                    }
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
