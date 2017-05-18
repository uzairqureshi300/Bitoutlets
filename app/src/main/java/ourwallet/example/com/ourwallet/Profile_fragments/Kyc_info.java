package ourwallet.example.com.ourwallet.Profile_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ourwallet.example.com.ourwallet.Constants;
import ourwallet.example.com.ourwallet.R;


public class Kyc_info extends Fragment {
    public static TextView mobile_verify, email_verify, photo_verify, address_verify;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.kyc, container, false);
        mobile_verify = (TextView) v.findViewById(R.id.mobile_verify);
        email_verify = (TextView) v.findViewById(R.id.email_verify);
        address_verify = (TextView) v.findViewById(R.id.address_verify);
        setData();
        return v;
    }
    private void setData() {
        mobile_verify.setText(Constants.verification_mobile);
        email_verify.setText(Constants.varification_email);
        address_verify.setText(Constants.verification_address);
    }

}
