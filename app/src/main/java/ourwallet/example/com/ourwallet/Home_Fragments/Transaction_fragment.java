package ourwallet.example.com.ourwallet.Home_Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import ourwallet.example.com.ourwallet.Activities.Add_EPin;
import ourwallet.example.com.ourwallet.Activities.MySingleton;
import ourwallet.example.com.ourwallet.Constants;
import ourwallet.example.com.ourwallet.R;


public class Transaction_fragment extends Fragment implements com.android.volley.Response.Listener<JSONObject>,
        com.android.volley.Response.ErrorListener, View.OnClickListener{
    private EditText old_password,new_password,confirm_password;
    private Button save;
    private List<String> generated_list = new ArrayList<String>();
    private ProgressDialog mProgressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v =  inflater.inflate(R.layout.transaction_change_fragment, container, false);
        old_password=(EditText)v.findViewById(R.id.input_old_password);
        new_password=(EditText)v.findViewById(R.id.input_new_password);
        confirm_password=(EditText)v.findViewById(R.id.input_cnfrm_password);
        save=(Button)v.findViewById(R.id.save);
        save.setOnClickListener(this);
        return v;
    }
    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setMessage("Please Wait..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
    private void transaction_password() {
        showProgressDialog();
        try {
            JSONObject json = new JSONObject();
            json.put("user_id", Constants.user_id);
            json.put("token", Constants.token);
            json.put("password", new_password.getText().toString());
            json.put("confirm", confirm_password.getText().toString());
            json.put("old_password", old_password.getText().toString());
            JSONObject json2 = new JSONObject();
            json2.put("to", "orupartners");
            json2.put("methods", "set_transaction_password");
            json2.accumulate("complex", json);
            String url = "http://propiran.com/cp/redirect_to.php";
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, json2, this, this) {

            };
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.save:
                transaction_password();
        }
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("error_response",error.toString());
        mProgressDialog.dismiss();
        }
    @Override
    public void onResponse(JSONObject response) {
        Log.e("response",response.toString());
        mProgressDialog.dismiss();
        try{
            if(response.getString("error").equals("0")){
                Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_SHORT).show();
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
