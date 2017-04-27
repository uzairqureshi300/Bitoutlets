package ourwallet.example.com.ourwallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,com.android.volley.Response.Listener<JSONObject>, com.android.volley.Response.ErrorListener  {
    private Button login;
    private EditText email,password;
    private ProgressDialog mProgressDialog;
    private View snack_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=(Button)findViewById(R.id.login);
        email=(EditText)findViewById(R.id.input_email);
        password=(EditText)findViewById(R.id.input_password);
        snack_bar=(View)findViewById(R.id.snackbar_view);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        view.getId();
        switch (view.getId()){
            case R.id.login:
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
}
