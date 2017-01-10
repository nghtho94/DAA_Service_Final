package com.example.tho.daa_service.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Handler;

import com.dd.CircularProgressButton;
import com.example.tho.daa_service.R;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);

    //View
    CircularProgressButton circularProgressButton;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        circularProgressButton = (CircularProgressButton) findViewById(R.id.btnlogin);
        password = (EditText) findViewById(R.id.login_text_edit);
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
    protected void onStart() {
        super.onStart();
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pass = password.getText().toString();

                if (checkPassword(pass) == true){

                    if (circularProgressButton.getProgress() == 100){
                        circularProgressButton.setProgress(0);
                    }else {
                        circularProgressButton.setIndeterminateProgressMode(true); // turn on indeterminate progress
                        circularProgressButton.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                circularProgressButton.setProgress(100);

                                //
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);

                                //Close Activity
                                finish();
                            }
                        }, 300);
                    }

                }else{

                    Toast.makeText(LoginActivity.this,"WRONG PASSWORD", Toast.LENGTH_SHORT).show();

                }



                // set progress to 100 or -1 to indicate complete or error state
                ; // set progress to 0 to switch back to normal state
            }
        });
    }

    public boolean checkPassword(String pd){
        String json = mPrefs.getString("password","");

        if (json != null){
            if (pd == json) {
                return true;
            }else{
                return false;
            }
        }else{
            if (pd == "Bob"){
                return true;
            }else{
                return false;
            }
        }

    }
}
