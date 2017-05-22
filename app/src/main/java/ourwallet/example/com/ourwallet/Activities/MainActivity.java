package ourwallet.example.com.ourwallet.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ourwallet.example.com.ourwallet.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,com.android.volley.Response.Listener<JSONObject>, com.android.volley.Response.ErrorListener  {
    private Button login;
    private EditText email,password;
    private ProgressDialog mProgressDialog;
    private View snack_bar;
    private SharedPreferences details;
    public  static final int RequestPermissionCode  = 1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EnableRuntimePermission();
        login=(Button)findViewById(R.id.login);
        email=(EditText)findViewById(R.id.input_email);
        password=(EditText)findViewById(R.id.input_password);
        snack_bar=(View)findViewById(R.id.snackbar_view);
        login.setOnClickListener(this);
    }
    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                MainActivity.this,
                Manifest.permission.READ_CONTACTS))
        {
            Toast.makeText(MainActivity.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }
    @Override
    public void onClick(View view) {
        view.getId();
        switch (view.getId()){
            case R.id.login:
                Validation();

        }
    }

private void Validation(){
    if(email.getText().toString().equals("") || password.getText().toString().equals("")){
        Snackbar.make(snack_bar,"enter  username or password",Snackbar.LENGTH_LONG).show();
    }
    else{
        try {
            Login();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


        private void Login() throws JSONException {
          showProgressDialog();
            JSONObject json = new JSONObject();
            json.put("username", email.getText().toString());
            json.put("password", password.getText().toString());

            JSONObject json2 = new JSONObject();
            json2.put("to","orupartners");
            json2.put("methods","log_in");
            json2.accumulate("complex",json);
            String url = "http://orupartners.com/cp/redirect_to.php";
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,json2, this ,this){

            };
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
        }
    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(MainActivity.this,R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setMessage("Please Wait..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e("RESPONSE",response.toString());
        try {



            String error = response.getString("error");
            if(error.equals("0")){
                String token=response.getString("token");
                JSONObject getdetails=response.getJSONObject("user_details");
                Log.e("data",getdetails.toString());
                details=getSharedPreferences("User_details", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=details.edit();
                edit.putString("name",getdetails.getString("full_name"));
                edit.putString("email",getdetails.getString("email"));
                edit.putString("balance",getdetails.getString("balance"));
                edit.putString("image",getdetails.getString("image"));
                edit.putString("token",token);
                edit.putString("wallet_address",getdetails.getString("wallet_address"));
                edit.putString("user_id",getdetails.getString("user_id"));
                edit.putString("mobile_verified",getdetails.getString("mobile_verified"));
                edit.putString("email_verified",getdetails.getString("email_verified"));
                edit.putString("address_verified",getdetails.getString("address_verified"));
                edit.putString("status",getdetails.getString("status"));
                edit.putString("password",getdetails.getString("password"));
                edit.putString("full_name",getdetails.getString("full_name"));
                edit.putString("country",getdetails.getString("country"));
                edit.putString("city",getdetails.getString("city"));
                edit.putString("username",getdetails.getString("username"));
                edit.putString("phone",getdetails.getString("phone"));
                edit.putString("address",getdetails.getString("address"));
                edit.putString("address2",getdetails.getString("address2"));


                edit.commit();
                Intent i=new Intent(getApplicationContext(),Home.class);
                startActivity(i);
                finish();
            }
            else{
                Snackbar.make(snack_bar,"Invalid Credentials",Snackbar.LENGTH_LONG).show();
            }
            mProgressDialog.dismiss();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                            Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
                }
                break;
        }
    }
}
