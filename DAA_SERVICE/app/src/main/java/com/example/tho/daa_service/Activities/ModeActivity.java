package com.example.tho.daa_service.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tho.daa_service.Interfaces.LoginAPI;
import com.example.tho.daa_service.Models.ResponseData.VerifyResponse;
import com.example.tho.daa_service.Models.Utils.CheckInternet;
import com.example.tho.daa_service.Models.Utils.Config;
import com.example.tho.daa_service.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModeActivity extends AppCompatActivity {

    public final String TAG = "ModeActivity";

    private static final int REQUEST_ENABLE_BT = 3;

    FancyButton btnOnline , btnOffline;
    private KProgressHUD progressHUD;

    private BluetoothAdapter mBluetoothAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        setTitle("Mode");

        //InitView
        initView();
        //Set Title
        setTitle(TAG);

        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                online();
            }
        });

        btnOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offline();
            }
        });
    }

    public void initView(){
        btnOffline = (FancyButton) findViewById(R.id.btn_mode_offline);
        btnOnline = (FancyButton) findViewById(R.id.btn_mode_online);
    }

    public void online(){


        if (CheckInternet.isConnected(ModeActivity.this) == true){
            //go to QRScan

            Intent i = new Intent(ModeActivity.this, QRCodeActivity.class);
            startActivity(i);
            finish();

        }else{
            Toast.makeText(ModeActivity.this, "Wifi not connected. Try again.", Toast.LENGTH_LONG).show();
        }
    }

    public void offline(){
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            this.finish();
        }

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        }
    }

    public void loginVerifier(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.URL_VERIFIER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        LoginAPI service = retrofit.create(LoginAPI.class);

        Call<VerifyResponse> call = service.login(Config.APP_ID, "123456");

        call.enqueue(new Callback<VerifyResponse>() {
            @Override
            public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {

            }

            @Override
            public void onFailure(Call<VerifyResponse> call, Throwable t) {

            }
        });
    }






    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session

                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();

                }
        }
    }

    private void scheduleDismiss() {

        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data");
        progressHUD.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressHUD.dismiss();

            }
        }, 2000);
    }

}
