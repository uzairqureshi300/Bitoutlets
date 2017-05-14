package ourwallet.example.com.ourwallet.YMC_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ourwallet.example.com.ourwallet.R;

public class Address_verification_fragment extends Fragment {
private EditText document_type,issuence_date,adress_one,address_two,country,state,city;
private ImageView upload_image;
    private Button btn_verify_address;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v =  inflater.inflate(R.layout.address_verify_fragment, container, false);
        document_type=(EditText)v.findViewById(R.id.input_document_type);
        issuence_date=(EditText)v.findViewById(R.id.issuence_date);
        adress_one=(EditText)v.findViewById(R.id.input_addressline_1);
        address_two=(EditText)v.findViewById(R.id.input_address_line_2);
        country=(EditText)v.findViewById(R.id.input_country);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
