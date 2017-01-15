package com.example.tho.daa_service.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tho.daa_service.CheckBoxView.SmoothCheckBox;
import com.example.tho.daa_service.Controller.BluetoothService;
import com.example.tho.daa_service.Controller.Constants;
import com.example.tho.daa_service.Controller.Singleton;
import com.example.tho.daa_service.Models.DAA.Authenticator;
import com.example.tho.daa_service.Models.DAA.Issuer;
import com.example.tho.daa_service.Models.DAA.Verifier;
import com.example.tho.daa_service.Models.ResponseData.IdentitySPData;
import com.example.tho.daa_service.Models.Utils.Utils;
import com.example.tho.daa_service.Models.crypto.BNCurve;
import com.example.tho.daa_service.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class BluetoothActivity extends AppCompatActivity {

    private static final String TAG = "BluetoothActivity";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    public final static int WIDTH=500;
    String QRcodeContent;

    //View
    SmoothCheckBox cbs1, cbs2, cbs3, cbs4;
    ImageView qrCodeImageview;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothService mChatService = null;

    //SingleTon
    Singleton singleton = null;

    BNCurve curve = null;
    IdentitySPData identitySPData = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        singleton = Singleton.getInstance();
        curve = singleton.getCurve();
        identitySPData = singleton.getIdentitySPData();



        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            this.finish();
        }

        createQRCode();
    }

    public void initView(){
        cbs1 = (SmoothCheckBox) findViewById(R.id.scb1);
        cbs2 = (SmoothCheckBox) findViewById(R.id.scb2);
        cbs3 = (SmoothCheckBox) findViewById(R.id.scb3);
        cbs4 = (SmoothCheckBox) findViewById(R.id.scb4);

        qrCodeImageview = (ImageView) findViewById(R.id.img_qr_code_image1);

        cbs1.setClickable(false);
        cbs2.setClickable(false);
        cbs3.setClickable(false);
        cbs4.setClickable(false);
    }

    public String getName() throws JSONException {

        String jsonBank = identitySPData.getLevel_customer();
        JSONObject json = new JSONObject(jsonBank);
        return json.getString("service_name");
    }

    public void createQRCode(){


        final JSONObject jsonInputx = new JSONObject();
        try {
            jsonInputx.put("mode","offline");

            jsonInputx.put("name", getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }




        // create thread to avoid ANR Exception
        Thread t = new Thread(new Runnable() {
            public void run() {
                // this is the msg which will be encode in QRcode
                QRcodeContent = jsonInputx.toString();



                try {
                    synchronized (this) {
                        wait(5000);
                        // runOnUiThread method used to do UI task in main thread.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Bitmap bitmap = null;

                                    bitmap = encodeAsBitmap(QRcodeContent);
                                    qrCodeImageview.setImageBitmap(bitmap);

                                } catch (WriterException e) {
                                    e.printStackTrace();
                                } // end of catch block

                            } // end of run method
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            }
        });
        t.start();

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
    }


    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }


    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread


        // Initialize the BluetoothService to perform bluetooth connections
        mChatService = new BluetoothService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * Makes this device discoverable.
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 500);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            //mOutEditText.setText(mOutStringBuffer);
        }
    }

    /**
     * The action listener for the EditText widget, to listen for the return key
     */
    private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };


    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {

        final ActionBar actionBar = this.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {

        final ActionBar actionBar = this.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            // mConversationArrayAdapter.clear();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add("Me:  " + writeMessage);
                    Toast.makeText(BluetoothActivity.this, writeMessage, Toast.LENGTH_LONG).show();
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d("message",readMessage);
                    ///HANDLE Message
                    Toast.makeText(BluetoothActivity.this, readMessage, Toast.LENGTH_LONG).show();
                    try {
                        messageHandle(readMessage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != BluetoothActivity.this) {

                        Toast.makeText(BluetoothActivity.this, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();

                        //******************* TEST

                        String type = msg.getData().getString("secure");
                        if (type == "Secure") {
                            mChatService.tho();
//                            String address = mConnectedDeviceName;
//                            // Get the BluetoothDevice object
//                            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//                            // Attempt to connect to the device
//                            mChatService.connect(device, false);
                        }
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != BluetoothActivity.this) {
                        Toast.makeText(BluetoothActivity.this, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    this.finish();
                }
        }
    }

    /**
     * Establish connection with other divice
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bluetooth_chat, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(BluetoothActivity.this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(BluetoothActivity.this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }

    public void messageHandle(String messageJSON) throws JSONException {


        JSONObject json = new JSONObject(messageJSON);
        String state = json.getString("state");

        switch (state) {
            case "sessionID":
                //Get User sessionID
                cbs1.setChecked(true);
                //
                String sesssionID = json.getString("sessionID");

                //CreateSig
                Authenticator.EcDaaSignature signatureSP = createSig(identitySPData.getPermission(),
                        identitySPData.getCredential_permission(),
                        identitySPData.getGsk_permission(),
                        sesssionID,
                        "permission", identitySPData.getIpk());

                //Encode Sig
                String sigString = Utils.bytesToHex(signatureSP.encode(curve));
                String s = "permission";
                //Create Service SessionID
                singleton.setSessionID(Utils.createSessionID());

                JSONObject jsonInput = new JSONObject();
                try {
                    jsonInput.put("state", s);
                    jsonInput.put("permission", identitySPData.getPermission());
                    jsonInput.put("sig", sigString);
                    jsonInput.put("sessionID", singleton.getSessionID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //send message
                sendMessage(jsonInput.toString());
                cbs2.setChecked(true);

                //Toast.makeText(BluetoothActivity.this, "sessionID" , Toast.LENGTH_SHORT);
                break;

            case "verification":

                cbs3.setChecked(true);

                //Get Service sessionID
                String UserSig = json.getString("sig");

                String info = json.getString("info");

                Issuer.IssuerPublicKey ipk = new Issuer.IssuerPublicKey(curve, identitySPData.getIpk());

                //Verify
                String SPsessionID = singleton.getSessionID();
                boolean temp = false;
                try {
                    temp = verifyEcDaaSigWrt(ipk, UserSig, SPsessionID, "verification",info.getBytes(), SPsessionID.getBytes());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                if (temp == true) {

                    cbs4.setChecked(true);
                    JSONObject jsonInput1 = new JSONObject();
                    try {
                        jsonInput1.put("state", "SUCCESS");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //send message
                    sendMessage(jsonInput1.toString());

                } else {
                    //If verify fail set sessionID = null
                    singleton.setSessionID(null);
                    JSONObject jsonInput1 = new JSONObject();
                    try {
                        jsonInput1.put("state", "CANCEL");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //send message
                    sendMessage(jsonInput1.toString());
                }
                Toast.makeText(BluetoothActivity.this, "sessionID" , Toast.LENGTH_SHORT);
                break;
            case "CANCEL":
                singleton.setSessionID(null);
                Toast.makeText(this, "Xác thực thất bại",
                        Toast.LENGTH_SHORT).show();
            break;

        }

    }



    //CREATE SIG
    private Authenticator.EcDaaSignature createSig(String info, String cre, String gsk, String sid, String basename, String ipkString) {
        try {
            Issuer.IssuerPublicKey ipk = new Issuer.IssuerPublicKey(curve, ipkString);
            Authenticator au = new Authenticator(curve, ipk, new BigInteger(gsk));
            Issuer.JoinMessage2 jm2 = new Issuer.JoinMessage2(curve, cre);
            au.setJoinState(Authenticator.JoinState.IN_PROGRESS);
            boolean x = au.EcDaaJoin2Wrt(jm2, info);

            if (x == true) {
                Log.d(TAG + "join", "Success");
            } else {
                Log.d(TAG + "join", "Fail");
            }
            Authenticator.EcDaaSignature sig = au.EcDaaSignWrt(info.getBytes(), basename, sid);

            return sig;
        } catch (NoSuchAlgorithmException ex) {


            return null;

        }

    }

    //VERRIFY
    private boolean verifyEcDaaSigWrt(Issuer.IssuerPublicKey pk, String sig, String message, String basename,
                                      byte[] info, byte[] session) throws NoSuchAlgorithmException {

        Verifier ver = new Verifier(curve);
        Authenticator.EcDaaSignature signature = new Authenticator.EcDaaSignature(
                Utils.hexStringToByteArray(sig), message.getBytes(), curve);
        //compare krd to session
        return ver.verifyWrt(info, session, signature, basename, pk, null);
    }

}

