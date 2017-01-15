package com.example.tho.daa_service.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.tho.daa_service.Controller.Singleton;
import com.example.tho.daa_service.Models.ResponseData.Bean;
import com.example.tho.daa_service.Models.ResponseData.IdentitySPData;
import com.example.tho.daa_service.Models.Utils.Config;
import com.example.tho.daa_service.R;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import mehdi.sakout.fancybuttons.FancyButton;

import static android.R.attr.width;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class QRCodeActivity extends AppCompatActivity {

    public final String TAG = "QRCodeActivity";

    ImageView qrCodeImageview;
    String QRcodeContent;
    public final static int WIDTH=500;
    // Mode QR
    public static Integer QRCode_INTERNET = 2909;
    public static Integer QRCode_BLUETOOTH = 2402;
    Singleton singleton;
    IdentitySPData identitySP_Data;
    FancyButton btnExit;
    SharedPreferences mPrefs = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        singleton = Singleton.getInstance();
        identitySP_Data = singleton.getIdentitySPData();

        btnExit = (FancyButton) findViewById(R.id.btn_online_exit);
        mPrefs = this.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);

        setTitle("QRCode");
        //
        getID();
        //
       // createQRCode("online");

    }

    @Override
    protected void onStart() {
        super.onStart();

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                Bean bean = new Bean("Tho", date.toString(),"Student",true);
                singleton.addLog(bean);

                Gson gson1 = new Gson();
                String jsonzx = gson1.toJson(singleton.getmList());
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.putString("LogList",jsonzx).commit();
            }
        });
    }

    public String getName() throws JSONException {

        String jsonBank = identitySP_Data.getLevel_customer();
        JSONObject json = new JSONObject(jsonBank);
        return json.getString("service_name");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void createQRCode(String s){


        final JSONObject jsonInput = new JSONObject();
        try {
            jsonInput.put("mode",s);
            jsonInput.put("appID", Config.APP_ID.toString());
            jsonInput.put("name", getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        QRcodeContent = jsonInput.toString();






//        // create thread to avoid ANR Exception
//        Thread t = new Thread(new Runnable() {
//            public void run() {
//                // this is the msg which will be encode in QRcode
//                QRcodeContent = jsonInput.toString();




//                try {
//                    synchronized (this) {
//                        wait(500);
//                        // runOnUiThread method used to do UI task in main thread.
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    Bitmap bitmap = null;
//
//                                    bitmap = encodeAsBitmap(QRcodeContent);
//                                    qrCodeImageview.setImageBitmap(bitmap);
//
//                                } catch (WriterException e) {
//                                    e.printStackTrace();
//                                } // end of catch block
//
//                            } // end of run method
//                        });
//
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }


//
//            }
//        });
//        t.start();

    }

    private void getID() {
        qrCodeImageview=(ImageView) findViewById(R.id.img_qr_code_image);
    }

    // this is method call from on create and return bitmap image of QRCode.
    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                //pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.black):getResources().getColor(R.color.white);
                pixels[offset + x] = result.get(x, y) ? ContextCompat.getColor(this, android.R.color.black):ContextCompat.getColor(this, android.R.color.white);
            }

        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        return bitmap;
    } /// end of this method

    Bitmap encodeAsBitmap1(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, w, h);
        return bitmap;
    }
}
