package com.golan.amit.softsuduku;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DEBUGTAG = "AMGO";
    public static final boolean DEBUG = false;

    SoftSudukuHelper ssh ;
    TextView tvInfo;
    Button btnPick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        init();

//        setListener();

        redirect();

//        playOnce();
    }

    private void redirect() {
        Intent i = new Intent(MainActivity.this, SoftSudukuActivity.class);
//        Intent i = new Intent(this, CompletedMatrixActivity.class);
//        Intent i = new Intent(this, CompletedMatrixActivity.class);
        startActivityForResult(i, 1);
    }

    private void setListener() {
        btnPick.setOnClickListener(this);
    }

    private void playOnce() {

        String tmpInfo = "";
        ssh.generateRandomNumber();
        for (int i = 0; i < 3; i++) {
            while (ssh.isNumInRandomArray()) {
                ssh.generateRandomNumber();
                Log.i(DEBUGTAG, "inside while");
            }
            ssh.insertRndNumToArray();
            ssh.increaseRndIndexer();
            ssh.displayRndNums();
            tmpInfo += String.valueOf(ssh.getRandomNumber());
        }
        tvInfo.setText(tmpInfo);
    }

    private void init() {
        ssh = new SoftSudukuHelper();
        tvInfo = findViewById(R.id.tvInformation);
        btnPick = findViewById(R.id.btnPick);
    }

    @Override
    public void onClick(View v) {
        if (v == btnPick) {
            ssh.generateRandomNumber();
            while (ssh.isNumInRandomArray()) {
                ssh.generateRandomNumber();
                Log.i(DEBUGTAG, "in onclick -> while, picked: " + ssh.getRandomNumber() +
                        ", now seq: " + ssh.returnRndNumsAsString());
            }
            ssh.insertRndNumToArray();
            ssh.increaseRndIndexer();
            tvInfo.setText(String.valueOf(ssh.getRandomNumber()) + ", seq: " + ssh.returnRndNumsAsString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED) {
            redirect();
        }
    }
}
