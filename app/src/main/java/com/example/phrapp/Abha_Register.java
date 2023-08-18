package com.example.phrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Abha_Register extends AppCompatActivity {

    private Button verify_otp_btn;
    private Button get_otp_btn;
    private EditText aadhaar_no_et;
    private String txnid;
    private EditText otp_et;

    private Intent to_registered_successfully_act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abha_register);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.abha_register_fragment_frame, new AbhaRegisterVerifyAadhaarOtpFragment() ).commit();

    }

}


