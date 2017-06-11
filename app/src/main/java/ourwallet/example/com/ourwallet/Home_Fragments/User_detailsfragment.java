package ourwallet.example.com.ourwallet.Home_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ourwallet.example.com.ourwallet.Constants;
import ourwallet.example.com.ourwallet.R;

/**
 * Created by hassan on 11/15/16.
 */
public class User_detailsfragment extends Fragment {
    private TextView adress;
    private TextView balance;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);



        View v =  inflater.inflate(R.layout.userdetail_fragment, container, false);
        adress=(TextView)v.findViewById(R.id.adress);
        balance=(TextView)v.findViewById(R.id.bal);
        adress.setText(Constants.adress);
        balance.setText(Constants.balance);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
