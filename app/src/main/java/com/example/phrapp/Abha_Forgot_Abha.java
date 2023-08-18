package com.example.phrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.phrapp.R;

public class Abha_Forgot_Abha extends AppCompatActivity {

    private TextView retrive_abha_using_aadhaar_see_tv;
    private TextView retrive_abha_using_mobile_see_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abha_forgot_abha);

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
                .replace(R.id.abha_forgot_abha_fragment_frame, new retrieve_abha_id_via_aadhaar() ).commit();

//        retrive_abha_using_aadhaar_see_tv = findViewById(R.id.abha_forgot_abha_using_aadhaar_btn_tv);
//        retrive_abha_using_mobile_see_tv = findViewById(R.id.abha_forgot_abha_using_mobile_btn_tv);


//        retrive_abha_using_aadhaar_see_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                retrive_abha_using_aadhaar_see_tv.setBackground(getDrawable(R.drawable.circular_border_left_top_bottom));
//                retrive_abha_using_mobile_see_tv.setBackground(null);
//
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
//                        .replace(R.id.abha_forgot_abha_fragment_frame, new retrieve_abha_id_via_aadhaar()).commit();
//
//            }
//        });

//        retrive_abha_using_mobile_see_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                retrive_abha_using_mobile_see_tv.setBackground(getDrawable(R.drawable.circular_border_left_top_bottom));
//                retrive_abha_using_aadhaar_see_tv.setBackground(null);
//
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
//                        .replace(R.id.abha_forgot_abha_fragment_frame, new retrieve_abha_id_via_mobile_generate_otp()).commit();
//
//            }
//        });
    }
}