package ourwallet.example.com.ourwallet.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.List;

import ourwallet.example.com.ourwallet.Constants;
import ourwallet.example.com.ourwallet.Models.Epins_Model;
import ourwallet.example.com.ourwallet.R;
import ourwallet.example.com.ourwallet.RecyclerViewAdapters.EpinsGenerate_recyclerView;
import ourwallet.example.com.ourwallet.RecyclerViewAdapters.RecyclerItemClickListener;
import ourwallet.example.com.ourwallet.RecyclerViewAdapters.SimpleDividerItemDecoration;

public class Epins extends AppCompatActivity implements com.android.volley.Response.Listener<JSONObject>,
        com.android.volley.Response.ErrorListener, View.OnClickListener,AdapterView.OnItemClickListener {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ProgressDialog mProgressDialog;
    private ImageView btn_addepin;
    private List<Epins_Model> epin_arraylist=new ArrayList<Epins_Model>();
    private RecyclerView epin_list;
    private EpinsGenerate_recyclerView epinsGenerate_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epins_list);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar_title=(TextView)findViewById(R.id.title_toolbar);
        btn_addepin=(ImageView) findViewById(R.id.add_contact);
        epin_list=(RecyclerView)findViewById(R.id.epinget_list);
            List();
        setToolbar();

    }
    private void setToolbar(){

        toolbar_title.setText("Epins");
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);
        btn_addepin.setOnClickListener(this);
        btn_addepin.setVisibility(View.VISIBLE);
    }
    private void List() {
        showProgressDialog();
        try {


            JSONObject json = new JSONObject();
            json.put("user_id", Constants.user_id);
            json.put("token", Constants.token);


            JSONObject json2 = new JSONObject();
            json2.put("to", "orupartners");
            json2.put("methods", "get_pins");
            json2.accumulate("complex", json);
            String url = "http://orupartners.com/cp/redirect_to.php";
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, json2, this, this) {

            };
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }
    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(Epins.this,R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setMessage("Please Wait..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.add_contact:
            Intent i=new Intent(getApplication(),Add_EPin.class);
            startActivity(i);
    }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e("response",response.toString());
        mProgressDialog.dismiss();
        try{
            String error=response.getString("error");
            JSONArray array = response.optJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                Epins_Model epins_model=new Epins_Model();
                epins_model.setEpin_id(jsonObject.getString("id"));
                epins_model.setPins(jsonObject.getString("pins"));
                epins_model.setStatus(jsonObject.getString("status"));
                epins_model.setDateadded(jsonObject.getString("dateadded"));
                epins_model.setAmount(jsonObject.getString("amount"));
                epins_model.setUsed_by(jsonObject.getString("used_by"));
                epins_model.setUser_id(jsonObject.getString("user_id"));
               epin_arraylist.add(epins_model);

            }
            Log.e("size",epin_arraylist.size()+"");
            Constants.constrtuctor_value=2;
            epinsGenerate_recyclerView = new EpinsGenerate_recyclerView(epin_arraylist);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            epin_list.addItemDecoration(new SimpleDividerItemDecoration(this));
            epin_list.setLayoutManager(mLayoutManager);
            epin_list.setAdapter(epinsGenerate_recyclerView);
            ListClick_listener(epin_arraylist);


        }catch (JSONException ex){

        }
    }
    private void ListClick_listener(final List<Epins_Model> list){
        epin_list.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        Intent intent=new Intent(getApplicationContext(),Contacts_detail.class);
                        intent.putExtra("value",1);
                        intent.putExtra("pin",list.get(i).getPins());
                        intent.putExtra("status",list.get(i).getStatus());
                        intent.putExtra("date",list.get(i).getDateadded());
                        intent.putExtra("amount",list.get(i).getAmount());
                        intent.putExtra("used_by",list.get(i).getUsed_by());
                        startActivity(intent);
                    }
                })
        );

    }

}
