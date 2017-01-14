package com.example.tho.daa_service.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tho.daa_service.Controller.Singleton;
import com.example.tho.daa_service.Interfaces.IdentitySPDataAPI;
import com.example.tho.daa_service.Models.ResponseData.IdentitySPData;
import com.example.tho.daa_service.Models.Utils.Config;
import com.example.tho.daa_service.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import info.hoang8f.widget.FButton;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    
    public final String TAG = "MainActivity";

    FButton btn_Authentication ;
    FancyButton btn_Profile;
    FancyButton btn_NewCre;
    FancyButton btn_Log;
    FancyButton btnLogout;
    TextView txtname;

    SharedPreferences mPrefs = null;
    Singleton singleton = null;

    IdentitySPData identitySP_Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Init Views
        initView();

        mPrefs = this.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        singleton = Singleton.getInstance();
        // String a = mPrefs.getString("AnoID", "");

        identitySP_Data = singleton.getIdentitySPData();


    }

    @Override
    protected void onStart() {
        super.onStart();

        btn_Authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        btn_NewCre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadIdentityData();
            }
        });

        btn_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_Log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public String getName() throws JSONException {

        String jsonBank = identitySP_Data.getLevel_customer();
        JSONObject json = new JSONObject(jsonBank);
        return json.getString("user_name");
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();


        // unregister the ACTION_FOUND receiver.
        //  unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void initView(){
        //View Init
        btn_Authentication = (FButton) findViewById(R.id.btn_authenticate);
        btn_Profile = (FancyButton) findViewById(R.id.btn_main_profile);
        btn_NewCre = (FancyButton) findViewById(R.id.btn_newCre);
        btn_Log = (FancyButton) findViewById(R.id.btn_Log);
        btnLogout = (FancyButton) findViewById(R.id.btn_LogOut);
        txtname = (TextView) findViewById(R.id.txtName);

        //sessionUser = Utils.createSessionID();
    }

    public void downloadIdentityData(){
        final KProgressHUD progressHUD = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Renew Ano-Id")
                .setDetailsLabel("Processing");
        progressHUD.show();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.URL_VERIFIER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IdentitySPDataAPI service = retrofit.create(IdentitySPDataAPI.class);

        Call<IdentitySPData> call = service.downloadFile(Config.APP_ID);

        call.enqueue(new Callback<IdentitySPData>() {
            @Override
            public void onResponse(Call<IdentitySPData> call, Response<IdentitySPData> response) {
                IdentitySPData identitySP_Datax = response.body();
                //initData();
                Log.d("identity", identitySP_Data.getPermission());
                singleton.setIdentitySPData(identitySP_Datax);
                Log.d(TAG+"Ano", "Success");

                Gson gson1 = new Gson();
                String json = gson1.toJson(identitySP_Datax);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.putString("AnoID",json).commit();



            }

            @Override
            public void onFailure(Call<IdentitySPData> call, Throwable t) {
                Log.d(TAG, "onResponse" + t.getMessage());
            }
        });



    }
}
