package ourwallet.example.com.ourwallet.YMC_fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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

import ourwallet.example.com.ourwallet.Constants;
import ourwallet.example.com.ourwallet.MySingleton;
import ourwallet.example.com.ourwallet.R;

public class Email_verification_fragment extends Fragment implements com.android.volley.Response.Listener<JSONObject>,
        com.android.volley.Response.ErrorListener,View.OnClickListener {
    private EditText email_verify_text;
    private String type = "sent mail";
    private View snack_view;
    private ProgressDialog mProgressDialog;

    private Button btn_verify_email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.email_verify_fragment, container, false);
        email_verify_text = (EditText) v.findViewById(R.id.input_verify_email);
        btn_verify_email = (Button) v.findViewById(R.id.btn_verify_email);
        snack_view = (View) v.findViewById(R.id.snack_view_email);

        Click_Listeners();
        return v;
    }


    private void Click_Listeners() {
        btn_verify_email.setOnClickListener(this);
    }

    private void Verify_Email() throws JSONException {
        showProgressDialog();
        JSONObject json = new JSONObject();
        json.put("email", email_verify_text.getText().toString());
        json.put("user_id", Constants.user_id);
        JSONObject json2 = new JSONObject();
        json2.put("to", "orupartners");
        json2.put("methods", "email_verified");
        json2.accumulate("complex", json);
        String url = "http://orupartners.com/cp/redirect_to.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, json2, this, this) {

        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }

    private void verify(String code) throws JSONException {
        showProgressDialog();
        Log.e("code", code);
        JSONObject json = new JSONObject();
        json.put("user_id", Constants.user_id);
        json.put("token", Constants.token);
        json.put("code", code);


        JSONObject json2 = new JSONObject();
        json2.put("to", "orupartners");
        json2.put("methods", "email_verification");
        json2.accumulate("complex", json);
        String url = "http://orupartners.com/cp/redirect_to.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, json2, this, this) {

        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mProgressDialog.dismiss();
        Snackbar.make(snack_view, "Slow internet connectivity", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e("response-verify", response.toString());

        try {
            switch (type) {
                case "sent mail":
                    mProgressDialog.dismiss();
                    type = "compare_code";
                    email_verify_text.setText("");
                    email_verify_text.setHint("Enter Code");
                    btn_verify_email.setText("Enter code");
                    break;
                case "compare_code":
                    mProgressDialog.dismiss();
                    String error = response.getString("error");
                    if (error.equals("0")) {
                        Snackbar.make(snack_view, "Email verified", Snackbar.LENGTH_LONG).show();
                        email_verify_text.setHint("verified");
                        email_verify_text.setEnabled(false);
                        btn_verify_email.setEnabled(false);
                        btn_verify_email.setText("verified");
                    }

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_verify_email:
                switch (type) {
                    case "sent mail":
                        try {
                            if (email_verify_text.getText().toString().equals("")) {
                                Snackbar.make(snack_view, "field cannot be empty", Snackbar.LENGTH_LONG).show();
                            } else {
                                Verify_Email();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "compare_code":
                        try {
                            if (email_verify_text.getText().toString().equals("")) {
                                Snackbar.make(snack_view, "field cannot be empty", Snackbar.LENGTH_LONG).show();
                            } else {
                                verify(email_verify_text.getText().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                break;
        }
    }
    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setMessage("Please Wait..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
}
