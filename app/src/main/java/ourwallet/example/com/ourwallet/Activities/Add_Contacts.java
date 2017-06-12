package ourwallet.example.com.ourwallet.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ourwallet.example.com.ourwallet.Constants;
import ourwallet.example.com.ourwallet.Database.AndroidOpenDbHelper;
import ourwallet.example.com.ourwallet.Models.Contacts_Model;
import ourwallet.example.com.ourwallet.R;

public class Add_Contacts extends AppCompatActivity implements View.OnClickListener ,com.android.volley.Response.Listener<JSONObject>, com.android.volley.Response.ErrorListener {
    private EditText F_name, L_name, Phone, Email, Fax, Address, Portal, Wallet_Address, Created_by,Grade;
    private Button add_contact;
    private List<Contacts_Model> contacts_list=new ArrayList<Contacts_Model>();
    private Contacts_Model contacts_model=new Contacts_Model();
    private ProgressDialog mProgressDialog;
    private View snackbar;
    private Toolbar toolbar;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

          setContentView(R.layout.activity_add_contacts);
        F_name=(EditText)findViewById(R.id.input_firstname);
        L_name=(EditText)findViewById(R.id.input_last_name);
        Phone=(EditText)findViewById(R.id.input_phone);
        snackbar=(View)findViewById(R.id.snackbar_view);
        Email=(EditText)findViewById(R.id.input_email);
        Fax=(EditText)findViewById(R.id.input_fax);
        Address=(EditText)findViewById(R.id.input_address);
        Portal=(EditText)findViewById(R.id.input_portal);
        Wallet_Address=(EditText)findViewById(R.id.input_wallet_address);
        Created_by=(EditText)findViewById(R.id.input_created_by);
        Grade=(EditText)findViewById(R.id.input_grade);
        add_contact=(Button)findViewById(R.id.ad_contact);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar_title=(TextView)findViewById(R.id.title_toolbar);
        add_contact.setOnClickListener(this);
        Created_by.setText(Constants.name);
       Created_by.setEnabled(false);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);
        toolbar_title.setText("Add Contact");

    }
    @Override
    public void onClick(View view) {
        view.getId();
        switch (view.getId()){
            case R.id.ad_contact:
                validation();

        }
    }
    private void validation(){

   if(F_name.getText().toString().equals("")){
       F_name.setError("this is error");
       F_name.requestFocus();
   }
   if(L_name.getText().toString().equals("")){

       L_name.setError("this is error");
       L_name.requestFocus();

          }
    if(Phone.getText().equals("")){

        Phone.setError("this is error");
        Phone.requestFocus();

        }
    if(!validateemail(Email.getText().toString())){

        Email.setError("Invalid Email");
        Email.requestFocus();
    }
    if(Fax.getText().toString().equals("")){
        Fax.setError("Invalid Fax");
        Fax.requestFocus();
    }
    if(Address.getText().toString().equals("")){
            Address.setError("Invalid Fax");
            Address.requestFocus();
        }
     if(Grade.getText().toString().equals("")){
            Grade.setError("Invalid Fax");
            Grade.requestFocus();
        }



        //||L_name.getText().toString().equals("")||Phone.getText().toString().equals("")
//            ||Email.getText().toString().equals("")||Fax.getText().toString().equals("")||Address.getText().toString().equals("")
//            ||Grade.getText().toString().equals("")){
//        Snackbar.make(snackbar,"Fill empty Field",Snackbar.LENGTH_SHORT).show();
//    }

        else{
        try {
            Add_Contact();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    }
    protected boolean validateemail(String email){

        String emailpattern="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern=Pattern.compile(emailpattern);
        Matcher matcher=pattern.matcher(email);
        return matcher.matches();



    }

    private void Add_Contact() throws JSONException {
        showProgressDialog();
        JSONObject json = new JSONObject();
        json.put("first_name", F_name.getText().toString());
        json.put("last_name", L_name.getText().toString());
        json.put("phone", Phone.getText().toString());
        json.put("email", Email.getText().toString());
        json.put("fax", Fax.getText().toString());
        json.put("address", Address.getText().toString());
        json.put("user_id", Constants.user_id);
        json.put("token", Constants.token);
        json.put("created_by", Constants.name);
        json.put("wallet_address",Wallet_Address.getText().toString());
        json.put("grade", Constants.name);
        Log.e("USER", Constants.user_id);
        JSONObject json2 = new JSONObject();
        json2.put("to","orupartners");
        json2.put("methods","add_contact");
        json2.accumulate("complex",json);
        String url = "http://propiran.com/cp/redirect_to.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,json2, this ,this){

        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

    }
    private void Add_Contact_to_List(){
           contacts_model.setFirst_name(F_name.getText().toString());
           contacts_model.setLast_name(L_name.getText().toString());
            contacts_model.setPhone(Phone.getText().toString());
            contacts_model.setEmail(Email.getText().toString());
            contacts_model.setFax(Fax.getText().toString());
            contacts_model.setAddress(Address.getText().toString());
            contacts_model.setPortal(Portal.getText().toString());
            contacts_model.setWallet_address(Wallet_Address.getText().toString());
            contacts_model.setCreated_by(Created_by.getText().toString());
            contacts_model.setGrade(Grade.getText().toString());

        contacts_list.add(contacts_model);

        insert_Contacts(contacts_model);
 }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(Add_Contacts.this,R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setMessage("Please Wait..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mProgressDialog.dismiss();
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e("contacts_response",response.toString());
        mProgressDialog.dismiss();
        try{
            String error=response.getString("error");
            String msg=response.getString("msg");

            if(error.equals("0")){
                Add_Contact_to_List();
                final Dialog alertbox;
                alertbox = new Dialog(Add_Contacts.this);
                alertbox.setContentView(R.layout.custom_dialogbox);
                alertbox.setTitle(msg);
                Button cancel=(Button)alertbox.findViewById(R.id.cancel_dialog);
                Button delete=(Button)alertbox.findViewById(R.id.delete);
                TextView title=(TextView)alertbox.findViewById(R.id.msg);
                title.setText(msg);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertbox.dismiss();
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finish();
                        alertbox.dismiss();
                    }
                });
                alertbox.show();
            }


        }catch(Exception ex){
            ex.printStackTrace();
        }
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
        finish();

        super.onBackPressed();
    }

    public void insert_Contacts(Contacts_Model contacts_model){

        // First we have to open our DbHelper class by creating a new object of that
        AndroidOpenDbHelper androidOpenDbHelperObj = new AndroidOpenDbHelper(this);

        // Then we need to get a writable SQLite database, because we are going to insert some values
        // SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
        SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();

        // ContentValues class is used to store a set of values that the ContentResolver can process.
        ContentValues contentValues = new ContentValues();

        // Get values from the POJO class and passing them to the ContentValues class
        contentValues.put(AndroidOpenDbHelper.Contact_Name, contacts_model.getFirst_name());
        contentValues.put(AndroidOpenDbHelper.Contact_number, contacts_model.getPhone());
        contentValues.put(AndroidOpenDbHelper.Contact_email, contacts_model.getEmail());
        contentValues.put(AndroidOpenDbHelper.Contact_Lname, contacts_model.getLast_name());
        contentValues.put(AndroidOpenDbHelper.Contact_fax, contacts_model.getFax());
        contentValues.put(AndroidOpenDbHelper.Contact_address, contacts_model.getAddress());
        contentValues.put(AndroidOpenDbHelper.Contact_grade, contacts_model.getGrade());



        // Now we can insert the data in to relevant table
        // I am going pass the id value, which is going to change because of our insert method, to a long variable to show in Toast
        long affectedColumnId = sqliteDatabase.insert(AndroidOpenDbHelper.TABLE_NAME_GPA, null, contentValues);

        // It is a good practice to close the database connections after you have done with it
        sqliteDatabase.close();

        // I am not going to do the retrieve part in this post. So this is just a notification for satisfaction ;-)
        Toast.makeText(this, "Values inserted column ID is :" + affectedColumnId, Toast.LENGTH_SHORT).show();

    }

}
