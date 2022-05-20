package com.golan.amit.softsuduku;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.TextView;

public class CompletedMatrixActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_SMS = 1;
    TextView tvCompleteDisplay;
    Button btnAnotherGame, btnFinish;

    String endMessage = "";
    private String telNumber = "0522412342";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_matrix);

        init();

        sendSMS();
    }

    private void sendSMS() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SMS);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) ==
                PackageManager.PERMISSION_GRANTED) {
            try {
                if(MainActivity.DEBUG) {
                    Log.i(MainActivity.DEBUGTAG, "sending sms to " + telNumber);
                }
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(telNumber, null, endMessage, null, null);
            } catch (Exception esms) {
                Log.e(MainActivity.DEBUGTAG, "exception when trying to send SMS");
            }
        }
    }

    private void init() {
        tvCompleteDisplay = findViewById(R.id.tvCompleteDisplay);
        btnAnotherGame = findViewById(R.id.btnAnotherGameId);
        btnFinish = findViewById(R.id.btnFinishId);

        endMessage = getIntent().getStringExtra(getString(R.string.end_message));
        if(endMessage != null && !endMessage.isEmpty()) {
            tvCompleteDisplay.setText(endMessage);
        }
    }

    public void btn_AnotherGame(View v) {
        Intent i = new Intent(this, SoftSudukuActivity.class);
        startActivity(i);
    }

    public void btn_Finish(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
    }
}
