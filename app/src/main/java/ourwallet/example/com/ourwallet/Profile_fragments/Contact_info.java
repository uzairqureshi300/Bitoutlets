package ourwallet.example.com.ourwallet.Profile_fragments;

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

import com.android.volley.VolleyError;

import org.json.JSONObject;

import ourwallet.example.com.ourwallet.Constants;
import ourwallet.example.com.ourwallet.R;

public class Contact_info extends Fragment implements com.android.volley.Response.Listener<JSONObject>,
        com.android.volley.Response.ErrorListener,View.OnClickListener{

  public static  EditText full_name, email, number, country, city, address1, address2;
    public static Button change;

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
        change=(Button) v.findViewById(R.id.change);
        setData();
        Log.e("values",String.valueOf(Constants.fragments_values));


        return v;
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
private void set_Listeners(){
    change.setOnClickListener(this);
}

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }
}
