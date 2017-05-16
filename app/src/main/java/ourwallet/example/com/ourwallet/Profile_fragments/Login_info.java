package ourwallet.example.com.ourwallet.Profile_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ourwallet.example.com.ourwallet.Constants;
import ourwallet.example.com.ourwallet.R;

public class Login_info extends Fragment {
    private EditText name, password, status;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.login_info, container, false);
        name = (EditText) v.findViewById(R.id.user_name);
        password = (EditText) v.findViewById(R.id.password);
        status = (EditText) v.findViewById(R.id.status);
        setData();
        return v;
    }

    private void setData() {
        name.setText(Constants.username);
        password.setText(Constants.password);
        if(Constants.status.equals("1")) {
            status.setText("Active");
        }
        else{
            status.setText("Not Active");

        }
        }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
