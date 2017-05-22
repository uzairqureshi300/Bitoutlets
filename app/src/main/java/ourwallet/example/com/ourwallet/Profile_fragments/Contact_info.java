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

public class Contact_info extends Fragment implements com.android.volley.Response.Listener<JSONObject>,
        com.android.volley.Response.ErrorListener,View.OnClickListener {

    public static EditText full_name, email, number, country, city, address1, address2;
    public static Button change;
    private View snack_view;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.contact_info, container, false);
        full_name = (EditText) v.findViewById(R.id.full_name);
        email = (EditText) v.findViewById(R.id.email);
        number = (EditText) v.findViewById(R.id.mobile);
        country = (EditText) v.findViewById(R.id.country);
        city = (EditText) v.findViewById(R.id.city);
        address1 = (EditText) v.findViewById(R.id.adress_one);
        address2 = (EditText) v.findViewById(R.id.adress_two);
        change = (Button) v.findViewById(R.id.change);
        snack_view=(View)v.findViewById(R.id.snack_contactinfo);
        setData();
        set_Listeners();

        return v;
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setMessage("Please Wait..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void update_Info() {
        showProgressDialog();
        try {
            JSONObject json = new JSONObject();
            json.put("user_id", Constants.user_id);
            json.put("token", Constants.token);

            json.put("full_name", full_name.getText().toString());
            json.put("email", email.getText().toString());
            json.put("mobile", number.getText().toString());
            json.put("country", country.getText().toString());
            json.put("city", city.getText().toString());
            json.put("address1", address1.getText().toString());
            json.put("address2", address2.getText().toString());
            JSONObject json2 = new JSONObject();
            json2.put("to", "orupartners");
            json2.put("methods", "edit_user");
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

    private void setData() {
        full_name.setText(Constants.full_name);
        email.setText(Constants.email);
        number.setText(Constants.phone_number);
        country.setText(Constants.country);
        city.setText(Constants.city);
        address1.setText(Constants.address1);
        address2.setText(Constants.address2);
    }
    private void Disable_View() {
        full_name.setEnabled(false);
        email.setEnabled(false);
        number.setEnabled(false);
        country.setEnabled(false);
        city.setEnabled(false);
        address1.setEnabled(false);
        address2.setEnabled(false);
        change.setVisibility(View.GONE);
    }
    private void set_Listeners() {
        change.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change:
                update_Info();
        }
    }
    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
    Log.e("response",response.toString());
        try{
            String error=response.getString("error");
               if(error.equals("0")){
                   String msg=response.getString("msg");
                   Snackbar.make(snack_view,msg,Snackbar.LENGTH_SHORT).show();
                    Constants.fragments_values=5;
                   Disable_View();

               }
                mProgressDialog.dismiss();
        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }
}