package com.example.tho.daa_service.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.tho.daa_service.Controller.Singleton;
import com.example.tho.daa_service.Models.ResponseData.IdentitySPData;
import com.example.tho.daa_service.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    private final  String TAG = "ProfileActivity";

    TextView txtName, txtBank;
    Singleton singleton = null;
    String name = null, account = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle("Service Infomation");

        singleton = Singleton.getInstance();

        try {
            getInfo(singleton.getIdentitySPData());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtName = (TextView) findViewById(R.id.txt_profile_name);

        txtBank = (TextView) findViewById(R.id.txt_profile_bank);

    }

    @Override
    protected void onStart() {
        super.onStart();


        txtName.setText(name);
        txtBank.setText(account);

    }

    public void getInfo(IdentitySPData identitySPData) throws JSONException {
        String jsonBank = identitySPData.getLevel_customer();

        JSONObject json = new JSONObject(jsonBank);


        name = json.getString("service_name");
        account = json.getString("service_account");



    }
}
