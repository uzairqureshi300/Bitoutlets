package ourwallet.example.com.ourwallet.Profile_fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ourwallet.example.com.ourwallet.Activities.MySingleton;
import ourwallet.example.com.ourwallet.Constants;
import ourwallet.example.com.ourwallet.R;

public class Login_info extends Fragment implements com.android.volley.Response.Listener<JSONObject>,
        com.android.volley.Response.ErrorListener,View.OnClickListener {
  public static EditText name, password, status,new_password,confirm_password;
    public static View c_layout,snack,new_layout;
   public static Button change;
    private ProgressDialog mProgressDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.login_info, container, false);
        c_layout=(View)v.findViewById(R.id.c_pass_layout);
        new_layout=(View)v.findViewById(R.id.new_pass_layout);
        snack=(View)v.findViewById(R.id.snack_login_info);
        name = (EditText) v.findViewById(R.id.user_name);
        password = (EditText) v.findViewById(R.id.password);
        change=(Button)v.findViewById(R.id.change_password);
        status = (EditText) v.findViewById(R.id.status);
        new_password=(EditText) v.findViewById(R.id.new_password);
        confirm_password=(EditText) v.findViewById(R.id.c_password);
        setData();
        set_Listeners();
        return v;
    }
    private void set_Listeners() {
        change.setOnClickListener(this);
    }
    private void setData() {
        name.setText(Constants.username);
        password.setText(Constants.password);
        if (Constants.status.equals("1")) {
            status.setText("Active");
        } else {
            status.setText("Not Active");

        }
    }
    private void update_Info() {
        showProgressDialog();
        try {
            JSONObject json = new JSONObject();
            json.put("user_id", Constants.user_id);
            json.put("token", Constants.token);
            json.put("password", new_password.getText().toString());
            json.put("confirm_password", confirm_password.getText().toString());
            json.put("old_password", password.getText().toString());

            JSONObject json2 = new JSONObject();
            json2.put("to", "orupartners");
            json2.put("methods", "update_password");
            json2.accumulate("complex", json);
            String url = "http://orupartners.com/cp/redirect_to.php";
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
    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setMessage("Please Wait..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_password:
                update_Info();
        }
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
            if(error.equals("0")){
                Snackbar.make(snack,"Successfully Updated",Snackbar.LENGTH_SHORT).show();
                Disable_View();
            }
        }catch(JSONException ex){

        }
    }
    private void Disable_View() {
        name.setEnabled(false);
        c_layout.setVisibility(View.GONE);
        new_layout.setVisibility(View.GONE);
        change.setVisibility(View.GONE);
    }
}
